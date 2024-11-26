package com.chhei.mall.mallsearch.thread;

public class ThreadDemo {
	public static void main(String[] args) {
		System.out.println("main 函数开始");
		ThreadDemo01 t1 = new ThreadDemo01();
		t1.start();

		ThreadDemo02 t2 = new ThreadDemo02();
		new Thread(t2).start();
		new Thread(()-> {
			System.out.println("当前线程：" + Thread.currentThread().getName());
		}).start();

		System.out.println("main 函数结束");
	}
}

class ThreadDemo01 extends Thread{
	@Override
	public void run() {
		System.out.println("当前线程："+Thread.currentThread().getName());
	}
}

class ThreadDemo02 implements Runnable{
	@Override
	public void run() {
		System.out.println("当前线程："+Thread.currentThread().getName());
	}
}