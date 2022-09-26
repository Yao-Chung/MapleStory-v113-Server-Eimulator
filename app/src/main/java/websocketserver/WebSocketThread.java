package app.src.main.java.websocketserver;

import java.net.Socket;

public abstract class WebSocketThread extends Thread {
    protected WebSocketSession session;

    protected WebSocketThread(Socket socket) {
        session = new WebSocketSession(socket);
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
