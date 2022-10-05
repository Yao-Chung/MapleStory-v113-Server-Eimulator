package handler;

import java.nio.ByteBuffer;

import websocket.WebSocketThread;

public class LoginHandler {
    public static void loginFunc(WebSocketThread thread, ByteBuffer bb) throws Exception {
        // Read the length of username
        short userLen = bb.getShort();
        byte[] username = new byte[userLen];
        bb.get(username, 0, userLen);
        
        // Read the length of password
        short passLen = bb.getShort();
        byte[] password = new byte[passLen];
        bb.get(password);

        thread.send(username);
        thread.send(password);
    }
}
