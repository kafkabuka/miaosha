package com.bnz.miaosha.seckill.config;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MyConfig extends WXPayConfig {

//    private byte[] certData;
//
//    public MyConfig() throws Exception {
//        String certPath = "/apoclient_cert.p12";
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
//    }

    public String getAppID() {
        return "wx8397f8696b538317";
    }

    public String getMchID() {
        return "1473426802";
    }

    public String getKey() {
        return "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb";
    }


    public InputStream getCertStream() {
        byte[] bytes = new byte[8];
        ByteArrayInputStream certBis = new ByteArrayInputStream(bytes);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
    public IWXPayDomain getWXPayDomain(){
//        // 这个方法需要这样实现, 否则无法正常初始化WXPay
//        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
//
//            public void report(String domain, long elapsedTimeMillis, Exception ex) {
//
//            }
//
//            public DomainInfo getDomain(WXPayConfig config) {
//                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
//            }
//        };
//        return iwxPayDomain;

        return new IWXPayDomain() {
            @Override
            public void report(String s, long l, Exception e) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig wxPayConfig) {
                return new DomainInfo(WXPayConstants.DOMAIN_API,true);
            }
        };
    }
}
