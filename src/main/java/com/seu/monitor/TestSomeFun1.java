package com.seu.monitor;

public class TestSomeFun1 {
    private Test test;
    public TestSomeFun1(Test test)
    {
        this.test = test;
    }
    public void changeStatus(){
        System.out.println(test);
        test.setStatus(true);
        System.out.println(test);

    }
}
