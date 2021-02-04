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
        //判断前端传递过来的pageNum是否为空
        if (pageNum == null){
            pageNum = 1;
        }
        //创建一个Pageable对象用于封装pageNUm和每页显示数据数量
        // （当前页， 每页记录数）
        Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_RECORD_NUM);
        //根据pageable对象查询当前登录用户的学生信息
        Page<Student> list = studentService.findAll(pageable);
        model.addAttribute("students", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        return "/listStudent";
    }

    /**
     * 跳转到增加学生界面
     * @return
     */
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
        //获取session域中的管理员Id
        int adminId = (int) session.getAttribute("adminId");
        //获取当前时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //创建一个学生对象，保存前端传递过来的数据
        Student student = new Student(adminId,stuName,age,address,timestamp,timestamp);
        //增加学生
        studentService.insertStudent(student);
        //获取学生Id
        String id = student.getId().toString();
        //学生Id作为key，将学生对象存储到redis中
        redisTemplate.opsForValue().set(id,student);
        Map<String,String> map = new HashMap();
        //返回给前端Ajax一个成功的数据
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
        //根据学生Id获取学生信息
        Student student = studentService.getStudent(id);
        //将学生信息存储到model域中，传递给前端
        model.addAttribute("student",student);
        //跳转到前端界面
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
        //获取session域中的管理员Id
        int adminId = (int) session.getAttribute("adminId");
        //获取当前时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //根据学生Id查询学生信息
        Student student = studentService.findBystuId(stuId);
        //修改学生相关信息
        //修改人Id
        student.setUpdateId(adminId);
        //学生姓名
        student.setName(stuName);
        //年龄
        student.setAge(age);
        //地址
        student.setAddress(address);
        //修改时间
        student.setUpdateTime(timestamp);
        studentService.updateStudent(student);
        String id = String.valueOf(stuId);
        //将学生数据存储到redis中，redis如果存在此key的数据，直接覆盖
        redisTemplate.opsForValue().set(student.getId().toString(),student);
        Map<String,String> map = new HashMap<String,String>();
        //返回给前端Ajax一个成功的数据
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
        //通过学生Id删除学生信息
        studentService.deleteStudent(studentId);
        //删除redis中对应学生数据
        redisTemplate.delete(studentId.toString());
        Map<String,String> map = new HashMap();
        //返回给前端Ajax一个成功的数据
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
        //判断前端传递过来的pageNum是否为空
        if (pageNum == null){
            pageNum = 1;
        }
        //通过学生姓名和当前页数查询学生信息
        Page<Student> list = studentService.pageQuery(name, pageNum);
        //将学生信息添加到model域中，传递给前端
        model.addAttribute("studentList", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        //跳转到查询界面
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
     * 批量添加学生信息
     * @param file
     */
    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,HttpSession session){
        try {
            //获取session域中的管理员Id
            int adminId = (int) session.getAttribute("adminId");
            //读取上传的excel文件，解析成list集合
            List<String[]> list = POIUtil.readExcel(file);
            //判断excel文件中是否有数据
            if(list !=null && list.size() >0){
                List<Student> data = new ArrayList<>();
                for (String[] strings : list) {
                    String name = strings[0];
                    Integer age = Integer.parseInt(strings[1]);
                    String address = strings[2];
                    //当前时间
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    //获取文件中对应的值，并赋值给一个新学生对象
                    Student student = new Student(adminId,name,age,address,timestamp,timestamp);
                    data.add(student);
                }
                studentService.addStudent(data);
                List<Student> studentList = studentService.listStudent();
                //遍历
                for (Student student : studentList) {
                    //存储到redis中
                    redisTemplate.opsForValue().set(student.getId().toString(),student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/listStudent";
    }
}
