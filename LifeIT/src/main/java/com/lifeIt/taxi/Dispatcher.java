package com.lifeIt.taxi;


import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Dispatcher {
    private static int ID_GENERATOR = 1;
    private final int id;
    private final BlockingQueue<Message> dispatcherInputQueue;
    private final Map<Integer, Taxi> taxis;
    private Thread th;

    public Dispatcher(BlockingQueue<Message> dispatcherInputQueue, Map<Integer, Taxi> taxis) {
        this.dispatcherInputQueue = dispatcherInputQueue;
        this.taxis = taxis;
        id = ID_GENERATOR;
        ID_GENERATOR++;
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

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Message message = dispatcherInputQueue.take();
                message.addDispatcherId(this.id);
                Taxi taxi = taxis.get(message.getTargetId());
                if (taxi != null) {
                    taxi.addMessage(message);
                    System.out.println(Thread.currentThread().getName() + ": Сообщение отправлено диспетчером таксисту №" + message.getTargetId() + " Текст сообщения: " + message);
                }
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+": Interrupted thread. It's ok.");
        }
    }
}
