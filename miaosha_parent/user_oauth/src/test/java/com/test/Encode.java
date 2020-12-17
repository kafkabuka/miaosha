package com.test;

import com.bnz.miaosha.oauth.dao.PermissionMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = OAuthApplication.class)
public class Encode {
    @Autowired
    private PermissionMapper permissionMapper;
    @Test
    public void testEncode(){
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
    @Test
    public void testEncode1(){
        System.out.println(new BCryptPasswordEncoder().matches("123456","$2a$10$9z2MYk10kS1/ewG10TEQ3.svyB22MkixGVI9sRvJdr7JXOfuGYquC"));
    }
    @Test
    public void testSalt(){
        System.out.println(BCrypt.hashpw("123456", BCrypt.gensalt()));
    }

    @Test
    public void testPermission(){
        System.out.println(permissionMapper.selectByUserId(37L));
    }
}
