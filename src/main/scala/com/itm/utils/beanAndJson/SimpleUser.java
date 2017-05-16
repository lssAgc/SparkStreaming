package com.itm.utils.beanAndJson;


/**
 * 只包含基本数据类型的简单实体类
 * @author TT_2009
 */

import java.util.List;

/**
 * Created by gaochuang on 2017/4/12.
 */
public class SimpleUser {

    private int id;

    private String name;

    private String sex;

    private int age;

    private String province;
    private List<Double> data;

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public SimpleUser() {

    }

    public SimpleUser(int age, int id, String name,  String province,String sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "SimpleUser{" +
                "\"id\":" + id +
                ",\"name\":" + name +
                ",\"sex\":" + sex +
                ",\"age\":" + age +
                ",\"province\":" + province +
                ",\"data\":" + data +
                '}';
    }


}
