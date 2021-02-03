package com.tenton.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
@Entity
@Table(name = "student")
public class Student implements Serializable {
    /**
     * 编码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" ,updatable = false)
    private Integer id;
    /**
     * 创建此学生信息的管理员Id
     */
    @Column(name = "create_id")
    private Integer createId;
    /**
     * 修改此学生信息的管理员Id
     */
    @Column(name = "update_id")
    private Integer updateId;
    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;
    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;
    /**
     * 地址
     */
    @Column(name = "address")
    private String address;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Timestamp createTime;
    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Timestamp updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Student() {
    }

    public Student(Integer createId, String name, Integer age, String address, Timestamp createTime, Timestamp updateTime) {
        this.createId = createId;
        this.name = name;
        this.age = age;
        this.address = address;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Student(Integer updateId, String name, Integer age, String address, Timestamp updateTime) {
        this.updateId = updateId;
        this.name = name;
        this.age = age;
        this.address = address;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", createId=" + createId +
                ", updateId=" + updateId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
