package cn.test.test;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhiwen on 2017/5/25.
 */
@Ignore
public class TomcatTest {

    @Test
    public void test() {
        getUrl();
    }

    @Test
    public void maxThread() {
        final CyclicBarrier barrier = new CyclicBarrier(512);
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 512; i++) {
            pool.execute(new Runnable() {
                             public void run() {
                                 try {
                                     barrier.await();
                                 } catch (InterruptedException e) {
                                     e.printStackTrace();
                                 } catch (BrokenBarrierException e) {
                                     e.printStackTrace();
                                 }
                                 TomcatTest.this.getUrl();
                             }
                         }
            );
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
        }
        System.out.println("完成");

    }

    public void getUrl() {
        try {
            System.out.println(Thread.currentThread() + " 开始执行时间：" + System.currentTimeMillis());
            URL url = new URL("http://192.168.99.100:8081/test/");
            URLConnection connection = url.openConnection();
            connection.connect();
            System.out.println(Thread.currentThread() + " 建立连接成功");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
