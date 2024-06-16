package com.lty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lty.model.dto.user.UserLoginRequest;
import com.lty.model.dto.user.UserQueryRequest;
import com.lty.model.dto.user.UserRegisterRequest;
import com.lty.model.dto.user.UserUpdateRequest;
import com.lty.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lty
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userRegisterRequest
     * @return long
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     *
     * @param userLoginRequest
     * @param request
     * @return User
     */
    User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取当前用户
     * @return com.lty.model.entity.User
     */
    User getLoginUser();

    /**
     * 是否为管理员
     * @param request
     * @return boolean
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 用户更新
     * @param userUpdateRequest
     * @return
     */
    boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 获取查询条件
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
