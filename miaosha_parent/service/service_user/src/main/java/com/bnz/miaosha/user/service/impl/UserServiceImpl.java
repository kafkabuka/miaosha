package com.bnz.miaosha.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnz.miaosha.user.dao.PointLogMapper;
import com.bnz.miaosha.user.dao.UserMapper;
import com.bnz.miaosha.user.pojo.User;
import com.bnz.miaosha.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {



    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<User> findAll() {

        return baseMapper.selectList(null);
    }

    /**
     * 根据ID查询
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return  baseMapper.selectOne(queryWrapper);
    }


    /**
     * 增加
     * @param user
     */
    @Override
    public void add(User user){
        baseMapper.insert(user);
    }


    /**
     * 修改
     * @param user
     */
    @Override
    public void update(User user){
        baseMapper.updateById(user);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        baseMapper.deleteById(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<User> findList(Map<String, Object> searchMap){
        return baseMapper.selectByMap(searchMap);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<User> findPage(long page, long size){
        Page<User> userPage = new Page<User>(page,size);
        baseMapper.selectPage(userPage,null);
        return userPage;
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<User> findPage(Map<String,Object> searchMap, long page, long size){
        Page<User> userPage = new Page<User>(page,size);
        baseMapper.selectPage(userPage,createQueryWrapper(searchMap));
        return userPage;
    }

    @Autowired
    private PointLogMapper pointLogMapper;

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private QueryWrapper<User> createQueryWrapper(Map<String, Object> searchMap){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(searchMap!=null){
            // 用户名
            if(searchMap.get("username")!=null && !"".equals(searchMap.get("username"))){
                queryWrapper.eq("username",searchMap.get("username"));
           	}
            // 密码，加密存储
            if(searchMap.get("password")!=null && !"".equals(searchMap.get("password"))){
                queryWrapper.eq("password",searchMap.get("password"));
           	}
            // 注册手机号
            if(searchMap.get("phone")!=null && !"".equals(searchMap.get("phone"))){
                queryWrapper.like("phone","%"+searchMap.get("phone")+"%");
           	}
            // 注册邮箱
            if(searchMap.get("email")!=null && !"".equals(searchMap.get("email"))){
                queryWrapper.like("email","%"+searchMap.get("email")+"%");
           	}
            // 会员来源：1:PC，2：H5，3：Android，4：IOS
            if(searchMap.get("sourceType")!=null && !"".equals(searchMap.get("sourceType"))){
                queryWrapper.eq("sourceType",searchMap.get("sourceType"));
           	}
            // 昵称
            if(searchMap.get("nickName")!=null && !"".equals(searchMap.get("nickName"))){
                queryWrapper.like("nickName","%"+searchMap.get("nickName")+"%");
           	}
            // 真实姓名
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                queryWrapper.like("name","%"+searchMap.get("name")+"%");
           	}
            // 使用状态（1正常 0非正常）
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                queryWrapper.like("status",searchMap.get("status"));
           	}
            // 头像地址
            if(searchMap.get("headPic")!=null && !"".equals(searchMap.get("headPic"))){
                queryWrapper.like("headPic","%"+searchMap.get("headPic")+"%");
           	}
            // QQ号码
            if(searchMap.get("qq")!=null && !"".equals(searchMap.get("qq"))){
                queryWrapper.like("qq","%"+searchMap.get("qq")+"%");
           	}
            // 手机是否验证 （0否  1是）
            if(searchMap.get("isMobileCheck")!=null && !"".equals(searchMap.get("isMobileCheck"))){
                queryWrapper.eq("isMobileCheck",searchMap.get("isMobileCheck"));
           	}
            // 邮箱是否检测（0否  1是）
            if(searchMap.get("isEmailCheck")!=null && !"".equals(searchMap.get("isEmailCheck"))){
                queryWrapper.eq("isEmailCheck",searchMap.get("isEmailCheck"));
           	}
            // 性别，1男，0女
            if(searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))){
                queryWrapper.eq("sex",searchMap.get("sex"));
           	}

            // 会员等级
            if(searchMap.get("userLevel")!=null ){
                queryWrapper.eq("userLevel",searchMap.get("userLevel"));
            }
            // 积分
            if(searchMap.get("points")!=null ){
                queryWrapper.eq("points",searchMap.get("points"));
            }
            // 经验值
            if(searchMap.get("experienceValue")!=null ){
                queryWrapper.eq("experienceValue",searchMap.get("experienceValue"));
            }

        }
        return queryWrapper;
    }

}
