import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Arrays;

public class WSEnd {
    static Room temp = null;
    static String roomName;
    public static void dealwithmsg(DataInputStream dis, DataOutputStream outputStream, Socket client_) throws IOException {

        String echoMessage = decodeBytes(dis);
        System.out.println("sending message to client: " + echoMessage);
        //sendBack(echoMessage,outputStream); //echoes back the message
        System.out.println("decodeBytes:"+echoMessage);
        String[] parsedEcho = echoMessage.split(" ", 3);
        //System.out.println(parsedEcho[0] + " " + parsedEcho[1] + " " + parsedEcho[2]);

        char colonCheck = parsedEcho[1].charAt(0); //for detecting "username: message"
        System.out.println("colon check: "+colonCheck);

        if(parsedEcho[0].equals("join")) {         // client wants to join room
            System.out.println("user joined room");
            roomName = parsedEcho[1];    // gets room name
//            temp=new Room(roomName);
            temp.getRoom(roomName,client_);
            temp.addClient();
            System.out.println("user is in room: " + roomName);
//                            thisRoom = Room.getRoom(roomName);  // gets or makes room
//                        thisRoom.sendAllRoomMsgs(client);   // gets all messages from room
//                        thisRoom.addClient(client);         // adds client socket to room
            temp= new Room(parsedEcho[1]);
        }
        else if( colonCheck == ':' ) {  //client wants to send a message
            System.out.println("checking for colon");
            String jsonMsg = "{\"user\": \""+parsedEcho[0]+
                    "\", \"message\": \""+parsedEcho[1]+"\"}";
            System.out.println("jsonMsg=="+jsonMsg);
            temp.sendMsgToAll(jsonMsg,parsedEcho[1]);
//            sendBack(jsonMsg,outputStream);
            
//                        thisRoom.sendMsgToAll(parsedEcho[0], parsedEcho[2]); //convert to JSON object
        }
        else{
            System.out.println("nothing");
        }
    }

    public static String decodeBytes(DataInputStream dis) throws IOException {
        System.out.println("decode bytes function running");
        byte byte1 = dis.readByte();
        byte byte2 = dis.readByte();
//            byte[] encoded = new byte[]{(byte) 0x81, (byte) 0x05, (byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f}; //hello
        byte opcode = (byte) (byte1 & 0x0F);
        System.out.println("opcode is: " + opcode);
        boolean maskExists = false;
        if ((byte2 & 0x80) != 0) {   //either 128 or 0;
            maskExists = true;
        }
        System.out.println("mask exists: " + maskExists);
        int payloadLength = 0;
        String payload = null;
//            int maskStartByte = 0;
//            int payloadStartByte = 0;
//
        if ((byte2 & 0x7F) <= 125) { //payload length is in b1 only
            payloadLength = (byte2 & 0x7F);
//                maskStartByte = 2; //mask is 4 bytes at b2-5
        } else if ((byte2 & 0x7F) == 126) { //payload length is in b2-3
            payloadLength = dis.readShort();
//                maskStartByte = 4; //mask is 4 bytes at b4-7
        } else if ((byte2 & 0x7F) == 127) {
//                payloadLength = ((encoded[2] << 56) | (encoded[3] << 48) | (encoded[4] << 40) | (encoded[5] << 32)
//                        | (encoded[6] << 24) | (encoded[7] << 16) | (encoded[8] << 8) | encoded[9]);
            payloadLength = (int) dis.readLong();
//                maskStartByte = 10; //mask is 4 bytes at b10-13
        }
        System.out.println("payload length: " + payloadLength);
//
        byte[] maskBytes = new byte[4];
//
//        Create masking key byte array
        if (maskExists) {
            maskBytes = dis.readNBytes(4);
            System.out.println("Mask bytes array: " + Arrays.toString(maskBytes));
//                payloadStartByte = maskStartByte + 4;
        } else {
//                payloadStartByte = maskStartByte;
            System.out.println("No Mask in frames.");
        }
        byte[] payloadBytes = new byte[payloadLength];
        payloadBytes = dis.readNBytes(payloadLength);
        System.out.println("Payload bytes array: " + Arrays.toString(payloadBytes));
//        Decoding payload data with masking key
        byte[] decodedPayloadBytes = new byte[payloadLength];
        if (maskExists) {
            for (int j = 0; j < payloadLength; j++) {
                decodedPayloadBytes[j] = (byte) (payloadBytes[j] ^ maskBytes[j % 4]);
            }
            System.out.println("Final decoded bytes: " + Arrays.toString(decodedPayloadBytes));
            payload = new String(decodedPayloadBytes);
            System.out.println("Final decoded message: " + payload);
        } else {
            payload = new String(payloadBytes);
            System.out.println("Final decoded message: " + payload);
        }
        return payload;
    }

    public static void sendBack(String extractedMessage, DataOutputStream myOs) throws IOException{
        //OutputStream myOs = client.getOutputStream();
        byte[] header = new byte[2];
        header[0] = (byte)(0x81); // fin bit is 1 and opcode is 1
        header[1] = (byte) extractedMessage.length();
        byte[] message1 = extractedMessage.getBytes();
        myOs.write(header);
        myOs.write(message1);
    }

//    public static void parseJson(Socket client_,String JsonMsg) throws IOException {
//        DataOutputStream outputStream = new DataOutputStream(client_.getOutputStream());
//        outputStream.write(JsonMsg.getBytes());
//        outputStream.flush();
//        System.out.println("~!@#$");
//
//    }
}

