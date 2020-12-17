package com.bnz.miaosha.oauth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnz.miaosha.oauth.domain.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 查询用户权限
     * @param userId
     * @return
     */
    List<Permission> selectByUserId(@Param("userId") Long userId);
}