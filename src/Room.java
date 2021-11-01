import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room {
    private static String roomName;
    private static Socket client;
    private static ArrayList<String> rooms = new ArrayList<>();
    private static ArrayList<Socket> listOfClients_ = new ArrayList<>(); // sockets

    private static ArrayList<Room> room_ = new ArrayList<>();

    private static Map<String,Socket> map = new HashMap<>();
    static DataInputStream dis=null;

    public Room(String name_) {
        roomName=name_;
    }

    public static synchronized Room getRoom(String roomName_,Socket client_){
        Room temp;

        if(room_.contains(roomName_)){
            System.out.println("add!!"+roomName);
            for(Room s:room_){
                System.out.println("RRRR"+s);
            }
            temp = new Room(roomName_);
        }
        else{
            System.out.println("contain!"+roomName);
            for(Room s:room_){
                System.out.println("RRRR"+s);
            }
            temp=new Room(roomName);
        }
        return temp;
    }

//    public static synchronized Room getRoom(String roomName_){
//        Room temp = new Room(roomName_);
//        if(rooms.contains(roomName_)==false){
//            rooms.add(temp.roomName);
//            System.out.println("add!!"+roomName);
//            for(String s:rooms){
//                System.out.println("RRRR"+s);
//            }
//
//        }
//        else{
//            System.out.println("contain!"+roomName);
//            for(String s:rooms){
//                System.out.println("RRRR"+s);
//            }
//            temp= new Room(roomName);
//
//        }
//        return temp;
//    }
    public static void sendAllRoomMsgs(Socket client_)throws IOException {
        dis=new DataInputStream(client_.getInputStream());
        WSEnd.decodeBytes(dis);
    }

    public static void addClient() {
        listOfClients_.add(client);
    }

//    public static void addClient(Socket client_, String roomname_) {
//        map.put(roomname_,client_);
//    }

    public static synchronized void sendMsgToAll(String extractedMessage, String room) throws IOException {
//        for(String keys: map.keySet()){
//            System.out.println("ROOM NAME::"+keys + " "+map.get(keys));
//        }
//        System.out.println("NOW ROOM:"+roomName);
//        for(String keys: map.keySet()){
//            if(keys.equals(roomName)){
//                System.out.println("KKKEEEYYY:"+keys);
//                DataOutputStream outputStream = new DataOutputStream(map.get(keys).getOutputStream());
//                byte[] header = new byte[2];
//                header[0] = (byte)(0x81); // fin bit is 1 and opcode is 1
//                header[1] = (byte) extractedMessage.length();
//                byte[] message1 = extractedMessage.getBytes();
//                outputStream.write(header);
//                outputStream.write(message1);
//            }
//        }

        for(Room r:room_){
            DataOutputStream outputStream = new DataOutputStream(r.client.getOutputStream());
            byte[] header = new byte[2];
            header[0] = (byte) (0x81); // fin bit is 1 and opcode is 1
            header[1] = (byte) extractedMessage.length();
            byte[] message1 = extractedMessage.getBytes();
            outputStream.write(header);
            outputStream.write(message1);
        }

//        for(Socket s:listOfClients_){
//            DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());
//
//            byte[] header = new byte[2];
//            header[0] = (byte) (0x81); // fin bit is 1 and opcode is 1
//            header[1] = (byte) extractedMessage.length();
//            byte[] message1 = extractedMessage.getBytes();
//            outputStream.write(header);
//            outputStream.write(message1);
//        }


    }

//    public synchronized void addToRoom(){
//        if(rooms.contains(roomName)){
////            listOfClients_.add(client);
//            System.out.println("contain!"+roomName);
//            for(String s:rooms){
//                System.out.println("RRRR"+s);
//            }
//        }
//        else{
//            rooms.add(this.roomName);
////            listOfClients_.add(client);
//            System.out.println("add!!"+roomName);
//            for(String s:rooms){
//                System.out.println("RRRR"+s);
//            }
//        }
//
//    }



}


/*
private String roomName;
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Socket> listOfClients_ = new ArrayList<>(); // sockets

    Room(String name){
        roomName = name;
    }

    public static synchronized Room getRoom(String roomname){

        return room;
    }

 */