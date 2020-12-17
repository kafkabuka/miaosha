package com.bnz.oauth.server.mapper;

import com.bnz.oauth.server.domain.TbPermission;

public interface TbPermissionDao {
    int deleteByPrimaryKey(Long id);

    int insert(TbPermission record);

    int insertSelective(TbPermission record);

    TbPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbPermission record);

    int updateByPrimaryKey(TbPermission record);
}