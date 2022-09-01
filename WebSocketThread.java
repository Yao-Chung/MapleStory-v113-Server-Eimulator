import java.net.Socket;
import java.util.Map;
import java.net.SocketException;

public abstract class WebSocketThread extends Thread {
    protected WebSocketServer server;
    protected WebSocketSession session;

    WebSocketThread(Socket socket, WebSocketServer server) {
        session = new WebSocketSession(socket);
        this.server = server;
    }
    public int send(String message) throws Exception {
        return session.send(message);
    }
    public int send(byte[] message) throws Exception {
        return session.send(message);
    }
    public void close() throws Exception {
        session.close();
    }
    public abstract void run();
}
