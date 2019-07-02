package com.lifeIt.taxi;

import java.util.concurrent.BlockingQueue;


public class Client {
    private final int TIME_SEND_INTERVAL_MS = 10;
    private final int MAX_COUNT_MESSAGES = 1000;
    private final BlockingQueue<Message> dispatcherInputQueue;
    private final int maxTaxiCount;
    private Thread th;

    public Client(BlockingQueue<Message> dispatcherInputQueue, int maxTaxiCount) {
        this.dispatcherInputQueue = dispatcherInputQueue;
        this.maxTaxiCount = maxTaxiCount;
    }

    private int getRandomTargetId() {
        return (int) Math.round(Math.random() * (maxTaxiCount - 1));
    }

    public void run() {
        try {
            int messageCount = 0;
            while (messageCount < MAX_COUNT_MESSAGES) {
                Message message = new Message(getRandomTargetId());
                dispatcherInputQueue.add(message);
                System.out.println(Thread.currentThread().getName() + ": Клиентом отправлено сообщение сообщение: " + message);
                Thread.sleep(TIME_SEND_INTERVAL_MS);
                messageCount++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void join() {
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        th = new Thread(this::run);
        th.start();
    }

    public void stop() {
        th.interrupt();
    }


}
