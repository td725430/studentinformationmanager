package com.tenton.dao;

import com.tenton.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
public interface AdminDao extends JpaRepository<Admin,Integer>, JpaSpecificationExecutor<Admin> {
}
