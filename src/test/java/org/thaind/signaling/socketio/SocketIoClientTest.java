package org.thaind.signaling.socketio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.thaind.signaling.common.Constants;
import org.thaind.signaling.dto.EventPacket;

import java.util.Arrays;

/**
 * @author duyenthai
 */

public class SocketIoClientTest {

    private static final String url = "http://localhost:8088";

    public static void main(String[] args) {
        try {

            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket"};
            // Number of failed retries
            options.reconnectionAttempts = 10;
            // Time interval for failed reconnection
            options.reconnectionDelay = 1000;
            // Connection timeout (ms)
            options.timeout = 500;
            final Socket socket = IO.socket(url, options);
            socket.on("Connect", object -> {
                System.out.println(Arrays.toString(object));
                EventPacket eventPacket = new EventPacket(Constants.PacketServiceType.PING.getServiceType(), "");
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    System.out.println("Ping back directly");
                    String event = objectMapper.writeValueAsString(eventPacket);
//                    System.out.println(event);
                    socket.emit("EventPacket", event);
//                    socket.emit("EventPacket", eventPacket);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
//                socket.emit("abc","Abc");
            });
            socket.on("connect", object -> {
                System.out.println(Arrays.toString(object));
//                socket.emit("abc","Abc");
                EventPacket eventPacket = new EventPacket(Constants.PacketServiceType.PING.getServiceType(), "");
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    System.out.println("Ping back directly");
                    String event = objectMapper.writeValueAsString(eventPacket);
//                    System.out.println(event);
                    socket.emit("EventPacket", event);
//                    socket.emit("EventPacket", eventPacket);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            socket.on("EventPacket", object -> {
                System.out.println("Res packet " + Arrays.toString(object));
                for(Object o : object){
                    System.out.println(o);
                }
            });
//            socket.on("Connect", objects -> System.out.println ("client:"+ "successful subscription, feedback - >" + Arrays.toString (objects)));
//            spam(socket);
            socket.connect();
//            spam(socket);
            System.out.println("Connection ok");
        } catch (Exception ex) {
            System.err.println("Error when try to do something");
            ex.printStackTrace();
        }
    }

    static void spam(Socket socket){
        EventPacket eventPacket = new EventPacket(Constants.PacketServiceType.PING.getServiceType(), "");
        socket.emit("EventPacket", eventPacket);
        System.out.println("Send spam ping");
    }

}
