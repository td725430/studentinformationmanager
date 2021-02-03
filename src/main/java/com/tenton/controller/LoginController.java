package com.tenton.controller;

import com.tenton.pojo.Admin;
import com.tenton.service.Impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description: 登录
 */
@Controller
public class LoginController {
    @Autowired
    AdminServiceImpl adminServiceImpl;
    @RequestMapping("/login")
    public String login(@RequestParam("name") String name,
                        @RequestParam("password") String password,
                        Model model, HttpSession session){
        List<Admin> admins = adminServiceImpl.listAdmin();
        //判断用户名和密码是否正确
        boolean flag = false;
        for (Admin admin : admins) {
            if (admin.getName().equals(name) && admin.getPassword().equals(password)){
                session.setAttribute("loginAdmin",name);
                session.setAttribute("adminId",admin.getId());
                flag = true;
                break;
            }
        }
        if (flag){
            return "redirect:/admin/listStudent";
        } else {
            //告诉用户，你登陆失败了
            model.addAttribute("msg", "用户名或密码错误！");
            return "/index";
        }
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        //使session域无效化
        session.invalidate();
        return "/index";
    }
}
