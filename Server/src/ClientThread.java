import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;



import java.io.ObjectInputStream;

public class ClientThread extends Thread {
    private Socket socket;



    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Bingo serverGame;
    private Consumer<Serializable> callback;
    private static volatile LinkedBlockingQueue<Object> messages = new LinkedBlockingQueue<>();


    private static int players = 0;
    private int playerNumber;

    //private static volatile hashmap for all players
    private static volatile HashMap<Integer, Boolean> connections = new HashMap<>();
    private static volatile HashMap<Integer, ObjectOutputStream> outputClient = new HashMap<>();


    ClientThread(Socket s, Bingo game, Consumer<Serializable> callback,ObjectOutputStream output,int player) throws IOException {
        this.socket = s;
        this.out= output;
        this.serverGame = game;
        this.callback = callback;
        players++;
        playerNumber = player;
        connections.put(playerNumber, true);
        outputClient.put(playerNumber, out);
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    //Returns socket connected to server
    public Socket getSocket() {
        return this.socket;
    }

    //Sends data from server to client
    public void send(Serializable data) throws Exception {
        this.out.writeObject(data);
    }

    //Sets player one and player two. Once both are connected, will listen for any input from clients
    public void run() {

        try {
            in = new ObjectInputStream(socket.getInputStream());
            socket.setTcpNoDelay(true);
        } catch (IOException e) {
            System.out.println("Cannot set up input 1");
        }

        //send("playerID "+playerNumber);
        //callback.accept("playerID "+playerNumber);
      callback.accept("newClient");

        try {
            System.out.println("Player Number "+ playerNumber);
            send("playerID"+" "+playerNumber);
        } catch (Exception e) {
            System.out.println("Cannot set up input");
        }

        Thread gameReady = new Thread() {
            public void run() {

                while (true) {

                    if (connections.size() == 4) {
                        System.out.println("4 clients connected");
                        try {
                            send("gameReady");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //callback.accept("gameReady");
                    }
                }
            }


        };

        gameReady.setDaemon(true);
        gameReady.start();


       while (true) {

            try {
                Serializable data = (Serializable) in.readObject();
                System.out.println(data.toString());
                //getInput(data.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public ObjectOutputStream getOut() {
        return this.out;
    }
}