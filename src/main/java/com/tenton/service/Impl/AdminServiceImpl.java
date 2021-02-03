package com.tenton.service.Impl;

import com.tenton.dao.AdminDao;
import com.tenton.pojo.Admin;
import com.tenton.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminDao adminDao;

    /**
     * 获取所有管理员信息
     * @return
     */
    @Override
    public List<Admin> listAdmin() {
        return adminDao.findAll();
    }
}
