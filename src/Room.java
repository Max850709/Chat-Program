import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room {
    private String roomName_rm;
    private ArrayList<Socket> listOfClients_ = new ArrayList<>();

    private static ArrayList<Room> room_ = new ArrayList<>();


    public Room(String name_) {
        roomName_rm =name_;
        //listOfClients_=new ArrayList<>();
    }

    public static synchronized Room getRoom(String roomName_){
//        Room temp = WSEnd.room;
//        if(room_.contains(temp)){
//            System.out.println("contain!"+ roomName_);
//        }
//        else{
//            System.out.println("ADD");
//            temp = new Room(roomName_);
//            room_.add(temp);
//            for(Room r:room_){
//                System.out.println("num: "+r);
//            }
//        }
//        for(Room r:room_){
//            System.out.println("Room: "+r.roomName_rm);
//        }
//
//        return temp;
        for(Room r:room_){
            if(r.roomName_rm.equals(roomName_)){
                return r;
            }
        }
        Room new_room = new Room(roomName_);
        room_.add(new_room);

        for(Room r:room_){
            System.out.println("Room: "+r.roomName_rm);
        }
        return new_room;
    }

    public synchronized void addClient(Socket client_) {
        listOfClients_.add(client_);
    }

    public synchronized void sendMsgToAll(String extractedMessage) throws IOException {

        for(Socket s:listOfClients_){
            DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());

            byte[] header = new byte[2];
            header[0] = (byte) (0x81); // fin bit is 1 and opcode is 1
            header[1] = (byte) extractedMessage.length();
            byte[] message = extractedMessage.getBytes();
            outputStream.write(header);
            outputStream.write(message);
        }

    }
}
