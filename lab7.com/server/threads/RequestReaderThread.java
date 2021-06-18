package com.lab7.server.threads;

import ch.qos.logback.classic.Logger;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.server.Server;
import com.lab7.server.responseInterpreter.Interpreter;
import com.sun.jmx.remote.internal.ServerCommunicatorAdmin;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

public class RequestReaderThread extends Thread{
    public static final Logger logger = (Logger) LoggerFactory.getLogger(Server.class);
    protected ExecutorService requestExecutorPool;
    private final Interpreter interpreter;
    private Server server;
    private SocketChannel client;
    private Request request;
    private Response response;


    public RequestReaderThread(Server server, SelectionKey key, Interpreter interpreter){
        this.requestExecutorPool = Executors.newCachedThreadPool();
        this.interpreter = interpreter;
        this.client = (SocketChannel) key.channel();;
        this.server = server;
        this.request = null;
        this.response = null;
    }

    @Override
    public void run(){
        System.out.println("Thread1 started");
        try {
            request = server.readRequest(client);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            yield();
        }
        if (request != null) {
            Callable<Response> executeResponseTask = () -> {
                Interpreter.RequestExecutor resp = interpreter.buildExecutor(request);
                return resp.compute();
            };
            Future<Response> responseFuture = requestExecutorPool.submit(executeResponseTask);
            while (!responseFuture.isDone()) {
                try {
                    response = responseFuture.get();
                } catch (InterruptedException | ExecutionException ex) {
                    logger.error(ex.getMessage());
                    break;
                }
            }
            if (response != null){
                try {
                    server.sendResponse(client, response);
                    yield();
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                    logger.error("Не удалось отправить ответ {}", client.socket().getRemoteSocketAddress());
                    yield();
                }
            } else {
                try {
                    client.close();
                    logger.info("Соединение разорвано {}", client.socket().getRemoteSocketAddress());
                    yield();
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                    yield();
                }
            }

        } else {
            try {
                client.close();
                logger.info("Соединение разорвано {}", client.socket().getRemoteSocketAddress());
                yield();
            } catch (IOException e) {
                e.printStackTrace();
                yield();
            }
        }
    }
}
