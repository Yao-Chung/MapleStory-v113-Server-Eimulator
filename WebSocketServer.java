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
    private Map<Long, Thread> clientsThreadMap;

    public WebSocketServer(int port) throws Exception {
        server = new ServerSocket(port);
        clientsThreadMap = new ConcurrentHashMap<>();
    }
    public void listen() throws Exception {
        Socket socket = server.accept();
        WebSocketThread webSocketThread = new WebSocketThread(socket, clientsThreadMap);
        Thread client = new Thread(webSocketThread);
        webSocketThread.setThreadID(client.getId());
        clientsThreadMap.put(client.getId(), client);
        client.start();
    }
    public void close() throws Exception {
        server.close();
    }
}
