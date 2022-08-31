import java.net.Socket;
import java.util.Map;

public class WebSocketThread extends Thread {
    private WebSocketServer server;
    private WebSocketSession session;

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
    public void run() {
        try {
            if(session.connect() != 0) {
                System.out.println("WebSocket Connection failed.");
                return;
            }
            System.out.println("WebSocket Connection success.");
            boolean running = true;
            while(running) {
                String msg = session.recv();
                switch (msg) {
                    case "close":
                        session.close();
                        running = false;
                        break;
                    case "broadcast":
                        server.notifyToAllClients("Broadcast testing: Hello!!!");
                        break;
                    default:
                        session.send(msg);
                }
            }
            // client thread exit, delete the entry in map
            server.removeClientThread(getId());
            System.out.printf("Thread %d end\n", getId());
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
