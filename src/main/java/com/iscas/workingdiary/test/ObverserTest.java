package com.iscas.workingdiary.test;

public class ObverserTest {
    public static void main(String[] args){
        Subject subject = new NewsSubject();
        subject.add(new Observer1());
        subject.add(new Observer2());
        subject.change();
    }
}
