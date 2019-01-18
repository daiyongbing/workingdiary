package com.iscas.workingdiary.test;

public class Observer1 implements Observer {
    @Override
    public void update() {
        System.out.println("Observer1接受到了推送文章");
    }
}
