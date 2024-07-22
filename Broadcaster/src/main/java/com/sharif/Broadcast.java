package com.sharif;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Broadcast {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(8081);
        System.out.println("Starting broadcaster server");

        sendMessagesInRandomIntervals(serverSocket);
    }

    public static void sendMessagesInRandomIntervals(ServerSocket serverSocket){
        Random random = new Random();
        Socket socket = null;

        while(true){
            try {
                socket = serverSocket.accept();
                PrintStream printStream = new PrintStream(socket.getOutputStream());
                int interval = random.nextInt(10) + 1;
                TimeUnit.SECONDS.sleep(interval);

                String msg = "Hello world (" + interval + " seconds)";
                System.out.println("Sending message to client: " + msg);
                printStream.print(msg);

                socket.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}