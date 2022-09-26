package app.src.main.java.websocketserver;

import java.util.Base64;
import java.util.Map;
import java.security.MessageDigest;
import java.util.HashMap;

public class WebSocketUtil {
    private static String buildSecWebSocketAccept(String key) throws Exception{
        key += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(key.getBytes());
        String secWebSocketAccept = Base64.getEncoder().encodeToString(messageDigest);
        return secWebSocketAccept;
    }
    public static void unmaskedPayLoad(byte[] payLoad, byte[] maskKey) {
        for(int i=0; i<payLoad.length; i++) {
            payLoad[i] ^= maskKey[i%4]; 
        }
    }
    public static int[] parseFinToOpcode(int b) throws Exception{
        int fin = (b & 128) >> 7;
        int rsv1 = (b & 64) >> 6;
        int rsv2 = (b & 32) >> 5;
        int rsv3 = (b & 16) >> 4;
        int opcode = (b & 15);
        if(rsv1 != 0 || rsv2 != 0 || rsv3 != 0) {
            throw new Exception("Error: RSV not equal to 0");
        }
        if(opcode != 0 && opcode != 1 && opcode != 2 && opcode != 8) {
            throw new Exception("Error: Opcode not support");
        } 
        return new int[]{fin, opcode};
        // System.out.printf("fin: %d\nrsv1: %d\nrsv2: %d\nrsv3: %d\nopcode: %d\n", fin, rsv1, rsv2, rsv3, opcode);
    }
    public static int parseToPayloadLen(int b) throws Exception{
        int mask = (b & 128) >> 7;
        int payLoadLength = (b & 127);
        if(mask != 1) {
            throw new Exception("Error: Mask should be 1");
        }
        return payLoadLength;
    }
    public static Map<String, String> parseHandShakeRequestHeader(String requestStr) {
        Map<String, String> retMap = new HashMap<>();
        String[] lines = requestStr.split("\r\n", 0);
        for(int i=0; i<lines.length; i++) {
            if(i == 0) {
                String[] startLine = lines[i].split(" ", 0);
                retMap.put("Method", startLine[0]);
                retMap.put("URI", startLine[1]);
            }else if(lines[i].indexOf(": ") >= 0){
                String[] header = lines[i].split(": ", 2);
                retMap.put(header[0], header[1]);
            }
        }
        return retMap;
    }
    public static String buildHandShakeResponse(Map<String, String> retMap) {
        String response = "";
        if(
            retMap.containsKey("Upgrade") && 
            retMap.get("Upgrade").equals("websocket") &&
            retMap.containsKey("Sec-WebSocket-Key") &&
            retMap.get("Sec-WebSocket-Key").length() == 24 &&
            retMap.get("Sec-WebSocket-Key").substring(22).equals("==")
        ) {
            try {
                String secWebSocketAccept = buildSecWebSocketAccept(retMap.get("Sec-WebSocket-Key"));
                response += "HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\n";
                response += ("Sec-WebSocket-Accept: " + secWebSocketAccept + "\r\n\r\n");
                retMap.put("Status-Code", "101");
            }catch(Exception ex) {
                ex.printStackTrace();
                response += "HTTP/1.1 500 Internal Server Error\r\n\r\nHandshake failure";
                retMap.put("Status-Code", "500");
            }
        }else{
            response += "HTTP/1.1 400 Bad Request\r\n\r\nHandshake failure";
            retMap.put("Status-Code", "400");
        }
        return response;
    }
    public static byte[] buildDataFrameHeader(byte[] message, int opcode) throws Exception {
        if(message.length > 125) {
            throw new Exception("Unsupported payload length");
        }
        byte[] dataFrame = new byte[]{(byte)(0x80 | opcode), (byte)message.length};
        return dataFrame;
    }
}
