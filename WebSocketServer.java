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
import java.security.MessageDigest;
import java.util.Base64;
import java.lang.Thread;
import java.util.Map;


public class WebSocketServer {
    private ServerSocket server;
    private Map<Long, WebSocketThread> clientsThreadMap;

    public WebSocketServer(int port) throws Exception {
        server = new ServerSocket(port);
        clientsThreadMap = new ConcurrentHashMap<>();
    }
    public void listen() throws Exception {
        Socket socket = server.accept();
        WebSocketThread webSocketThread = new WebSocketThread(socket, this);
        clientsThreadMap.put(webSocketThread.getId(), webSocketThread);
        webSocketThread.start();
    }
    public void removeClientThread(Long threadID) {
        clientsThreadMap.remove(threadID);
    }
    public void notifyToAllClients(String message) throws Exception {
        for(Map.Entry<Long, WebSocketThread> entry: clientsThreadMap.entrySet()) {
            entry.getValue().send(message);
        }
    }
    public void close() throws Exception {
        server.close();
    }
}
