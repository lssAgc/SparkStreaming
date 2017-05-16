package com.itm.utils.beanAndJson;

import java.util.List;

/**
 * Created by gaochuang on 2017/4/12.
 */
public class ComplexUser extends SimpleUser{

    private List<String> address;

    public ComplexUser() {
    }

    public ComplexUser(int id, String name, String sex, int age, String province, List<String> address) {
        super( age, id, name,province, sex);
        this.address = address;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
        
    }


    @Override
    public String toString() {

        return "ComplexUser{" +
                "id=" + super.getId() +
                ", name=" + super.getName()  +
                ", sex=" + super.getSex()  +
                ", age=" + super.getAge() +
                ", province=" + super.getProvince()+
                ",address=" + address +
                '}';
    }
}
