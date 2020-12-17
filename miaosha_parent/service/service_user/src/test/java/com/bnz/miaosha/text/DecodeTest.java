package com.bnz.miaosha.text;

import com.bnz.miaosha.UserApplication;
import com.bnz.miaosha.user.config.TokenDecode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApplication.class})
public class DecodeTest {
    @Test
    public void endecode(){
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTYwMTQzMzk5OCwiYXV0aG9yaXRpZXMiOlsiU3lzdGVtQ29udGVudERlbGV0ZSIsIlN5c3RlbVVzZXJWaWV3IiwiU3lzdGVtVXNlciIsIlN5c3RlbVVzZXJJbnNlcnQiLCJTeXN0ZW1Vc2VyRGVsZXRlIiwiU3lzdGVtVXNlclVwZGF0ZSIsIlN5c3RlbUNvbnRlbnRJbnNlcnQiLCJTeXN0ZW1Db250ZW50VXBkYXRlIiwiU3lzdGVtQ29udGVudFZpZXciLCJzZWNraWxsb3JkZXIiLCJTeXN0ZW0iLCJTeXN0ZW1Db250ZW50Il0sImp0aSI6IjhkZTgwOTM5LThlM2QtNDZlYi1hYTBjLTdkMjI3NDBlMjA3ZSIsImNsaWVudF9pZCI6Im1pYW9zaGEiLCJ1c2VybmFtZSI6IntcImNyZWF0ZWRcIjoxNjAxMTkyNTk1MDAwLFwiaWRcIjozNyxcImlzRW1haWxDaGVja1wiOlwiMFwiLFwiaXNNb2JpbGVDaGVja1wiOlwiMFwiLFwicGFzc3dvcmRcIjpcIiQyYSQxMCQ5ejJNWWsxMGtTMS9ld0cxMFRFUTMuc3Z5QjIyTWtpeEdWSTlzUnZKZHI3SlhPZnVHWXF1Q1wiLFwicGhvbmVcIjpcIjEzNTEyMzQ1Njc4XCIsXCJzZXhcIjpcIjBcIixcInN0YXR1c1wiOlwiMVwiLFwidXBkYXRlZFwiOjE2MDExOTI1OTUwMDAsXCJ1c2VybmFtZVwiOlwiYWRtaW5cIn0ifQ.U99Vu3jg0kuc57XUrqRtlTxTP2rJwUK2_eVkNSro1VHt3lo1Ko_9JWZen2XLmnVOpOlCblEzvusw4MiC_eCC9j83SKVOz93I8b9_Le74ZrikZSN8b9vZSblESNeEd7Jxb2lKB1XXlJu1lgdH8z3ppKdz96u-XjErI06OewfMNn9nVEwLjV3QuARgpLyiuY68oHRiDhoyylRTdGlIayP5GAuTU8T6JnKedzx_MroPX1mrYjsk-9jwBJV-zhxnpp4hiyR03rrGlWHqujRzu3C917fHFK_AU5t13LP1Jcw_Kiwwi2fpcfaYX8ftU7cGTF5DdBPECz0wzwnMC6uPTNHoQw";
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
