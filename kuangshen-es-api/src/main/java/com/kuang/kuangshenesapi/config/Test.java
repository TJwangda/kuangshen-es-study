package com.kuang.kuangshenesapi.config;


public class Test {

      class MyThread extends Thread {
        public void run() {
            System.out.println("MyThread.run()");
        }
      }

    MyThread myThread1 = new Test.MyThread();
    MyThread myThread2 = new Test.MyThread();



}
