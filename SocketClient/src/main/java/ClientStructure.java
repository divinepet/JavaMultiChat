import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

class ClientStructure {
    public static final String CYAN = "\u001B[36m";
    public static final String RESET = "\u001B[0m";
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String addr;
    private int port;
    private String nickname;
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;

    /**
     @param addr
     @param port
     */

    public ClientStructure(String addr, int port) {
        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.registration();
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            ClientStructure.this.downService();
        }
    }

    private void registration() {
        System.out.println("enter a command");
        try {
            String cmd = inputUser.readLine();
            out.write(cmd + "\n");
            out.flush();
            String serverMsg = in.readLine();
            System.out.println(serverMsg);
            nickname = inputUser.readLine();
            out.write(nickname + "\n");
            out.flush();
            serverMsg = in.readLine();
            System.out.println(serverMsg);
            String password = inputUser.readLine();
            out.write(password + "\n");
            out.flush();
            serverMsg = in.readLine();
            if (serverMsg.equals("0"))
                System.out.println("Start messaging");
            else if (serverMsg.equals("1")) {
                System.out.println("Такой пользователь уже есть. Используй signIn");
                this.downService();
                System.exit(1);
            } else {
                System.out.println("Неверные логин или пароль.");
                this.downService();
                System.exit(1);
            }
        } catch (IOException ignored) {
        }

    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals("stop")) {
                        ClientStructure.this.downService();
                        break;
                    }
                    System.out.println(str);
                }
            } catch (IOException e) {
                ClientStructure.this.downService();
            }
        }
    }

    public class WriteMsg extends Thread {

        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                    time = new Date();
                    dt1 = new SimpleDateFormat("HH:mm:ss");
                    dtime = dt1.format(time);
                    userWord = inputUser.readLine();
                    if (userWord.equals("stop")) {
                        out.write("stop" + "\n");
                        ClientStructure.this.downService();
                        break;
                    } else {
                        out.write( "(" + dtime + ") " + nickname + ": " + userWord + "\n");
                    }
                    out.flush();
                } catch (IOException e) {
                    ClientStructure.this.downService();
                }
            }
        }
    }
}