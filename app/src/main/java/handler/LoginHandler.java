package app.src.main.java.handler;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import app.src.main.java.websocketserver.WebSocketThread;

public class LoginHandler {
    public static void loginFunc(WebSocketThread thread, ByteBuffer bb) throws Exception {
        // Read the length of username
        short userLen = bb.getShort();
        byte[] username = new byte[userLen];
        bb.get(username);
        String usernameStr = new String(username, StandardCharsets.UTF_8);
        // Read the length of password
        short passLen = bb.getShort();
        byte[] password = new byte[passLen];
        bb.get(password);
        String passwordStr = new String(password, StandardCharsets.UTF_8);
        System.out.printf("Username: %s, Password: %s\n", usernameStr, passwordStr);
        thread.send(String.format("Username: %s\n", usernameStr));
        thread.send(String.format("Password: %s\n", passwordStr));
    }
}
