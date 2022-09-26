package app.src.main.java.websocketserver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LoginThread extends WebSocketThread{
    private WebSocketServer server;

    public LoginThread(Socket socket, WebSocketServer server) {
        super(socket);
        this.server = server;
    }
    private List<String> parseUsernameAndPassword(String msg) {
        List<String> usrAndPass = new ArrayList<>();
        int flagPos = msg.indexOf("@");
        if(flagPos >= 0) {
            usrAndPass.add(msg.substring(0, flagPos));
            usrAndPass.add(msg.substring(flagPos+1, msg.length()));
        }
        return usrAndPass;
    }
    @Override
    public void run() {
        try {
            if(session.connect() != 0) {
                System.out.println("WebSocket Connection failed.");
                return;
            }
            System.out.println("===== Login server connection success =====");
            boolean running = true;
            while(running) {
                try {
                    String msg = session.recv();
                    List<String> usrAndPass = parseUsernameAndPassword(msg);
                    if(usrAndPass.size() != 2) {
                        System.out.println("===== Wrong username and password =====");
                        continue;
                    }
                    String returnMsg = "Username: " + usrAndPass.get(0) + "\nPassword: " + usrAndPass.get(1);
                    session.send(returnMsg);
                }catch(Exception ex) {
                    break;
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            server.removeClientThread(getId());
        }
    }
}
