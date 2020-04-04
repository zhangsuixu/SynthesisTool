package com.common;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test public void bubbleSort() {
        int[] data = {3, 7, 4, 8, 1, 2, 9};

        System.out.println("data.length" + data.length);

        int temp;
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = 0; j < data.length - 1 - i; j++) {
                if (data[j] < data[j + 1]) {
                    temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
                printlnData(data);
            }
        }
    }

    @Test public void selectionSort(){
        int[] data = {3, 7, 4, 8, 2, 9, 1, 6, 5};

        System.out.println("data.length" + data.length);

        int temp;
        for (int i = 0; i < data.length; i++) {
            for (int j = i + 1; j < data.length; j++) {
                if (data[i] > data[j]) {
                    temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                }
            }
        }

        printlnData(data);
    }

    private void printlnData(int[] data) {
        StringBuilder sb = new StringBuilder();

        for (int number : data) {
            sb.append(number);
            sb.append(",");
        }

        System.out.println(sb.toString());
    }

    private class TestRunable implements Runnable{
        @Override
        public void run() {

        }
    }

    private BlockingQueue mQueue;
    @Test public void println() {
//        new TestThread().start();

//        new Thread(new TestRunable()).start();
        ReentrantLock aPayLock = new ReentrantLock();

        mQueue = new ArrayBlockingQueue<>(10);
        new TestThread().start();
    }

    private Lock aPayLock;
    private double[] accounts;

    private class TestThread extends Thread{
        @Override
        public void run() {
            for (int i = 0;i< 11;i++){
                try{
                    boolean result = mQueue.add(i + "--");
                }catch (IllegalStateException e){

                }
            }
        }
    }

    private class ooxx implements Comparable{
        @Override
        public int compareTo(Object o) {
            return 0;
        }
    }
}