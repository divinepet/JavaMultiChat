package edu.school21.sockets.app;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.ServerSomthing;
import edu.school21.sockets.server.Story;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Main {
    public static LinkedList<ServerSomthing> serverList = new LinkedList<>();
    public static Story story;


    /**
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);

        int PORT = 0;
        try {
            if (args[0].startsWith("--port="))
                PORT = Integer.parseInt(args[0].substring(7));
            else
                throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Add --port=$PORT");
            System.exit(1);
        }
        ServerSocket server = new ServerSocket(PORT);
        story = new Story();
        System.out.println("Server Started");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSomthing(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}
