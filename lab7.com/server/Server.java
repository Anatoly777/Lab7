package com.lab7.server;

import ch.qos.logback.classic.Logger;
import com.lab7.common.Utils.ObjectSerializer;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.server.responseInterpreter.Interpreter;
import com.lab7.server.threads.RequestReaderThread;
import com.lab7.server.workersManager.WorkersManager;
import org.postgresql.gss.GSSOutputStream;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

class ServerCommandController extends Thread {
    Server server;
    boolean stopped = false;

    public ServerCommandController(Server server) {
        this.server = server;
    }



    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (!stopped && scanner.hasNext()) {

                String command = scanner.nextLine();
                if ("exit".equals(command)) {
                    this.server.stop();
                    scanner.close();
                    stopped = false;
                    Thread.currentThread().interrupt();
                } else {
                    System.out.println("Команда не распознана");
                }

            }


        } catch (IllegalStateException ex) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Server {

    private Selector selector;
    private final ByteBuffer buffer;
    private ServerSocketChannel serverSocketChannel;
    private boolean stopped = false;
    private final Interpreter interpreter;
    public final WorkersManager manager;
    public static final Logger logger = (Logger) LoggerFactory.getLogger(Server.class);
    protected ExecutorService requestExecutorPool;
    protected ForkJoinPool responseSenderPool;
    protected ExecutorService requestReaderPool;

    public Server(Interpreter interpreter) {


        this.buffer = ByteBuffer.allocate(10000);
        this.interpreter = interpreter;
        this.manager = this.interpreter.getManager();
        this.requestExecutorPool = Executors.newCachedThreadPool();
        this.responseSenderPool = ForkJoinPool.commonPool();
        this.requestReaderPool = Executors.newCachedThreadPool();

    }

    public synchronized void stop() {
        this.stopped = true;

        try {
            this.selector.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }


    public void bind(int port) throws IOException {
        this.selector = Selector.open();
        SocketAddress address = new InetSocketAddress(port);
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(address);
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        logger.info("Сервер размещён на порте {}", port);

    }

    private void createConnection() throws IOException {

        SocketChannel client = this.serverSocketChannel.accept();
        SocketAddress a = client.getRemoteAddress();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        logger.info("Установлено соединение с {}", a);


    }

    public synchronized Request readRequest(SocketChannel client) throws IOException {
        try {
            buffer.clear();
            client.read(buffer);
            Request rq;
            ByteArrayInputStream stream = new ByteArrayInputStream(buffer.array());
            ObjectInputStream os = new ObjectInputStream(stream);
            rq = (Request) os.readObject();
            buffer.flip();
            logger.info("Получен запрос от {}", client.socket().getRemoteSocketAddress());
            return rq;
        } catch (SocketException | ClassNotFoundException ex) {
            client.close();
        } catch (StreamCorruptedException ex) {

            client.close();
            buffer.clear();
        } catch (ClassCastException ex) {
            return null;
        }
        return null;

    }

    public synchronized void sendResponse(SocketChannel client, Response resp) throws IOException {
        try {

            byte[] bytes = ObjectSerializer.serialize(resp);
            ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(bytes));

            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            client.write(buffer);
            buffer.flip();
            logger.info("Ответ отправлен {}, размер: {}", client.socket().getRemoteSocketAddress(), bytes.length);

        } catch (ClosedChannelException ex) {
            client.close();

        }


    }

    public void run() {
        ServerCommandController controller = new ServerCommandController(this);
        controller.start();
        logger.info("Сервер начал работу");
        buffer.flip();

        while (!this.stopped) {

            //System.out.println("what");
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            this.createConnection();
                        }
                        if (key.isReadable()) {
                            (new RequestReaderThread(this, key, interpreter)).start();
                            Thread.sleep(1000);
                        }
                    }
                }
                it.remove();
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            } catch (ClosedSelectorException ex){
                continue;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
