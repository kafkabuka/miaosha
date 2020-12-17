package com.bnz.miaosha.seckill.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Properties;

public class AlipayConfig {
	

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016102400749793";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDLUwuDikfJp9KfQ4OFClCP/GA+dy86YVxPLWpcMNjNukzdOjXJm4tKcAqkoXY6KxyRka4fxwu659IKaevBX+JzyMJn/WHJGVJUv4lKOVrp2M7bc/JuC7Km0lIyA5gTCFRpzTgmsvEYJP6DQ/DSl7Hal6i+RF5Un3eOXxLhYrDPED/dpN3UR2Wo4FNCUDvbHL/bwRmjxBL+dw0TtMAdI6cqathdaT+Aav3ZScyrHa3W6pEecT1McAy0UA6lPad3WXaz0ETTZWNraYZh133c1UUL2nIFJHgz4FmlRa0QCvJTx52QjTTfoIN51mi4HRF9uJXsxsGd3Zc1xPwCX3gq0kCdAgMBAAECggEASfS09W2w2vq+M8v85MFxuE3WPCJh/Vw4lVwxmcLyuTIQj0+DDQgUzEigtm08ccAIrjJNIaXm8F8uGTeZ10anOfJGNEkH7nvVZJoet+7Q0MpZw50TC1AZ9W//Ec4Y3KNlAO7yg7EG3P9nzBtEV3Vx7c7IRBzeKu2uMQ0H8w9QvJ/HzA4wVe5kDU9y1ZgWh4upeyDd+rn9THYhsuMd9W8gmU8GAWlSn/DMUt/I58LTLsryICVPUUkAXsCYj3oeZyIy4COkwZWHf1Wk2a00Ie9o+yGX6mPWEgriyAL0l+NJBpmrmSq4vo7FS4RLBz0IJyC4r9f/sgSIO0pYlfCxaEXrQQKBgQD6SU1IN1wxUHJNl8SR8vrd2PyEDHvSvMH6svrVHIHhVBzge/b4zvLspAhUHE0BRm4qObXv+BnLszJWXvewmt6irj0WSPyrDvwxlFf8goKUFxttlFacSaC0J/g74+1pEOSsmrRi1wIA6Y98cuZ0VVOuB+bIgSSpHGAmhoyGelwr2QKBgQDP90r48uj+DSzVeqkWcMv65UvBxrTo1cjwAGiijNonLBJPE/L5SxaNhyxAspnUbqf+N8taJUZHG1oqy8vyglmv5EGpeCZBMKna66ytHQdg4Qtci3udF5880OYf+aoEGNMRD7mSCTnMvyArkm1eX/3712EQDQTwa7bL5rqzvEgUZQKBgQCvEisewqqYTB9IK0rylDXqjGdFrr3GUQaW6C8Qdtpa1U1FsTC2JXxDDxJMU2eB5tQUcT3UV4UjuXsSc2dqf0oQHTTZq8Ahn1/FBnDtThUspWUoJ0TWGaJxoiu/KNXeUlTCb4ySPrdGEBmwAWc2JKFd8GT8B2mvaQvQpvkQ7/IdMQKBgA8dPuKm7M4B8R82wfEyk3CRl5bmtSEy4F6o+PHPYM92AYKjxYXesi3pBAtTr6BjxmvQbEQJ73idgUkY9KKUXKZO5YLGi6kzOzUA6dTPBklBMc0xCfPZ0qXd3Mu4HmYb08MqrGbzqrK5tUHVa4NvYIscFR7Qg2phubBJ/YmwhmCNAoGALV27e1eM6l+U0a9qDx6LyB2Udf01Qj6lIy5R7qQI+raf0IXUad3lYloIFgwo6Z9Xc/3s/mhF52mTLPv6VxStviNo7MITD2oqLBe2iDVcwBuoXq18Z8e4MSijk/jhekjiK5v0fM9wkGjj2xrT61PxZCKvswrunX3+ZHN/Bha/5dM=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkJEhXOXpAg3iTRq41vG/EoLaICCGoC9qo+DF2hA8QVF4qojwUVyq2o98XvoVmfL0+ZmSbIJBthwcTq/oNSuWiLhOQcslQkPfuCINbScm9bnDpumk0CQnp/4oFKZ9q/ry1Gy+C5Qsvfuo0xte27dR6A0TQ1lGMOxZJetF05XJkj98UDOg8aHWb5A9LY/LSXP703xGY5QvvruF3HmCAHUXe4l7xWE4TJSy7/OkkJVbikZlAHHIi5RAojtjNghgsJK6vdHI0pzKNU8/QvFHUayxvTyslrem+G9Yca/Xz+E4nYi8yss1NURU3gILQKMwQ0WLNm5wQBJprBV95NU79cIifQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://127.0.0.1/alipay/notifyUrl";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://127.0.0.1/alipay/returnUrl";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

	public static String payCallbackUrl = "http://127.0.0.1:8080/#/second-kill";

	// 支付宝网关
	public static String log_path = "E:/logs";


    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static final String ALIPAY_DEMO = "alipay_demo";
    public static final String ALIPAY_DEMO_VERSION = "alipay_demo_JAVA_20180907112129";

    /**
     * 配置文件加载
     */
    private static Properties prop;

