package com.tenton.controller;

import com.tenton.pojo.Student;
import com.tenton.service.Impl.StudentServiceImpl;
import com.tenton.utils.ConstantUtil;
import com.tenton.utils.POIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description: 管理员操作学生数据
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    StudentServiceImpl studentService;
    @Autowired
    RedisTemplate redisTemplate;
    /**
     * 显示所有学生信息
     * @param model
     * @param response
     * @param pageNum
     * @return
     */
    @RequestMapping("/listStudent")
    public String listStudent(Model model, HttpServletResponse response, Integer pageNum) {
        if (pageNum == null){
            pageNum = 1;
        }
        // （当前页， 每页记录数）
        Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_RECORD_NUM);
        Page<Student> list = studentService.findAll(pageable);
        model.addAttribute("students", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        return "/listStudent";
    }
    @GetMapping("/toAddStudent")
    public String toAddStudent(){
        return "/addStudent";
    }

    /**
     * 增加学生
     * @param stuName
     * @param age
     * @param address
     * @param session
     * @return
     */
    @Transactional
    @PostMapping("/addStudent")
    @ResponseBody
    public Map<String,String> addStudent(String stuName,int age,String address, HttpSession session) {
        int adminId = (int) session.getAttribute("adminId");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Student student = new Student(adminId,stuName,age,address,timestamp,timestamp);
        studentService.insertStudent(student);
        String id = student.getId().toString();
        redisTemplate.opsForValue().set(id,student);
        Map<String,String> map = new HashMap();
        map.put("code","0");
        return map;
    }

    /**
     * 跳转到修改界面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/toUpdateStudent/{id}")
    public String toUpdateBook(@PathVariable("id") Integer id, Model model){
        Student student = studentService.getStudent(id);
        model.addAttribute("student",student);
        return "/updateStudent";
    }

    /**
     * 修改学生信息
     * @param stuId
     * @param stuName
     * @param age
     * @param address
     * @param session
     * @return
     */
    @Transactional
    @PostMapping("/updateStudent")
    @ResponseBody
    public Map<String,String> updateStudent(int stuId,String stuName,int age,String address, HttpSession session) {
        int adminId = (int) session.getAttribute("adminId");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Student student = studentService.getStudent(stuId);
        student.setUpdateId(adminId);
        student.setName(stuName);
        student.setAge(age);
        student.setAddress(address);
        student.setUpdateTime(timestamp);
        studentService.updateStudent(student);
        redisTemplate.opsForValue().set(student.getId().toString(),student);
        Map<String,String> map = new HashMap();
        map.put("code","0");
        return map;
    }

    /**
     * 删除学生信息
     * @param studentId
     * @return
     */
    @Transactional
    @GetMapping("/deleteStudent")
    @ResponseBody
    public Map<String,String>  deleteStudent(Integer studentId){
        studentService.deleteStudent(studentId);
        redisTemplate.delete(studentId.toString());
        Map<String,String> map = new HashMap();
        map.put("code","0");
        return map;
    }
    /**
     * 跳转到查询学生信息界面
     * @return
     */
    @GetMapping("/toGetStudent")
    public String toGetStudent(){
        return "/getStudentByName";
    }

    /**
     * 模糊查询
     * @param model
     * @param response
     * @param name
     * @param pageNum
     * @return
     */
    @GetMapping("/getStudent")
    public String getStudent(Model model, HttpServletResponse response,String name,Integer pageNum){
        if (pageNum == null){
            pageNum = 1;
        }
        Page<Student> list = studentService.pageQuery(name, pageNum);
        model.addAttribute("studentList", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        return "/getStudentByName";
    }

    /**
     * 跳转到查询界面
     * @return
     */
    @GetMapping("/toGetStudentByName")
    public String toGetStudentByName(){
        return "/getStudentByName";
    }
    /**
     * 跳转到批量上传界面
     * @return
     */
    @GetMapping("/toUploadExcel")
    public String toUploadExcel(){
        return "/uploadExcel";
    }
    /**
     * 批量添加图书
     * @param file
     */
    /**
     * 读取上传的excel文件，解析成list集合
     * 判断excel文件中是否有数据
     * 获取文件中对应的值，并赋值给一个新图书对象
     *
     */
    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,HttpSession session){
        try {
            int adminId = (int) session.getAttribute("adminId");
            List<String[]> list = POIUtil.readExcel(file);
            if(list !=null && list.size() >0){
                List<Student> data = new ArrayList<>();
                for (String[] strings : list) {
                    String name = strings[0];
                    Integer age = Integer.parseInt(strings[1]);
                    String address = strings[2];
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Student student = new Student(adminId,name,age,address,timestamp,timestamp);
                    data.add(student);
                }
                studentService.addStudent(data);
                List<Student> studentList = studentService.listStudent();
                for (Student student : studentList) {
                    redisTemplate.opsForValue().set(student.getId().toString(),student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/listStudent";
    }
}
