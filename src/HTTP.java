//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;

public class HTTP {

//    public static String Request(Socket client_) throws IOException {
//        System.out.println("Into request!!!!");
//
//        InputStream userRequest = client_.getInputStream();
//        Scanner scanRequest = new Scanner(userRequest);
//        String content = scanRequest.next();
//        content = scanRequest.next();
//        System.out.println("content: is empty" );
//        return content;
//    }

    public static void Response(Socket client_, String request_) throws IOException {
        File file = null;
        try {
            if (request_.equals("/")) {
                file = new File("/Users/max/Desktop/cs6010/chaochinYang/CS6011/Day17/WebSocketEchoes/Resources/index.html");
            }
            else{
                file = new File("/Users/max/Desktop/cs6010/chaochinYang/CS6011/Day17/WebSocketEchoes/Resources"+request_);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        OutputStream outputStream = client_.getOutputStream();
//      BufferedOutputStream bos = new BufferedOutputStream(outputStream);


        FileReader filereader = new FileReader(file);
        BufferedReader reader = new BufferedReader(filereader);

        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println("HTTP/1.1 200 OK");
        if(file.getName().endsWith(".js")){
            printWriter.println("Content-Type: text/js");
        }
        if(file.getName().endsWith(".html")){
            printWriter.println("Content-Type: text/html");
        }

        printWriter.println("Content-Length: " + file.length());
        printWriter.println("\r\n");
        printWriter.flush();

        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            printWriter.println(line);
            printWriter.flush();
            try {
                Thread.sleep( 1 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

//        byte[] fileContent = Files.readAllBytes(file_.toPath());
//
//        for(int i=0;i<fileContent.length;i++){
//            bos.write(fileContent);
//        }
//        bos.flush();
//        reader.close();
//        printWriter.close();
    }
}
