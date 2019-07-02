package com.lifeIt.taxi;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Taxi {

    private static int ID_GENERATOR = 0;
    private final int SLEEP_TIME_MS = 3000;
    private final BlockingQueue<Message> queueMessage = new LinkedBlockingQueue<>();
    private final int id;
    private Thread th;

    public Taxi() {
        id = ID_GENERATOR;
        ID_GENERATOR++;
    }

    public void addMessage(Message message) {
        queueMessage.add(message);
    }


    public void run() {
        try {
            while (!Thread.interrupted()) {

                Message message = queueMessage.take();
                message.saveTofile();
                System.out.println(Thread.currentThread().getName() + ": Сообщение обработано таксистом №" + message.getTargetId() + " Текст сообщения: " + message);
                Thread.sleep(SLEEP_TIME_MS);
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+": Interrupted thread. It's ok.");
        }
    }

    public void start() {
        th = new Thread(this::run);
        th.start();
    }

    public void stop() {
        th.interrupt();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean emptyQueue() {
        if (queueMessage.isEmpty())
            return true;
        return false;

    }

    public int getId() {
        return id;
    }
}
