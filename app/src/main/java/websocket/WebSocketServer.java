package websocket;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class WebSocketServer extends Thread {
    private ServerSocket server;
    private Map<Long, WebSocketThread> clientsThreadMap;
    private BiFunction<Socket, WebSocketServer, WebSocketThread> creater;

    public WebSocketServer(BiFunction<Socket, WebSocketServer, WebSocketThread> creater, int port) throws Exception {
        this.creater = creater;
        server = new ServerSocket(port, 20, InetAddress.getByName("0.0.0.0"));
        clientsThreadMap = new ConcurrentHashMap<>();
    }
    public void run() {
        try {
            while(true) {
                Socket socket = server.accept();
                System.err.println("accepted");
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
