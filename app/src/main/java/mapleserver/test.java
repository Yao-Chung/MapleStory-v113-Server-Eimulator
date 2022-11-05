package mapleserver;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import thread.LoginThread;
import websocket.WebSocketServer;

public class test {
    public static void main(String[] args) throws Exception {
        // Creating a Mongo client
        // String uri = "mongodb://root:example@localhost:27017/?maxPoolSize=20&w=majority";
        // MongoClient mongoClient = MongoClients.create(uri);
        // MongoDatabase database = mongoClient.getDatabase("admin");
        // System.out.println(database);
        // mongoClient.close();
        WebSocketServer loginServer = new WebSocketServer(LoginThread::new, 8484);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("=== start to close connections ===");
                loginServer.close();
                // webServer1.close();
                // webServer2.close();
            }catch(Exception ex) {
                System.out.println("Error occurs");
            }
        }));
        loginServer.start();
        loginServer.join();
        // ByteBuffer bb = ByteBuffer.allocate(1024);
        // bb.putShort((short)6);
        // bb.put("Yao123".getBytes());
        // bb.position(0);
        // short strLen = bb.getShort();
        // System.out.println(strLen);
        // byte[] strArr = new byte[strLen];
        // bb.get(strArr);
        // System.out.println(new String(strArr, StandardCharsets.UTF_8));
        // WebSocketServer webServer1 = new WebSocketServer(MyThread::new, 9876);
        // WebSocketServer webServer2 = new WebSocketServer(MyThread::new, 9877);
        // WebSocketServer loginServer1 = new WebSocketServer(LoginThread::new, 9876);

        // loginServer1.start();
        // loginServer1.join();
        // webServer1.start();
        // webServer2.start();
        // webServer1.join();
        // webServer2.join();
    }
}
