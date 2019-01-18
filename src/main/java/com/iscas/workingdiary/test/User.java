package com.iscas.workingdiary.test;

import java.io.Serializable;

public class User implements Serializable,Cloneable {
    private static final long serialVersionUID = -3544026470067157304L;
    private String name;
    private int age;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (User)super.clone();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
