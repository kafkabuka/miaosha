package com.bnz.oauth.server.mapper;

import com.bnz.oauth.server.domain.TbRole;

public interface TbRoleDao {
    int deleteByPrimaryKey(Long id);

    int insert(TbRole record);

    int insertSelective(TbRole record);

    TbRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbRole record);

    int updateByPrimaryKey(TbRole record);
}