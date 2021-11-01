import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {


    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serversocket = new ServerSocket(port);
        System.out.println("Server is running on port: "+port);
        while(true){
            //connect
            try {
                Socket client = serversocket.accept();
                if (client != null) {
                    MyThread runner = new MyThread(client);
                    Thread th = new Thread(runner);
                    th.start();
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();

            }
        }
    }
}
