package com.lty.aop;

import com.lty.common.ErrorCode;
import com.lty.constant.FileProperties;
import com.lty.exception.BusinessException;
import com.lty.utils.IpInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * IP黑名单拦截器
 * @author lty
 */
@Component
public class IpBlacklistInterceptor implements HandlerInterceptor {

    @Resource
    private FileProperties fileProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        String ip = IpInfoUtil.getIpAddr(request);

        if (fileProperties.getBlocked().contains(ip)) {
            // 如果访问文件地址
            if(request.getRequestURI().contains("/api/file")){
                throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "IP访问被拒绝");
                //response.sendError(HttpServletResponse.SC_FORBIDDEN);
                //return false;
            }
        }
        return true;
    }
}
