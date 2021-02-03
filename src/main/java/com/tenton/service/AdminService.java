package com.tenton.service;

import com.tenton.pojo.Admin;

import java.util.List;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
public interface AdminService {
    /**
     * 获取所有管理员信息
     * @return
     */
    List<Admin> listAdmin();
}
