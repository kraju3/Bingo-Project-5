import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import java.util.function.Consumer;



import java.io.ObjectInputStream;

public class ClientThread extends Thread {
    private Socket socket;
    private ObjectOutputStream out;
    private int points = 0;
    private ObjectInputStream in;
    private Bingo serverGame;
    private Consumer<Serializable> callback;

    private volatile boolean verify = false;
    static volatile Random random;

    private static int players = 0;
    private int playerNumber;
    private volatile Stack<Integer> numbersDrawn = new Stack<>();
    //private static volatile hashmap for all players
    private static volatile HashMap<Integer, Boolean> connections = new HashMap<>();
    private static volatile HashMap<Integer, ObjectOutputStream> outputClient = new HashMap<>();


    ClientThread(Socket s, Bingo game, Consumer<Serializable> callback,ObjectOutputStream output,int player,Random r) throws IOException {
        this.socket = s;
        this.out= output;
        this.serverGame = game;
        this.callback = callback;
        this.numbersDrawn=new Stack<>();
        random=r;
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

    public void sendtoAllClients(Serializable data,ObjectOutputStream x) throws IOException {
        x.writeObject(data);

    }

    //Sets player one and player two. Once both are connected, will listen for any input from clients
    public void run() {
        boolean clientOn = true;
            try {
                in = new ObjectInputStream(socket.getInputStream());
                socket.setTcpNoDelay(true);
            } catch (IOException e) {
                System.out.println("Cannot set up input 1");
            }


            callback.accept("newClient");
            setUpGame();
            int numberDrawn;


            while(true) synchronized (random){
                try{
                Thread.sleep(10000);
                int drawnNumber = random.nextInt((49) + 1);
                numbersDrawn.push(drawnNumber);
                send("drawn"+" "+drawnNumber);
                System.out.println("Client "+playerNumber+" Draw"+drawnNumber);
                }
                catch(Exception e){System.out.println("error sending drawn numbers");}

                try {
                    Serializable data = (Serializable) in.readObject();
                    System.out.println(data.toString());
                    getInput(data.toString());
                    callback.accept("Client " + playerNumber + " " + data.toString());
                } catch (Exception e) {
                    System.out.println(e);
                }
        }
    }




    public ObjectOutputStream getOut() {
        return this.out;
    }
    public void getInput(String data) throws InterruptedException {
        String[] parsedData = data.split(" ");

        switch (parsedData[0]){
            case "history":
                sendHistory();
                break;
            case "leaderBoard":
                sendleaderBoard();
                break;
            case "bingo":
                this.verify=true;
                Thread.sleep(2000);
                verifyBingo(parsedData);
            default:
                break;
        }
    }
    public boolean getVerify(){
        return this.verify;
    }

    public void verifyBingo(String[] bingoToken){
        if(this.serverGame.verifyBingo(bingoToken)){
            points++;
            try {
                Thread.sleep(5000);
                for(ObjectOutputStream x:outputClient.values()) {
                    try {
                        sendtoAllClients("gameWinner " + playerNumber+" "+points, x);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                    callback.accept("Client "+playerNumber+" "+"won");
            } catch (Exception e) {
                System.out.println("error sending bingo validator");
            }
        }
        else{
            try {
                send("invalidBingo");
            } catch (Exception e) {
                System.out.println("error sending bingo validator");
            }
        }
    }

   //if the client rrequests the history of numbers drawn;
    public void sendHistory() {
        String numberHistory="";
        for(Integer x:numbersDrawn){
            numberHistory=numberHistory.concat(x+" ");
        }
        try {
            send("history"+" "+numberHistory+" "+"end");
        } catch (Exception e) {
           System.out.println("Sending history gone wrong");
        }
    }
    // if client requests the leadrboard
    public void sendleaderBoard()  {
        try {
            send("leaderBoard playerID points end");
        } catch (Exception e) {
            System.out.println("Sending the leaderboard gone wrong");
        }
    }

    public void setUpGame(){

     while(true){
        try {
            //System.out.println("Player Number " + playerNumber);

            Thread.sleep(1000);
            send(this.serverGame.getBingoSheet());
            //this.out.flush();
            if (connections.size() < 4) {
                try {
                    Thread.sleep(1000);
                    send("waiting");
                    //callback.accept("waiting for more");
                } catch (Exception e) {
                    System.out.println("error waiting");
                }
            }
            else if(connections.size()==4) {
                try {
                    Thread.sleep(1000);
                    send("gameReady");
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if(connections.size()>4){
                try {
                    Thread.sleep(1000);
                    send("gameReady");
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        } catch (Exception e) {
            System.out.println("Cannot set up input");
        }
    }}


    public Stack getNumDrawn(){
        return numbersDrawn;
    }
}