package com.wei.broadband.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.po.User;
import com.wei.broadband.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    //自定义授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("进入自定义授权方法");
        //获取当前用户身份信息
        String username = principalCollection.getPrimaryPrincipal().toString();
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        //调用接口方法获取用户的角色信息
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole());
        log.info("当前用户角色信息：" + roles);

//        List<String> permissions = userService.getUserPermissionInfo(roles);
//        log.info("当前用户权限信息："+permissions);

        //创建对象，存储当前登录的用户的权限和角色
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //存储角色
        info.addRoles(roles);
//        info.addStringPermissions(permissions);
        //返回
        return info;
    }

    //自定义登录认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {



        //1 获取用户身份信息
        String username = token.getPrincipal().toString();
        //2 调用业务层获取用户信息（数据库中）
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        //3 判断并将数据完成封装
        if (user != null) {
            AuthenticationInfo info = new SimpleAuthenticationInfo(
                    token.getPrincipal(),
                    user.getPassword(),
                    token.getPrincipal().toString()
            );
            return info;
        }
        return null;
    }
}