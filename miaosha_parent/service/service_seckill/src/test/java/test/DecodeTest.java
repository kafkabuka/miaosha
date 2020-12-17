package test;

import com.bnz.miaosha.seckill.SecKillApplication;
import com.bnz.miaosha.seckill.config.TokenDecode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecKillApplication.class})
public class DecodeTest {
    @Test
    public void endecode(){
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTYwMTQzNjAwMywiYXV0aG9yaXRpZXMiOlsiU3lzdGVtQ29udGVudERlbGV0ZSIsIlN5c3RlbVVzZXJWaWV3IiwiU3lzdGVtVXNlciIsIlN5c3RlbVVzZXJJbnNlcnQiLCJTeXN0ZW1Vc2VyRGVsZXRlIiwiU3lzdGVtVXNlclVwZGF0ZSIsIlN5c3RlbUNvbnRlbnRJbnNlcnQiLCJTeXN0ZW1Db250ZW50VXBkYXRlIiwiU3lzdGVtQ29udGVudFZpZXciLCJzZWNraWxsb3JkZXIiLCJTeXN0ZW0iLCJTeXN0ZW1Db250ZW50Il0sImp0aSI6ImY0ZTViMjZmLTA1MTMtNDM1YS05NjNlLTQxNTlhMzkzNDBhZiIsImNsaWVudF9pZCI6Im1pYW9zaGEiLCJ1c2VybmFtZSI6IntcImNyZWF0ZWRcIjoxNjAxMTkyNTk1MDAwLFwiaWRcIjozNyxcImlzRW1haWxDaGVja1wiOlwiMFwiLFwiaXNNb2JpbGVDaGVja1wiOlwiMFwiLFwicGFzc3dvcmRcIjpcIiQyYSQxMCQ5ejJNWWsxMGtTMS9ld0cxMFRFUTMuc3Z5QjIyTWtpeEdWSTlzUnZKZHI3SlhPZnVHWXF1Q1wiLFwicGhvbmVcIjpcIjEzNTEyMzQ1Njc4XCIsXCJzZXhcIjpcIjBcIixcInN0YXR1c1wiOlwiMVwiLFwidXBkYXRlZFwiOjE2MDExOTI1OTUwMDAsXCJ1c2VybmFtZVwiOlwiYWRtaW5cIn0ifQ.m7F4GtziqQ4o-4WCxHnPQcd5fpZjZRCtaBnGfpv0ut6T4BYMp2CPNqpAnMEDDh9dWP6oSpPzfRdXIPTV1lk6k8iFCro1mRNezmQiXoNF2VrWd7vTQXeFcLHApVzz0jYpMVlD3GaAMZWK_2K_KGCp88wRsI-kl5WHEpofC5-3C99O3Sg0hoWDlU0PMrDAGFHYzSmWW1DciProIEvutGn6Tc8cql2G0jE34sjnLHko6DHGaMYnn4xRAm-caIG2BcEhTTPxWh6qQKubj3q8xRCVnIYG-gkAaVigTE9E_oQ_ysQc7XLmIKJNrkVDoyTtto7SrlqIrUsJvwDSuvPGNaXUBw";
        TokenDecode tokenDecode = new TokenDecode();


        Map<String, String> stringStringMap = tokenDecode.dcodeToken(token);
        Set<Map.Entry<String, String>> entries = stringStringMap.entrySet();
        for (Map.Entry val: entries) {
            System.out.println("key: "+val.getKey()+", value: "+val.getValue());
        }

        Map<String, String> userInfo = tokenDecode.getUserInfo();
        Set<Map.Entry<String, String>> entries1 = userInfo.entrySet();
        for (Map.Entry val: entries1) {
            System.out.println("key1: "+val.getKey()+", value1: "+val.getValue());
        }

    }
    @Test
    public void testsub(){
        String str = "Bearer 123456";
        System.out.println(str.substring(7));
    }


}
