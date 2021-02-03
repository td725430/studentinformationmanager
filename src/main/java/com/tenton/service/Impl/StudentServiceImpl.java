package com.tenton.service.Impl;

import com.mysql.cj.util.StringUtils;
import com.tenton.dao.StudentDao;
import com.tenton.pojo.Student;
import com.tenton.service.StudentService;
import com.tenton.utils.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;
    /**
     * 增加学生
     * @param student
     * @return
     */
    @Override
    public void insertStudent(Student student) {
        studentDao.save(student);
    }
    /**
     * 批量插入学生
     * @param students
     */
    @Override
    public void addStudent(List<Student> students) {
        studentDao.saveAll(students);
    }
    /**
     * 删除学生
     * @param id
     * @return
     */
    @Override
    public void deleteStudent(int id) {
        studentDao.deleteById(id);
    }
    /**
     * 修改学生
     * @param student
     * @return
     */
    @Override
    public void updateStudent(Student student) {
        studentDao.save(student);
    }
    /**
     * 获取所有学生
     * @return
     */
    @Override
    public List<Student> listStudent() {
        return studentDao.findAll();
    }
    /**
     * 通过学生编码查询学生
     * @param id
     * @return
     */
    @Override
    public Student getStudent(int id) {
        return studentDao.getOne(id);
    }
    /**
     * 分页查询
     * @param pageable
     * @return
     */
    @Override
    public Page<Student> findAll(Pageable pageable) {
        return studentDao.findAll(pageable);
    }
    /**
     * 模糊查询
     * @param name
     * @param pageNum
     * @return
     */
    @Override
    public Page<Student> pageQuery(String name, Integer pageNum) {
        Specification<Student> specification = new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = null;
                //判断你是否输入了查询条件，如果有条件，就通过接口拼装sql进行模糊查询
                if (!StringUtils.isNullOrEmpty(name)){
                    predicate = criteriaBuilder.like(root.get("name").as(String.class),"%" + name + "%");
                }
                return predicate;
            }
        };
        //当前页，分页条数
        Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_RECORD_NUM);
        Page<Student> list = studentDao.findAll(specification,pageable);
        return list;
    }
}
