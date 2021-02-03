package com.tenton.dao;

import com.tenton.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
public interface StudentDao extends JpaRepository<Student,Integer>, JpaSpecificationExecutor<Student> {
}
