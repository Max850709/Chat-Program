import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MyThread implements Runnable{
    public Socket client;
        public MyThread(Socket socket_){
            client=socket_;
        }
        @Override
        public void run() {
            System.out.println("--------Connected!");
            String request=null;
            DataInputStream msg=null;
            Map<String,String> requestMap = null;
            String clientmsg=null;
            InputStream userRequest=null;
            DataInputStream dis = null;

            //------------------------------------------
            try {
                userRequest = client.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
//            dis = new DataInputStream(userRequest);
//            System.out.println("DIS:::"+dis);
            //------------------------------------------

            try {
                requestMap = Request(client);
                for(String keys : requestMap.keySet()){
                    System.out.println("This is the request map " + keys + ":"+ requestMap.get(keys));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            if((requestMap.get("Upgrade")==null)){
                System.out.println("--------Into HTTP");
                request=requestMap.get("Address");
                System.out.println("--------File Request: "+request);
                try {
                    HTTP.Response(client,request);
                    System.out.println("HTTP!!!!!!");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else{
                System.out.println("--------Upgrade to WS");
                try {
                    WebSocket.WSResponse(client,requestMap);
                    System.out.println("WS!!!!!!");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
//                byte[] encoded = new byte[]{(byte) 0x81, (byte) 0x85, (byte) 0x37, (byte) 0xfa, (byte) 0x21, (byte) 0x3d, (byte) 0x7f, (byte) 0x9f, (byte) 0x4d, (byte) 0x51, (byte) 0x58};
//                InputStream is = new ByteArrayInputStream(encoded);
//                dis = new DataInputStream(is);


                //------------------------------------------
                String echoMessage = null;
                DataOutputStream outputStream=null;
                while(true) {
                    //Socket cl = client;
                    dis=new DataInputStream(userRequest);
                    try {
                        outputStream = new DataOutputStream(client.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        WSEnd.dealwithmsg(dis,outputStream,client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        echoMessage = WSEnd.decodeBytes(dis);
//                        System.out.println("sending message to client: " + echoMessage);
//                        WSEnd.sendBack(echoMessage,outputStream); //echoes back the message
//                        System.out.println("decodeBytes:::::"+echoMessage);
//                        String[] parsedEcho = echoMessage.split(" ", 3);
//                        //System.out.println(parsedEcho[0] + " " + parsedEcho[1] + " " + parsedEcho[2]);
//
//                        char colonCheck = parsedEcho[1].charAt(0); //for detecting "username: message"
//                        System.out.println("colon check: "+colonCheck);
//
//                        if(parsedEcho[0].equals("join")) {         // client wants to join room
//                            System.out.println("user joined room");
//                            String roomName = parsedEcho[1];    // gets room name
//                            System.out.println("user is in room: " + roomName);
////                            thisRoom = Room.getRoom(roomName);  // gets or makes room
////                        thisRoom.sendAllRoomMsgs(client);   // gets all messages from room
////                        thisRoom.addClient(client);         // adds client socket to room
//                        }
//                        else if( colonCheck == ':' ) {  //client wants to send a message
//                            System.out.println("checking for colon");
//                            String jsonMsg = "{\"user\": \""+parsedEcho[0]+
//                                              "\", \"message\": \""+parsedEcho[1]+"\"}";
//                            System.out.println("jsonMsg=="+jsonMsg);
//                            WSEnd.sendBack(jsonMsg,outputStream);
//
////                        thisRoom.sendMsgToAll(parsedEcho[0], parsedEcho[2]); //convert to JSON object
//                        }
//                        else{
//                            System.out.println("nothing");
//                        }
////
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }


            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    public static Map Request(Socket client_) throws IOException {
        InputStream userRequest = client_.getInputStream();
        System.out.println("user_____"+userRequest);
        Scanner scanRequest = new Scanner(userRequest);

        Map<String,String> map = new HashMap<>();
        String cmd = scanRequest.next();
        String filename = scanRequest.next();
        String protocol = scanRequest.next();
        map.put("CMD",cmd);
        map.put("Address",filename);
        map.put("Protocol",protocol);

        scanRequest.nextLine();

        while(scanRequest.hasNextLine()){
            String line = scanRequest.nextLine();
            //System.out.println("request line " + line);
            if(line.isEmpty()){
                break;
            }
            String [] temp = line.split(":",2);
            String key = temp[0];
            String value = temp[1];
            map.put(key,value);
        }

        return map;
    }

    public static String handleMessage(Socket client_) throws IOException {
        InputStream userRequest = client_.getInputStream();
        Scanner scanRequest = new Scanner(userRequest);
        String msg = new String();
        while(scanRequest.hasNextLine()){
            msg+=scanRequest.next();
        }
        return msg;
    }
}
