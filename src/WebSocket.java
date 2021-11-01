import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WebSocket {


    public static void WSResponse(Socket client_, Map<String,String> request_) throws IOException, NoSuchAlgorithmException {
        String swKey = request_.get("Sec-WebSocket-Key");
        String respondKey = generateResponseKey(swKey.trim());
        System.out.println("------key: "+respondKey);

        OutputStream outputStream = client_.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.print("HTTP/1.1 101 Switching Protocols\r\n");
        printWriter.print("Connection: Upgrade\r\n");
        printWriter.print("Upgrade: websocket\r\n");
        printWriter.print("Sec-WebSocket-Accept: " + respondKey.trim() +"\r\n");
        printWriter.print("\r\n");
        printWriter.flush();
        //printWriter.close();
    }

    public static String generateResponseKey(String requestKey) throws NoSuchAlgorithmException {
        String provided = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        requestKey += provided;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashed = md.digest( requestKey.getBytes() );
        String result = Base64.getEncoder().encodeToString( hashed );
        return result;
    }
}