    /**
     * 配置文件名称
     */
    public static String CONFIG_FILE = "Alipay-Config.properties";

    /**
     * 配置文件相对路径
     */
    public static String ALIPAY_CONFIG_PATH = File.separator + "etc" + File.separator + CONFIG_FILE;

    /**
     * 项目路径
     */
    public static String PROJECT_PATH = "";

    private static Log logger = LogFactory.getLog(AlipayConfig.class);

    /**
     * 初始化配置值
     */
    public static void initPropertis() {
        prop = new Properties();
        try {
            synchronized (prop) {
                InputStream inputStream = AlipayConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
                prop.load(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            logger.error("日志 =============》： 配置文件Alipay-Config.properties找不到");
            e.printStackTrace();
        }
    }

    /**
     * 获取配置文件信息
     *
     * @return
     */
    public static Properties getProperties() {
        if (prop == null)
            initPropertis();
        return prop;
    }


    /**
     * 配置信息写入配置文件
     */
    public static void writeConfig() {
        System.out.println("ALIPAY_CONFIG_PATH:"+ALIPAY_CONFIG_PATH);
        File file = new File(ALIPAY_CONFIG_PATH);
        if (file.exists()) {
            file.setReadable(true);
            file.setWritable(true);
        }
        String lineText = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bw = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            bufferedReader = new BufferedReader(new FileReader(ALIPAY_CONFIG_PATH));
            while ((lineText = bufferedReader.readLine()) != null) {

                if (lineText.startsWith("APP_ID")) {
                    lineText = "APP_ID = " + prop.getProperty("APP_ID");
                } else if (lineText.startsWith("RSA2_PRIVATE_KEY")) {
                    lineText = "RSA2_PRIVATE_KEY = " + prop.getProperty("RSA2_PRIVATE_KEY");
                } else if (lineText.startsWith("RSA2_PUBLIC_KEY")) {
                    lineText = "RSA2_PUBLIC_KEY = " + prop.getProperty("RSA2_PUBLIC_KEY");
                } else if (lineText.startsWith("ALIPAY_RSA2_PUBLIC_KEY")) {
                    lineText = "ALIPAY_RSA2_PUBLIC_KEY = " + prop.getProperty("ALIPAY_RSA2_PUBLIC_KEY");
                } else if (lineText.startsWith("NOTIFY_URL")) {
                    lineText = "NOTIFY_URL = " + prop.getProperty("NOTIFY_URL");
                } else if (lineText.startsWith("RETURN_URL")) {
                    lineText = "RETURN_URL = " + prop.getProperty("RETURN_URL");
                } else if (lineText.startsWith("SANDBOX_BUYER_EMAIL")) {
                    lineText = "SANDBOX_BUYER_EMAIL = " + prop.getProperty("SANDBOX_BUYER_EMAIL");
                } else if (lineText.startsWith("SANBOX_BUYER_LOGON_PWD")) {
                    lineText = "SANBOX_BUYER_LOGON_PWD = " + prop.getProperty("SANBOX_BUYER_LOGON_PWD");
                } else if (lineText.startsWith("SANBOX_BUYER_PAY_PWD")) {
                    lineText = "SANBOX_BUYER_PAY_PWD = " + prop.getProperty("SANBOX_BUYER_PAY_PWD");
                } else if (lineText.startsWith("SANDBOX_SELLER_ID")) {
                    lineText = "SANDBOX_SELLER_ID = " + prop.getProperty("SANDBOX_SELLER_ID");
                } else if (lineText.startsWith("SANDBOX_SELLER_EMAIL")) {
                    lineText = "SANDBOX_SELLER_EMAIL = " + prop.getProperty("SANDBOX_SELLER_EMAIL");
                } else if (lineText.startsWith("SANDBOX_SELLER_LOGON_PWD")) {
                    lineText = "SANDBOX_SELLER_LOGON_PWD = " + prop.getProperty("SANDBOX_SELLER_LOGON_PWD");
                } else if (lineText.startsWith("ALIPAY_GATEWAY_URL")) {
                    lineText = "ALIPAY_GATEWAY_URL = " + prop.getProperty("ALIPAY_GATEWAY_URL");
                } else if (lineText.startsWith("CHARSET")) {
                    lineText = "CHARSET = " + prop.getProperty("CHARSET");
                } else if (lineText.startsWith("FORMAT")) {
                    lineText = "FORMAT = " + prop.getProperty("FORMAT");
                } else if (lineText.startsWith("SIGNTYPE")) {
                    lineText = "SIGNTYPE = " + prop.getProperty("SIGNTYPE");
                }

                stringBuffer.append(lineText).append("\r\n");
            }
            bufferedReader.close();
            bw = new BufferedWriter(new FileWriter(ALIPAY_CONFIG_PATH));
            bw.write(stringBuffer.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @ClassName: Status
     * @Description: TODO(上传公钥回执状态码)
     * @author 君泓 junying.wjy
     * @date 2018年7月23日 下午4:40:47
     *
     */
    public final static class Status {

        /**
         * 上传成功
         */
        public final static String UPLOAD_SUCCESS = "UPLOAD_SUCCESS";

        /**
         * 上传失败
         */
        public final static String UPLOAD_FAILED = "UPLOAD_FAILED";

        /**
         * 同样的公钥
         */
        public final static String NOT_COVER = "NOT_COVER";

    }
}

