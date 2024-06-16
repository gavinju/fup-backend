package com.lty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lty.common.ErrorCode;
import com.lty.constant.UserConstant;
import com.lty.exception.BusinessException;
import com.lty.mapper.UserMapper;
import com.lty.model.dto.user.UserLoginRequest;
import com.lty.model.dto.user.UserQueryRequest;
import com.lty.model.dto.user.UserRegisterRequest;
import com.lty.model.dto.user.UserUpdateRequest;
import com.lty.model.entity.User;
import com.lty.service.UserService;
import com.lty.utils.PasswordUtil;
import com.lty.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lty
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < UserConstant.ACCOUNT_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < UserConstant.PASSWORD_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = PasswordUtil.encodePassword(userPassword);
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < UserConstant.ACCOUNT_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < UserConstant.PASSWORD_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = PasswordUtil.encodePassword(userPassword);
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return user;
    }

    @Override
    public User getLoginUser() {
        // 先判断是否已登录
        HttpServletRequest request = ServletUtil.getRequest();
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            return null;
        }
        return currentUser;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && UserConstant.ADMIN_ROLE.equals(user.getUserRole());
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public boolean updateUser(UserUpdateRequest userUpdateRequest) {
        User oldUser = this.getById(userUpdateRequest.getId());
        User resultUser = new User();
        BeanUtils.copyProperties(oldUser, resultUser);
        String userName = userUpdateRequest.getUserName();
        String password = userUpdateRequest.getUserPassword();
        String userAvatar = userUpdateRequest.getUserAvatar();
        Integer gender = userUpdateRequest.getGender();
        String userRole = userUpdateRequest.getUserRole();
        if (StringUtils.isNotBlank(userName)) {
            resultUser.setUserName(userName);
        }
        if (StringUtils.isNotBlank(userAvatar)) {
            resultUser.setAvatarUrl(userAvatar);
        }
        if (StringUtils.isNotBlank(password)) {
            resultUser.setUserPassword(PasswordUtil.encodePassword(password));
        }
        if (StringUtils.isNotBlank(userRole)) {
            resultUser.setUserRole(userRole);
        }
        if (gender != null && gender >= 0) {
            resultUser.setGender(gender);
        }
        boolean result = this.updateById(resultUser);
        return result;
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        qw.like(StringUtils.isNoneBlank(userAccount), "userAccount", userAccount);
        qw.like(StringUtils.isNoneBlank(userName), "userName", userName);

        qw.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals("asc"), sortField);
        return qw;
    }
}




