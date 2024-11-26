package com.chhei.mall.mallsearch.thread;

import java.util.concurrent.*;

public class ThreadPoolDemo {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5,
				100,
				10,
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(10000),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.AbortPolicy()
		);	

		poolExecutor.execute(()->{
			System.out.println("----->" + Thread.currentThread().getName());
		});
	}
}
