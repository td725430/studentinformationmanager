package com.tenton.pojo;

import javax.persistence.*;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
@Entity
@Table(name = "admin")
public class Admin {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" ,updatable = false)
    private Integer id;
    /**
     * 登录名
     */
    @Column(name = "name")
    private String name;
    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admin() {
    }

    public Admin(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
