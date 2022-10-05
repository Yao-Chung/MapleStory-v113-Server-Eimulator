package mock;

import java.io.ByteArrayOutputStream;

import websocket.WebSocketThread;

public class MockWebSocketThread extends WebSocketThread {

    private ByteArrayOutputStream out;

    public MockWebSocketThread() {
        out = new ByteArrayOutputStream();
    }

    public byte[] getBytes() {
        return out.toByteArray();
    }

    @Override
    public int send(String message) throws Exception {
        out.write(message.getBytes());
        return 0;
    }

    @Override
    public int send(byte[] message) throws Exception {
        out.write(message);
        return 0;
    }

    @Override
    public void run() {}
}
