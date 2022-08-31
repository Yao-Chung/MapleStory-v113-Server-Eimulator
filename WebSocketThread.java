import java.net.Socket;
import java.util.Map;

public class WebSocketThread extends WebSocketSession implements Runnable {
    private Map<Long, Thread> clientsThreadMap;
    private Long threadID;

    WebSocketThread(Socket socket, Map<Long, Thread> clientsThreadMap) {
        super(socket);
        this.clientsThreadMap = clientsThreadMap;
    }
    public void setThreadID(Long id) {
        this.threadID = id;
    }
    public void run() {
        try {

            if(connect() != 0) {
                System.out.println("WebSocket Connection failed.");
                return;
            }
            System.out.println("WebSocket Connection success.");
            while(true) {
                String msg = recv();
                if(msg.equals("close")) {
                    close();
                    break;
                }else {
                    send(msg);
                }
            }
            // client thread exit, delete the entry in map
            clientsThreadMap.remove(threadID);
            System.out.printf("Thread %d end\n", threadID);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
