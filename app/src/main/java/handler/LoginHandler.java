package handler;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import packet.Opcode;
import websocket.WebSocketThread;

public class LoginHandler {
    //TODO: generate token

    // Small table to memorize validated Users
    private static ConcurrentHashMap<String, Integer> validatedUsers = new ConcurrentHashMap<>();

    private static boolean validateUserAndPass(String username, String password) {
        if(username.equals("Yao123") && password.equals("Hello"))
            return true;
        else
            return false;
    }
    public static void loginFunc(WebSocketThread thread, ByteBuffer bb) throws Exception {
        // Read the length of username
        short userLen = bb.getShort();
        byte[] username = new byte[userLen];
        bb.get(username, 0, userLen);
        
        // Read the length of password
        short passLen = bb.getShort();
        byte[] password = new byte[passLen];
        bb.get(password);

        //TODO: verify username and password
        boolean result = validateUserAndPass(new String(username, StandardCharsets.UTF_8), new String(password, StandardCharsets.UTF_8));
        
        //TODO: if true => opcode + status + uid. else => opcode + status

        // Send result back to user
        ByteBuffer sendBack = ByteBuffer.allocate(3);
        sendBack.putShort(Opcode.USER_PASS_VALIDATION.value());
        sendBack.put(result ? (byte)1 : (byte)0);        
        thread.send(sendBack.array());
    }
    //TODO: CreateChar

    //TODO: DeleteChar

    //TODO: List characters to client

    //TODO: SelectChar: Received selected character and enter game

    //TODO: CheckCharName (prevent used name)

}
