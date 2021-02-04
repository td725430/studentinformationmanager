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
    /**
     * 根据学生Id查询出对应学生实体信息
     * @param id
     * @return
     */
    @Query(value = "select * from Student where id = ?1",nativeQuery = true)
    Student findBystuId(Integer id);
}
