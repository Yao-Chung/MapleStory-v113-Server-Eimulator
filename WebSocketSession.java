import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

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
        System.out.println(recvMessage);
        Map<String, String> retMap = WebSocketUtil.parseHandShakeRequestHeader(recvMessage);
        String response = WebSocketUtil.buildHandShakeResponse(retMap);

        // Send the http response back to client
        OutputStream outs = socket.getOutputStream();
        byte[] outBuff = response.getBytes(StandardCharsets.UTF_8);
        outs.write(outBuff);
        
        // Close stream
        return (retMap.get("Status-Code") == "101") ? 0 : -1;
    }
    public int send(String message) {
        // TODO: build the data frame
        return 0;
    }
    public void close() throws Exception {
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
