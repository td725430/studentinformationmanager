package com.tenton.service;

import com.tenton.pojo.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
public interface StudentService {
    /**
     * 增加学生
     * @param student
     * @return
     */
    void insertStudent(Student student);

    /**
     * 批量插入学生
     * @param students
     */
    void addStudent(List<Student> students);

    /**
     * 删除学生
     * @param id
     * @return
     */
    void deleteStudent(int id);

    /**
     * 修改学生
     * @param student
     * @return
     */
    void updateStudent(Student student);

    /**
     * 获取所有学生
     * @return
     */
    List<Student> listStudent();

    /**
     * 通过学生编码查询学生
     * @param id
     * @return
     */
    Student getStudent(int id);

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    Page<Student> findAll(Pageable pageable);

    /**
     * 模糊查询
     * @param name
     * @param pageNum
     * @return
     */
    Page<Student> pageQuery(String name, Integer pageNum);
}
