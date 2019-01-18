package com.iscas.workingdiary.test;

public class NewsSubject extends AbstractSubject {
    @Override
    public void change() {
        System.out.println("我是NewsSubject,我发布了新文章");
        notifyObservers();
    }
}
