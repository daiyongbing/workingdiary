package com.iscas.workingdiary.test;

public class Observer2 implements Observer {
    @Override
    public void update() {
        System.out.println("Observer2接受到了推送文章");
    }
}
