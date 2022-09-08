import java.lang.Thread;
import java.net.SocketException;
import java.net.Socket;

public class MyThread extends WebSocketThread {
    private WebSocketServer server;

    public MyThread(Socket socket, WebSocketServer server) {
        super(socket);
        this.server = server;
    }

    @Override
    public void run() {
        try {
            if(session.connect() != 0) {
                System.out.println("WebSocket Connection failed.");
                return;
            }
            System.out.println("WebSocket Connection success.");
            boolean running = true;
            while(running) {
                try {
                    String msg = session.recv();
                    switch (msg) {
                        case "close":
                            session.close();
                            running = false;
                            break;
                        case "broadcast":
                            server.broadcast("Broadcast testing: Hello!!!");
                            break;
                        default:
                            session.send(msg);
                    }
                }catch(SocketException ex) {
                    break;
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
