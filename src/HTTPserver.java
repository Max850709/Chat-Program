//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//import java.lang.String;
//import java.util.Map;
//import java.io.IOException;
//import java.lang.String;
//public class HTTPserver {
//    public static class myRunnable implements Runnable {
//        public Socket client;
//        public Room thisRoom = null;
//        public myRunnable(Socket clientSocket) {
//            client = clientSocket;
//        }
//        public String generateResponseKey(String requestKey) throws NoSuchAlgorithmException {
//            String provided = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
//            requestKey += provided;
//            MessageDigest md = MessageDigest.getInstance("SHA-1");
//            byte[] hashed = md.digest(requestKey.getBytes());
//            String result = Base64.getEncoder().encodeToString(hashed);
////            System.out.println("responseKey: " + result);
//            return result;
//        }
//        @Override
//        public void run() {
//            InputStreamReader input = null;
//            try {
//                input = new InputStreamReader(client.getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Scanner scan = new Scanner(input);
//            //scan first line :  GET / HTTP/1.1
//            String command = scan.next();
//            String fileName = scan.next();
//            String protocol = scan.next();
////            System.out.println(command + " " + fileName + " " + protocol);
//            String line = scan.nextLine();
//            Map<String, String> data;
//            data = new HashMap<>();
//            OutputStream output = null;
//            try {
//                output = client.getOutputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            PrintWriter pw = new PrintWriter(output);
//            while (scan.hasNextLine()) {
//                String[] tmp = scan.nextLine().split(": ", 2);
//                if (tmp[0].isEmpty()) {
//                    break;
//                }
//                data.put(tmp[0], tmp[1]);
////                System.out.println(tmp[0] + ": " + tmp[1]);
//            }
//            String upgradeValue = data.get("Upgrade"); // equals websocket
//            String connectionValue = data.get("Connection"); // equals Upgrade
//            String requestKey = data.get("Sec-WebSocket-Key"); // get key
////            System.out.println("\nWS values from map: " + connectionValue + " " + upgradeValue + " " + requestKey);
////            // Handle the response to the client
//            String filePath = "resources";
//            String indexPath = "/index.html";
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (fileName.equals("/")) {
//                filePath += indexPath;
//            } else {
//                filePath += fileName;
//            }
//            if (connectionValue.equals("Upgrade") && upgradeValue.equals("websocket")) {
//                System.out.println("WS request received\n");
////            HTTP/1.1 101 Switching Protocols
//                pw.write("HTTP/1.1 101 Switching Protocols\r\n");
//                pw.write("Connection: " + connectionValue + "\r\n");
//                pw.write("Upgrade: " + upgradeValue + "\r\n");
//                String responseKey = null;
//                try {
//                    responseKey = generateResponseKey(requestKey);
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//                pw.write("Sec-WebSocket-Accept: " + responseKey + "\r\n\r\n");
////                pw.println("\r");
//                pw.flush();
//                DataInputStream dis = null;
//                try {
//                    dis = new DataInputStream(client.getInputStream());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
////                //testing Data decoding with hello
////                byte[] encoded = new byte[]{(byte) 0x81, (byte) 0x85, (byte) 0x37, (byte) 0xfa, (byte) 0x21, (byte) 0x3d, (byte) 0x7f, (byte) 0x9f, (byte) 0x4d, (byte) 0x51, (byte) 0x58};
////                InputStream is = new ByteArrayInputStream(encoded);
////                dis = new DataInputStream(is);
////        // Start data input stream
////        dis = new DataInputStream(is);
//                String echoMessage = null;
//                while(true) {
//                    try {
//                        echoMessage = decodeBytes(dis);
//                        System.out.println("sending message to client: " + echoMessage);
////                        sendBack(echoMessage); //echoes back the message
//                        String[] parsedEcho = echoMessage.split(" ", 3);
//                        System.out.println(parsedEcho[0] + " " + parsedEcho[1] + " " + parsedEcho[2]);
//                        char colonCheck = parsedEcho[1].charAt(0); //for detecting "username: message"
//                        System.out.println(colonCheck);
//                        if(parsedEcho[1].equals("joined")) {         // client wants to join room
//                            System.out.println("user joined room");
//                            String roomName = parsedEcho[2];    // gets room name
//                            System.out.println("user is in room: " + roomName);
////                            thisRoom = Room.getRoom(roomName);  // gets or makes room
//                            thisRoom.sendAllRoomMsgs(client);   // gets all messages from room
//                            thisRoom.addClient(client);         // adds client socket to room
//                        }
//                        else if( colonCheck == ':' ) {  //client wants to send a message
//                            System.out.println("checking for colon");
//                            thisRoom.sendMsgToAll(parsedEcho[0], parsedEcho[2]); //convert to JSON object
//                        }
////
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
////                pw.flush();
////                pw.close();
//            }
//            else if (!connectionValue.equals("Upgrade")) {
//                pw.println("HTTP/1.1 200 OK");
//                System.out.println("HTTP request received\n");
//                try {
//                    File file = new File(filePath);
//                    BufferedReader reader = new BufferedReader(new FileReader(file));
//                    String line2 = reader.readLine();// line to go line by line from file
//                    while (line2 != null) { // repeat till the file is read
//                        pw.println(line2);
//                        line2 = reader.readLine();
//                    }
//                } catch (IOException fnfe) {
//                    try {
//                        File errorFile = new File("resources/404.html");
//                        BufferedReader errorReader = new BufferedReader(new FileReader(errorFile));
//                        String line3 = errorReader.readLine();// line to go line by line from file
//                        while (line3 != null) {
//                            pw.println(line3);
//                            line3 = errorReader.readLine();
//                        }
//                    } catch (IOException fnfe2) {
//                        System.err.println("404 file not found error");
//                    }
//                }
//                pw.flush();
//            }
//            pw.close();
//        }
//        public static void main(String[] args) throws IOException {
//            ServerSocket server = new ServerSocket(8080);
//            System.out.println("server socket created");
//            while (true) {
//                // Handle data from client
//                try {
//                    Socket client = server.accept();
//                    Runnable runner = new myRunnable(client);
//                    Thread t = new Thread(runner);
//                    t.start();
////                    t.join();
//                }
//                catch (IOException ioe) {
//                    System.err.println("IO error: server not accepted.");
//                }
//            }
//        }
//        //        public void joinRoom(String roomName){}
//        public void sendBack(String extractedMessage) throws IOException{
//            OutputStream myOs = client.getOutputStream();
//            byte[] header = new byte[2];
//            header[0] = (byte)(0x8 << 4 | 0x1); // fin bit is 1 and opcode is 1
//            header[1] = (byte) extractedMessage.length();
//            byte[] message1 = extractedMessage.getBytes();
//            myOs.write(header);
//            myOs.write(message1);
//        }
//        public static String decodeBytes(DataInputStream dis) throws IOException {
//            System.out.println("decode bytes function running");
//            byte byte1 = dis.readByte();
//            byte byte2 = dis.readByte();
////            byte[] encoded = new byte[]{(byte) 0x81, (byte) 0x05, (byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f}; //hello
//            byte opcode = (byte) (byte1 & 0x0F);
//            System.out.println("opcode is: " + opcode);
//            boolean maskExists = false;
//            if ((byte2 & 0x80) != 0) {   //either 128 or 0;
//                maskExists = true;
//            }
//            System.out.println("mask exists: " + maskExists);
//            int payloadLength = 0;
//            String payload = null;
////            int maskStartByte = 0;
////            int payloadStartByte = 0;
////
//            if ((byte2 & 0x7F) <= 125) { //payload length is in b1 only
//                payloadLength = (byte2 & 0x7F);
////                maskStartByte = 2; //mask is 4 bytes at b2-5
//            } else if ((byte2 & 0x7F) == 126) { //payload length is in b2-3
//                payloadLength = dis.readShort();
////                maskStartByte = 4; //mask is 4 bytes at b4-7
//            } else if ((byte2 & 0x7F) == 127) {
////                payloadLength = ((encoded[2] << 56) | (encoded[3] << 48) | (encoded[4] << 40) | (encoded[5] << 32)
////                        | (encoded[6] << 24) | (encoded[7] << 16) | (encoded[8] << 8) | encoded[9]);
//                payloadLength = (int) dis.readLong();
////                maskStartByte = 10; //mask is 4 bytes at b10-13
//            }
//            System.out.println("payload length: " + payloadLength);
////
//            byte[] maskBytes = new byte[4];
////
////        Create masking key byte array
//            if (maskExists) {
//                maskBytes = dis.readNBytes(4);
//                System.out.println("Mask bytes array: " + Arrays.toString(maskBytes));
////                payloadStartByte = maskStartByte + 4;
//            } else {
////                payloadStartByte = maskStartByte;
//                System.out.println("No Mask in frames.");
//            }
//            byte[] payloadBytes = new byte[payloadLength];
//            payloadBytes = dis.readNBytes(payloadLength);
//            System.out.println("Payload bytes array: " + Arrays.toString(payloadBytes));
////        Decoding payload data with masking key
//            byte[] decodedPayloadBytes = new byte[payloadLength];
//            if (maskExists) {
//                for (int j = 0; j < payloadLength; j++) {
//                    decodedPayloadBytes[j] = (byte) (payloadBytes[j] ^ maskBytes[j % 4]);
//                }
//                System.out.println("Final decoded bytes: " + Arrays.toString(decodedPayloadBytes));
//                payload = new String(decodedPayloadBytes);
//                System.out.println("Final decoded message: " + payload);
//            } else {
//                payload = new String(payloadBytes);
//                System.out.println("Final decoded message: " + payload);
//            }
//            return payload;
//        }
//    }
//}