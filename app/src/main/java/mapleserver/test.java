package mapleserver;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import websocketserver.LoginThread;
import websocketserver.WebSocketServer;

import java.net.Socket;
import java.lang.Runtime;

public class test {
    public static void main(String[] args) throws Exception {
        // WebSocketServer webServer1 = new WebSocketServer(MyThread::new, 9876);
        // WebSocketServer webServer2 = new WebSocketServer(MyThread::new, 9877);
        WebSocketServer loginServer1 = new WebSocketServer(LoginThread::new, 9876);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("=== start to close connections ===");
                loginServer1.close();
                // webServer1.close();
                // webServer2.close();
            }catch(Exception ex) {
                System.out.println("Error occurs");
            }
        }));
        loginServer1.start();
        loginServer1.join();
        // webServer1.start();
        // webServer2.start();
        // webServer1.join();
        // webServer2.join();
    }
}
