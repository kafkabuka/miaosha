package com.bnz.miaosha.oauth.service;

import com.bnz.miaosha.oauth.domain.Permission;

import java.util.List;

public interface PermissionService {

    /**
     * 通过用户ID获取用户权限
     * @param userId
     * @return
     */
    List<Permission> selectByUserId(Long userId);

}

