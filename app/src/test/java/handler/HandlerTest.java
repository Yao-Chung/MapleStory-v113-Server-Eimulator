package handler;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import mock.MockWebSocketThread;

public class HandlerTest {
    
    @Test
    void testLoginFunc() throws Exception {
        String username = "Yao123", password = "Hello";
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.putShort((short)username.length());
        bb.put(username.getBytes());
        bb.putShort((short)password.length());
        bb.put(password.getBytes());
        bb.position(0);

        MockWebSocketThread mThread = new MockWebSocketThread();

        LoginHandler.loginFunc(mThread, bb);
        
        byte[] result = mThread.getBytes();
        
        assertArrayEquals(result, "Yao123Hello".getBytes());
    }
}
