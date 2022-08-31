import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.net.Socket;

public class test {
    public static void main(String[] args) throws Exception {
        WebSocketServer webServer = new WebSocketServer(9876);
        while(true) {
            webServer.listen();
        }
        // Socket socket = webServer.listen();
        // System.out.println("get the socket of server");
        // WebSocketSession webSession = new WebSocketSession(socket);
        // if(webSession.connect() != 0) {
        //     System.out.println("WebSocket Connection failed.");
        //     return;
        // }
        // System.out.println("WebSocket Connection success.");
        // while(true) {
        //     String msg = webSession.recv();
        //     if(msg.equals("close")) {
        //         webSession.close();
        //         break;
        //     }else {
        //         webSession.send(msg);
        //     }
        // }
    }
}
