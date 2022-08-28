import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.security.MessageDigest;
import java.util.Base64;


public class socketServer {
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;

    public static Map<String, String> parseRequestHeader(String requestStr) {
        Map<String, String> retMap = new HashMap<>();
        String[] lines = requestStr.split("\r\n", 0);
        for(int i=0; i<lines.length; i++) {
            System.out.println(lines[i]);
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

    public static String buildResponse(Map<String, String> retMap) {
        String response = "";
        if(retMap.containsKey("Upgrade") && retMap.containsKey("Sec-WebSocket-Key")) {
            try {
                String secWebSocketAccept = buildSecWebSocketAccept(retMap.get("Sec-WebSocket-Key"));
                response += "HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\n";
                response += ("Sec-WebSocket-Accept: " + secWebSocketAccept + "\r\n\r\n");
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        }else{
            response += "HTTP/1.1 200 OK\r\n\r\nStill using HTTP";
        }
        return response;
    }

    public static String buildSecWebSocketAccept(String key) throws Exception{
        // System.out.printf("origin key: %s\n", key);
        key += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(key.getBytes());
        String secWebSocketAccept = Base64.getEncoder().encodeToString(messageDigest);
        return secWebSocketAccept;
    }

    public static void firstByte(int b) {
        int fin = (b & 128) >> 7;
        int rsv1 = (b & 64) >> 6;
        int rsv2 = (b & 32) >> 5;
        int rsv3 = (b & 16) >> 4;
        int opcode = (b & 15);
        System.out.printf("fin: %d\nrsv1: %d\nrsv2: %d\nrsv3: %d\nopcode: %d\n", fin, rsv1, rsv2, rsv3, opcode);
    }

    public static int secondByte(int b) {
        int mask = (b & 128) >> 7;
        int payLoadLength = (b & 127);
        System.out.printf("mask: %d\npayLoadLength: %d\n", mask, payLoadLength);
        return payLoadLength;
    }

    public static void unmaskedPayLoad(byte[] payLoad, byte[] maskKey) {
        for(int i=0; i<payLoad.length; i++) {
            payLoad[i] ^= maskKey[i%4]; 
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        Boolean finished = false;
        while(!finished){
            System.out.println("======== Waiting for the client request ========");
            
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            
            //read from socket to ObjectInputStream object
            byte[] inBuff = new byte[1024];
            InputStream ins = socket.getInputStream();
            // ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //convert ObjectInputStream object to String
            // String message = (String) ois.readObject();
            ins.read(inBuff);
            String recvMessage = new String(inBuff, StandardCharsets.UTF_8);
            // System.out.println("Message Received: " + recvMessage);
            Map<String, String> retMap = parseRequestHeader(recvMessage);
            String response = buildResponse(retMap);
            
            //create ObjectOutputStream object
            // ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            OutputStream outs = socket.getOutputStream();

            //write object to Socket
            byte[] outBuff = response.getBytes(StandardCharsets.UTF_8);
            outs.write(outBuff);
            
            while(true) {
                // int readLength = ins.read(inBuff);
                // System.out.printf("Read %d bytes from socket\n", readLength);
                firstByte(ins.read());
                int payLoadLength = secondByte(ins.read());
                byte maskKey[] = new byte[4];
                ins.read(maskKey, 0, 4);
                byte[] buff = new byte[payLoadLength];
                ins.read(buff, 0, payLoadLength);
                unmaskedPayLoad(buff, maskKey);
                String payLoad = new String(buff, StandardCharsets.UTF_8);
                System.out.printf("PayLoad: %s\n", payLoad);
                if(payLoad.equalsIgnoreCase("exit")) {
                    finished = true;
                    break;
                }
            }
            //close resources
            ins.close();
            outs.close();
            socket.close();
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
    }
}
