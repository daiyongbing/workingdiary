package com.iscas.workingdiary.test;

/**
 * 订阅主题
 */
public interface Subject {
    //添加观察者
    void add(Observer observer);
    // 删除观察者
    void delete(Observer observer);
    // 通知观察者
    void notifyObservers();

    // 自身变化
    void change();
}
