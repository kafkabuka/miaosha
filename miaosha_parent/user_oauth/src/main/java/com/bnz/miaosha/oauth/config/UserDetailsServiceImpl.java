package com.bnz.miaosha.oauth.config;

import com.alibaba.fastjson.JSON;
import com.bnz.miaosha.oauth.domain.Permission;
import com.bnz.miaosha.oauth.service.PermissionService;
import com.bnz.miaosha.oauth.util.UserJwt;
import com.bnz.miaosha.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/*****
 * 自定义授权认证类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private PermissionService permissionService;

    /****
     * 自定义授权认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication==null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //秘钥
                String clientSecret = clientDetails.getClientSecret();
                //静态方式
                //return new User(username,new BCryptPasswordEncoder().encode(clientSecret), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
                //数据库查找方式
                return new User(username,clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }

        if (StringUtils.isEmpty(username)) {
            return null;
        }

        //根据用户名查询用户信息
        com.bnz.miaosha.user.pojo.User user = userFeign.findUserInfo(username);

        //创建User对象
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user != null) {
            List<Permission> permissions = permissionService.selectByUserId(user.getId());
            permissions.forEach(permission -> {
                System.out.println("user:permission == "+permission);
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority((permission.getEnname()));
                grantedAuthorities.add(grantedAuthority);
            });
        }
        //创建User对象
        UserJwt userDetails = new UserJwt(JSON.toJSONString(user),user.getPassword(),grantedAuthorities);
        userDetails.setId(user.getId().toString());
        userDetails.setName(user.getName());
        return userDetails;
    }
}
