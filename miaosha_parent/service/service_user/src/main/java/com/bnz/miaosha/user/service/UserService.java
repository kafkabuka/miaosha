package com.bnz.miaosha.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnz.miaosha.user.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService{

    /***
     * 查询所有
     * @return
     */
    List<User> findAll();

    /**
     * 根据ID查询
     * @param username
     * @return
     */
    User findByUsername(String username);

    /***
     * 新增
     * @param user
     */
    void add(User user);

    /***
     * 修改
     * @param user
     */
    void update(User user);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<User> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<User> findPage(long page, long size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<User> findPage(Map<String,Object> searchMap, long page, long size);


}
