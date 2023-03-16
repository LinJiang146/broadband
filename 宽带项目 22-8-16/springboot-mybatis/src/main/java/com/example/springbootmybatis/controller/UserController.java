package com.example.springbootmybatis.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springbootmybatis.common.R;
import com.example.springbootmybatis.po.CardCustomer;
import com.example.springbootmybatis.po.User;
import com.example.springbootmybatis.service.UserService;
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

    @Autowired
    private SecurityManager securityManager;

    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user){


        //1 获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        //2 封装请求数据到 token 对象中
        AuthenticationToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        //3 调用 login 方法进行登录认证
        try {
            subject.login(token);
//            return "登录成功";
//            session.setAttribute("user", token.getPrincipal().toString());
            return R.success(null);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            System.out.println("登录失败");
            return R.error("登录失败");
        }


//        //1、将页面提交的密码password进行md5加密处理
//        String password = user.getPassword();
//        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        System.out.println(password);
//        //2、根据页面提交的用户名username查询数据库
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getUsername,user.getUsername());
//        User user1 = userService.getOne(queryWrapper);
//
//        //3、如果没有查询到则返回登录失败结果
//        if(user1 == null){
//            return R.error("登录失败");
//        }
//
//        //4、密码比对，如果不一致则返回登录失败结果
//        if(!user1.getPassword().equals(password)){
//            return R.error("登录失败");
//        }
//
//        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
//        if(user1.getStatus() == 0){
//            return R.error("账号已禁用");
//        }
//
//        //6、登录成功，将员工id存入Session并返回登录成功结果
//        request.getSession().setAttribute("user",user1.getId());
//        user1.setPassword(null);
//        return R.success(user1);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody User user){
        int id = (int)request.getSession().getAttribute("user");

        int permission = userService.getById(id).getPermission();
        int permission2 = user.getPermission();
        if((permission<=2&&permission<permission2)||permission==1) {

            user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            //user.setPassword("123456");

            user.setCreateUser(id);
            user.setUpdateUser(id);
            user.setStatus(1);
            userService.save(user);

            return R.success("新增员工成功");
        }
        return R.error("权限不足");
    }
    @GetMapping("/list")
    public R<List<User>> list(HttpServletRequest request,String content, int sort){
        int id = (int)request.getSession().getAttribute("user");
        int permission = userService.getById(id).getPermission();
        if(permission<=2) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(content)) {
                queryWrapper.gt(User::getPermission,permission).like(User::getName, content).or().
                        gt(User::getPermission,permission).like(User::getUsername, content).or().
                        gt(User::getPermission,permission).like(User::getPhone, content);
            }
            if (sort == 0)
                queryWrapper.orderByDesc(User::getUpdateTime);
            if (sort == 1)
                queryWrapper.gt(User::getPermission,permission).orderByAsc(User::getUpdateTime);
            List<User> users = userService.list(queryWrapper);
            for (User user:users){
                user.setPassword(null);
            }
            return R.success(users);
        }
        return R.error("权限不足");
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
        int id = (int)request.getSession().getAttribute("user");
        int permission = userService.getById(id).getPermission();
        int permission2 = userService.getById(user.getId()).getPermission();
        System.out.println(permission);
        System.out.println(permission2);
        if((permission < user.getPermission()&&permission<permission2&&permission<=2)||permission==1) {
            user.setUpdateUser(id);
            user.setPassword(null);
            userService.updateById(user);
            return R.success("更新成功");
        }
        return R.error("权限不足");
    }
    @GetMapping("/updata")
    public R<String> updata(HttpServletRequest request,Integer id,Integer permission,Integer revenueStatus,String password){

        return R.error("权限不足");
    }

//    @DeleteMapping
//    public R<String> delete(HttpServletRequest request,int id){
//
//        int userid = (int)request.getSession().getAttribute("user");
//        int permission = userService.getById(userid).getPermission();
//        int permission2 = userService.getById(id).getPermission();
//        if(permission<permission2&&permission<=2) {
//            userService.removeById(id);
//            return R.success("删除成功");
//        }
//        return R.error("权限不足");
//    }


    @GetMapping("enableUser")
    public R<String> enableUser(Integer id){
        User user = userService.getById(id);
        user.setStatus(1- user.getStatus());
        userService.updateById(user);
        return R.success("更新成功");
    }

    @PostMapping("/password")
    public R<String> updatePassword(HttpServletRequest request,@RequestBody User user){

        int userid = (int)request.getSession().getAttribute("user");
        User user1 = userService.getById(userid);

        String password = user.getPassword();
        if(password.length()<6||password.length()>18) return R.error("密码不能小于6位或大于18位");
        user1.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));

        userService.updateById(user1);

        return R.success("修改成功");
    }

}
