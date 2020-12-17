package com.bnz.miaosha.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnz.miaosha.entity.PageResult;
import com.bnz.miaosha.entity.Result;
import com.bnz.miaosha.entity.CodeMsg;
import com.bnz.miaosha.user.pojo.User;
import com.bnz.miaosha.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('accountant')")
    public Result findAll(){
        List<User> userList = userService.findAll();
        return Result.success(CodeMsg.OK,userList) ;
    }

    /***
     * 根据ID查询数据
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public Result findById(@PathVariable String username){
        User user = userService.findByUsername(username);
        return Result.success( CodeMsg.OK,user);
    }

    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username") String username){
        User user = userService.findByUsername(username);
        log.info("User=={}",user.toString());
        return user;
    }



    /***
     * 新增数据
     * @param user
     * @return
     */
    @PostMapping
    public Result add(@RequestBody User user){
        userService.add(user);
        return Result.success(CodeMsg.OK);
    }


    /***
     * 修改数据
     * @param user
     * @param username
     * @return
     */
    @PutMapping(value="/{username}")
    public Result update(@RequestBody User user,@PathVariable String username){
        user.setUsername(username);
        userService.update(user);
        return Result.success(CodeMsg.OK);
    }


    /***
     * 根据ID删除品牌数据
     * @param username
     * @return
     */
    @DeleteMapping(value = "/{username}" )
    public Result delete(@PathVariable String username){
        userService.delete(username);
        return Result.success(CodeMsg.OK);
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<User> list = userService.findList(searchMap);
        return Result.success(CodeMsg.OK,list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<User> pageList = userService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getRecords());
        return Result.success(CodeMsg.OK,pageResult);
    }


}
