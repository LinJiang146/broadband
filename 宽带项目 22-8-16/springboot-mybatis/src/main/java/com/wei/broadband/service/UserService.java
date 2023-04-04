package com.wei.broadband.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.broadband.common.R;
import com.wei.broadband.po.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends IService<User> {
    R<User> login(HttpServletRequest request, User user);

    R<String> logout(HttpServletRequest request);

    R<String> saveUser(HttpServletRequest request, User user);

    R<List<User>> UserList(HttpServletRequest request, String content, int sort);

    R<String> updateUser(HttpServletRequest request,User user);

    R<String> enableUser(Integer id);

    R<String> updatePassword(HttpServletRequest request, User user);
}
