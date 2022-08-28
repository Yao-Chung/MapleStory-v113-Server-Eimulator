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

public class WebSocketServer {
    private ServerSocket server;

    public WebSocketServer(int port) throws Exception {
        server = new ServerSocket(port);
    }
    public Socket listen() throws Exception {
        Socket socket = server.accept();
        return socket;
    }
    public void close() throws Exception {
        server.close();
    }
}
