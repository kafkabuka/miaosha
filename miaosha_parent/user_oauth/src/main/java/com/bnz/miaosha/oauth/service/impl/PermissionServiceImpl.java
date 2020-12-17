package com.bnz.miaosha.oauth.service.impl;

import com.bnz.miaosha.oauth.domain.Permission;
import com.bnz.miaosha.oauth.dao.PermissionMapper;
import com.bnz.miaosha.oauth.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;



    //通过用户ID，查询用户权限
    @Override
    public List<Permission> selectByUserId(Long userId) {
        return permissionMapper.selectByUserId(userId);
    }
}


