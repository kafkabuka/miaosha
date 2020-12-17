package com.bnz.miaosha.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnz.miaosha.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User>{

    @Update("update tb_user set points=points+#{point} where username=#{username}")
    int updateUserPoint(@Param("username") String username, @Param("point") int point);
}
