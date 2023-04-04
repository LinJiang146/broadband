package com.wei.broadband.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.R;
import com.wei.broadband.po.User;
import com.wei.broadband.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user){



        return userService.login(request,user);

    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        return userService.logout(request);

    }

    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody User user){

        return userService.saveUser(request,user);

    }
    @GetMapping("/list")
    public R<List<User>> list(HttpServletRequest request,String content, int sort){

        return userService.UserList(request,content,sort);

    }
    @GetMapping("/allList")
    public R<List<User>> allList(){
        List<User> userList = userService.list();
        return R.success(userList);
    }
    @GetMapping("/getbyid")
    public R<User> getById( int id){
        User user = userService.getById(id);
        user.setPassword(null);
        return R.success(user);
    }
    @GetMapping("/getMyUser")
    public R<User> getMyUser(HttpServletRequest request){
        int id = (int)request.getSession().getAttribute("user");
        User user = userService.getById(id);
        user.setPassword(null);
        return R.success(user);
    }
    @PostMapping("/updata")
    public R<String> updata(HttpServletRequest request,@RequestBody User user){

        return userService.updateUser(request,user);

    }

    @GetMapping("enableUser")
    public R<String> enableUser(Integer id){
        return userService.enableUser(id);

    }

    @PostMapping("/password")
    public R<String> updatePassword(HttpServletRequest request,@RequestBody User user){
        return userService.updatePassword(request,user);
    }

}
