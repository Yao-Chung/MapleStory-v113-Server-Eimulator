package thread;

import handler.LoginHandler;
import packet.Opcode;
import websocket.WebSocketServer;
import websocket.WebSocketThread;

import java.net.Socket;
import java.nio.ByteBuffer;

public class LoginThread extends WebSocketThread{
    private WebSocketServer server;

    public LoginThread(Socket socket, WebSocketServer server) {
        super(socket);
        this.server = server;
    }
    
    private void handlePacket(Opcode opcode, ByteBuffer bb) throws Exception {
        switch (opcode) {
            case LOGIN:
                LoginHandler.loginFunc(this, bb);
                break;
            default:
                throw new IllegalArgumentException("Opcode is wrong in login handlePacket");
        }
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
                    // Receive byte array from client
                    byte[] msg = session.recv();
                    // Convert to ByteBuffer and read buffer sequentially
                    ByteBuffer bb = ByteBuffer.wrap(msg);
                    short opcode = bb.getShort();
                    handlePacket(Opcode.get(opcode), bb);
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
