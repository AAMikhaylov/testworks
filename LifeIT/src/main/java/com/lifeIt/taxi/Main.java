package com.lifeIt.taxi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private final static int CHECK_CANSTOP_INTERVAL_MS = 3000;
    private final int MAX_DISPATCHER_COUNT = 100;
    private final int MAX_TAXI_COUNT = 10;
    private final BlockingQueue<Message> dispatcherInputQueue = new LinkedBlockingQueue<>();
    private final Map<Integer, Taxi> taxis = new HashMap<>();
    private final List<Dispatcher> dispatchers = new ArrayList<>(MAX_DISPATCHER_COUNT);
    private final Client client;

    private void init() {
        for (int i = 0; i < MAX_DISPATCHER_COUNT; i++)
            dispatchers.add(new Dispatcher(dispatcherInputQueue, taxis));
        for (int i = 0; i < MAX_TAXI_COUNT; i++) {
            Taxi taxi = new Taxi();
            taxis.put(taxi.getId(), taxi);
        }
    }

    private boolean canStop() {
        if (!dispatcherInputQueue.isEmpty())
            return false;
        for (Map.Entry<Integer, Taxi> entry : taxis.entrySet()) {
            if (!entry.getValue().emptyQueue())
                return false;
        }
        return true;
    }

    private void start() {
        client.start();
        dispatchers.forEach(d -> d.start());
        taxis.forEach((k, v) -> v.start());
    }

    private void stop() {
        client.stop();
        dispatchers.forEach(d -> d.stop());
        taxis.forEach((k, v) -> v.stop());
    }

    public Main() {
        client = new Client(dispatcherInputQueue, MAX_TAXI_COUNT);
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.init();
        m.start();
        m.client.join();

        try {
            while (!m.canStop()) {
                Thread.sleep(CHECK_CANSTOP_INTERVAL_MS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();


        }
        m.stop();

    }


}
