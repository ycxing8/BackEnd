package com.seu.monitor;

public class TestSomeFun1 {
    String threadStatus;
    public TestSomeFun1(String threadStatus)
    {
        this.threadStatus = threadStatus;
    }
    public void changeStatus(){
        threadStatus = "String";

    }
}
