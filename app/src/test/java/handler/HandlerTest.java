package handler;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import mock.MockWebSocketThread;
import packet.Opcode;

public class HandlerTest {
    @Test
    void testLoginFuncSuccess() throws Exception {
        String username = "Yao123", password = "Hello";
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.putShort((short)username.length());
        bb.put(username.getBytes());
        bb.putShort((short)password.length());
        bb.put(password.getBytes());
        bb.position(0);

        MockWebSocketThread mThread = new MockWebSocketThread();

        LoginHandler.loginFunc(mThread, bb);
        
        bb = ByteBuffer.wrap(mThread.getBytes());

        Opcode opcode = Opcode.get(bb.getShort());
        assertEquals(opcode, Opcode.USER_PASS_VALIDATION);
        byte result = bb.get();
        assertEquals(result, (byte)1);
    }
    @Test
    void testLoginFuncFail() throws Exception {
        String username = "Yao123456", password = "Hello123";
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.putShort((short)username.length());
        bb.put(username.getBytes());
        bb.putShort((short)password.length());
        bb.put(password.getBytes());
        bb.position(0);

        MockWebSocketThread mThread = new MockWebSocketThread();

        LoginHandler.loginFunc(mThread, bb);
        
        bb = ByteBuffer.wrap(mThread.getBytes());

        Opcode opcode = Opcode.get(bb.getShort());
        assertEquals(opcode, Opcode.USER_PASS_VALIDATION);
        byte result = bb.get();
        assertEquals(result, (byte)0);
    }
}
