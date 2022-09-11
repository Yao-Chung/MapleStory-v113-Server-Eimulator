import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.net.Socket;
import java.lang.Runtime;

public class test {
    public static void main(String[] args) throws Exception {
        WebSocketServer webServer1 = new WebSocketServer(MyThread::new, 9876);
        WebSocketServer webServer2 = new WebSocketServer(MyThread::new, 9877);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("=== start to close connections ===");
                webServer1.close();
                webServer2.close();
            }catch(Exception ex) {
                System.out.println("Error occurs");
            }
        }));
        webServer1.start();
        webServer2.start();
        webServer1.join();
        webServer2.join();
    }
}
