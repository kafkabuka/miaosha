package com.bnz.miaosha.seckill.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.bnz.miaosha.seckill.config.AlipayConfig;
import com.bnz.miaosha.seckill.service.AlipayService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {

	@Override
	public String toAlipay(Map<String, String> sourceMap) throws IOException {
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		//商户订单号，商户网站订单系统中唯一订单号，必填
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ sourceMap.get("out_trade_no") +"\"," 
				+ "\"total_amount\":\""+ sourceMap.get("total_amount") +"\"," 
				+ "\"subject\":\""+ "Alipay" +"\"," 
				+ "\"body\":\""+ sourceMap.get("body") +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
	}


}
