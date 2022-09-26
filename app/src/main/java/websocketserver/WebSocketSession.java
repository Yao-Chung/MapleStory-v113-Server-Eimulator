package app.src.main.java.websocketserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WebSocketSession {
    private Socket socket;
    public WebSocketSession(Socket socket) {
        this.socket = socket;
    }
    public int connect() throws Exception {
        // Receive http handshake request from client
        byte[] inBuff = new byte[2048];
        InputStream ins = socket.getInputStream();
        ins.read(inBuff);
        String recvMessage = new String(inBuff, StandardCharsets.UTF_8);
        Map<String, String> retMap = WebSocketUtil.parseHandShakeRequestHeader(recvMessage);
        String response = WebSocketUtil.buildHandShakeResponse(retMap);

        // Send the http response back to client
        OutputStream outs = socket.getOutputStream();
        byte[] outBuff = response.getBytes(StandardCharsets.UTF_8);
        outs.write(outBuff);
        
        // Close stream
        return (retMap.get("Status-Code") == "101") ? 0 : -1;
    }
    public int send(String message) throws Exception {
        return send(message.getBytes(), 1);
    }
    public int send(byte[] message) throws Exception {
        return send(message, 2);
    }
    private synchronized int send(byte[] message, int opcode) throws Exception {
        OutputStream outs = socket.getOutputStream();
        outs.write(WebSocketUtil.buildDataFrameHeader(message, opcode));
        outs.write(message);
        return 0;
    }
    public void close() throws Exception {
        OutputStream outs = socket.getOutputStream();
        outs.write(new byte[]{(byte)0x88, (byte)0x00});
        outs.close();
        socket.close();
    }
    public String recv() throws Exception {
        InputStream ins = socket.getInputStream();
        
        int[] finAndOp = WebSocketUtil.parseFinToOpcode(ins.read());
        int payloadLen = WebSocketUtil.parseToPayloadLen(ins.read());
        
        byte[] maskKey = new byte[4];
        ins.read(maskKey, 0, 4);
        
        byte[] payLoad = new byte[payloadLen];
        ins.read(payLoad, 0, payloadLen);
        
        WebSocketUtil.unmaskedPayLoad(payLoad, maskKey);
        String message = new String(payLoad, StandardCharsets.UTF_8);
        
        return message;
    }
}
