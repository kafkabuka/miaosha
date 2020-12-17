package com.bnz.miaosha.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.bnz.miaosha.entity.SeckillStatus;
import com.bnz.miaosha.seckill.config.AlipayConfig;
import com.bnz.miaosha.seckill.config.TokenDecode;
import com.bnz.miaosha.seckill.service.AlipayService;
import com.bnz.miaosha.seckill.service.SecKillOrderService;
import com.bnz.miaosha.seckill.utils.PayStatus;
import com.bnz.miaosha.seckill.utils.RedisConstans;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
@Slf4j
@Controller
@RequestMapping("/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;
    @Autowired
    private SecKillOrderService secKillOrderService;
	@Autowired
	private TokenDecode tokenDecode;
	@Autowired
    private RedissonClient redissonClient;

    @RequestMapping("/flashPay")
    public void flashPay(HttpServletRequest httpRequest,
                         HttpServletResponse httpResponse) throws IOException {
		String userId = tokenDecode.getUserInfo().get("id");
		SeckillStatus seckillStatus = secKillOrderService.getSeckillStatus(userId);
		log.info("订单状态:{}",seckillStatus);
		if (seckillStatus == null){
			return;
		}

        // 生成一笔支付记录，支付完成时将支付状态改为成功
        Map<String, String> sourceMap = new HashMap<>();
        sourceMap.put("out_trade_no", seckillStatus.getOrderId().toString());
        sourceMap.put("total_amount", seckillStatus.getMoney().toString());
        sourceMap.put("subject","秒杀专场" );
        sourceMap.put("body", getUTF8XMLString(JSON.toJSONString(seckillStatus)));

        String form = alipayService.toAlipay(sourceMap);
        httpResponse.setContentType("text/html;charset=" + AlipayConfig.charset);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }


    // 注意异步返回结果通知是以post请求形式返回的
    @RequestMapping("notifyUrl")
    public String notify_url(HttpServletRequest request) {
        Map<String, String> paramsMap = convertRequestParamsToMap(request);
        String out_trade_no = paramsMap.get("out_trade_no");
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key,
                    AlipayConfig.charset, AlipayConfig.sign_type);
            // 无论同步异步都要验证签名
            if (signVerified) {
                String trade_status = paramsMap.get("trade_status");
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    // 处理自己系统的业务逻辑，如：将支付记录状态改为成功，需要返回一个字符串success告知支付宝服务器
                    secKillOrderService.updateOrder(out_trade_no, 2);
                    return "success";
                } else {
                    // 支付失败不处理业务逻辑
                    secKillOrderService.updateOrder(out_trade_no, 3);
                    return "fail";
                }
            } else {
                // 签名验证失败不处理业务逻辑
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    // 注意同步返回结果是以get请求形式返回的
    @RequestMapping("/returnUrl")
    public void return_url(HttpServletRequest request, Model model, HttpServletResponse response) {
        Map<String, String> paramsMap = convertRequestParamsToMap(request);
        for (Entry<String, String> entry : paramsMap.entrySet()) {
            System.out.println("key: " + entry.getKey() + "\tvalue: " + entry.getValue());
        }
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key,
                    AlipayConfig.charset, AlipayConfig.sign_type);
            String orderId = paramsMap.get("out_trade_no");
			String userId = tokenDecode.getUserInfo().get("id");

            if (signVerified) {
                // 跳转支付成功界面

				// 删除redis抢购状态
                RMap<String, SeckillStatus> map = redissonClient.getMap(RedisConstans.SECKILL_USER_STATUS_QUEUE);
                SeckillStatus seckillStatus = map.remove(userId);
                if (seckillStatus == null ){
                    log.info("[支付服务]--AlipayController  删除redis抢购状态失败");
                }
				// 更改数据库订单状态
				secKillOrderService.updateOrder(orderId, PayStatus.FINISHED);
                response.sendRedirect(AlipayConfig.payCallbackUrl);
            } else {
                // 跳转支付失败界面
                SeckillStatus seckillStatus = secKillOrderService.getSeckillStatus(userId);
				// 更改redis订单状态
				seckillStatus.setStatus(PayStatus.FAIL); //支付失败
				secKillOrderService.putSeckillStatus(userId,seckillStatus);
				// 更改数据库订单状态
                secKillOrderService.updateOrder(orderId, PayStatus.FAIL);
            }
        } catch (AlipayApiException | IOException e) {
            e.printStackTrace();
        }
    }

    // 将请求中的参数转换为Map
    public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap();
        Set<Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();
        Iterator var3 = entrySet.iterator();

        while (true) {
            while (var3.hasNext()) {
                Entry<String, String[]> entry = (Entry) var3.next();
                String name = (String) entry.getKey();
                String[] values = (String[]) entry.getValue();
                int valLen = values.length;
                if (valLen == 1) {
                    retMap.put(name, values[0]);
                } else if (valLen <= 1) {
                    retMap.put(name, "");
                } else {
                    StringBuilder sb = new StringBuilder();
                    String[] var9 = values;
                    int var10 = values.length;

                    for (int var11 = 0; var11 < var10; ++var11) {
                        String val = var9[var11];
                        sb.append(",").append(val);
                    }

                    retMap.put(name, sb.toString().substring(1));
                }
            }

            return retMap;
        }
    }

    // 将字符串转换为UTF-8编码以防出现乱码错误
    public static String getUTF8XMLString(String xml) {
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8 = "";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return xmlUTF8;
    }

}
