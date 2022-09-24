package websocketserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.security.MessageDigest;
import java.util.Base64;
import java.lang.Thread;
import java.util.Map;
import java.net.SocketException;

public class WebSocketServer extends Thread {
    private ServerSocket server;
    private Map<Long, WebSocketThread> clientsThreadMap;
    private BiFunction<Socket, WebSocketServer, WebSocketThread> creater;

    public WebSocketServer(BiFunction<Socket, WebSocketServer, WebSocketThread> creater, int port) throws Exception {
        this.creater = creater;
        server = new ServerSocket(port);
        clientsThreadMap = new ConcurrentHashMap<>();
    }
    public void run() {
        try {
            while(true) {
                Socket socket = server.accept();
                WebSocketThread webSocketThread = creater.apply(socket, this);
                clientsThreadMap.put(webSocketThread.getId(), webSocketThread);
                webSocketThread.start();
            }
        }catch(SocketException ex) {
            // Ignore
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public void removeClientThread(Long threadID) {
        clientsThreadMap.remove(threadID);
    }
    public void broadcast(String message) throws Exception {
        for(Map.Entry<Long, WebSocketThread> entry: clientsThreadMap.entrySet()) {
            entry.getValue().send(message);
        }
    }
    public void close() throws Exception {
        server.close();
        WebSocketThread[] threads = new WebSocketThread[clientsThreadMap.size()];
        int i = 0;
        // Close all client thread
        for(Map.Entry<Long, WebSocketThread> entry: clientsThreadMap.entrySet()) {
            threads[i++] = entry.getValue();
            entry.getValue().close();
        }
        for(WebSocketThread entry: threads) {
            entry.join();
        }
        System.out.println("Closed all clients");
    }
}
