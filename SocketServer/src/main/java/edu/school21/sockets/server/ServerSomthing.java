package edu.school21.sockets.server;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.services.UsersService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

import static edu.school21.sockets.app.Main.story;
import static edu.school21.sockets.app.Main.serverList;


public class ServerSomthing extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    /**
     * @param socket
     * @throws IOException
     */

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        welcome();
        story.printStory(out);
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            word = in.readLine();
            try {
                out.write(word + "\n");
                out.flush();
            } catch (IOException ignored) {}
            try {
                while (true) {
                    word = in.readLine();
                    if(word.equals("stop")) {
                        this.downService();
                        break;
                    }
                    System.out.println("Echoing: " + word);
                    story.addStoryEl(word);
                    for (ServerSomthing vr : serverList) {
                        vr.send(word);
                    }
                }
            } catch (NullPointerException ignored) {}
        } catch (IOException e) {
            this.downService();
        }
    }

    /**
     * @param msg
     */
    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }

    private void welcome() {
        String cmd;
        try {
            cmd = in.readLine();
            System.out.println(cmd);
            if (cmd.equals("signUp"))
                register();
            else if (cmd.equals("signIn"))
                login();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login() {
        try {
            out.write("Enter username:\n");
            out.flush();
            String userName = in.readLine();
            out.write("Enter password:\n");
            out.flush();
            String password = in.readLine();
            ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
            UsersService usersService = context.getBean(UsersService.class);
            System.out.println(userName + " " + password);
            if (usersService.signIn(userName, password)) {
                out.write("0\n");
            } else {
                out.write("2\n");
            }
            out.flush();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            this.downService();
        }
    }

    private void register() {
        try {
            out.write("Enter username:\n");
            out.flush();
            String userName = in.readLine();
            out.write("Enter password:\n");
            out.flush();
            String password = in.readLine();
            ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
            UsersService usersService = context.getBean(UsersService.class);
            System.out.println(userName + " " + password);
            if (usersService.signUp(userName, password)) {
                out.write("0\n");
            } else {
                out.write("1\n");
            }
            out.flush();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            this.downService();
        }
    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}
