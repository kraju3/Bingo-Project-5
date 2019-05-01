import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.function.Consumer;



import java.io.ObjectInputStream;

public class ClientThread extends Thread {
    private Socket socket;
    private ObjectOutputStream out;

    private ObjectInputStream in;
    private Bingo serverGame;
    private Consumer<Serializable> callback;
    private int points=0;

    private volatile boolean startDraw;
    private static volatile boolean stopDraw;
    private static volatile boolean restart;

    private static int players = 0;
    private int playerNumber;
    private static  volatile Stack<Integer> numbersDrawn = new Stack<Integer>();
    //private static volatile hashmap for all players
    private static volatile HashMap<Integer, Boolean> connections = new HashMap<>();
    private static volatile HashMap<Integer, ObjectOutputStream> outputClient = new HashMap<>();
    private static volatile HashMap<Integer,Integer> leaderBoard =new HashMap<>();


    ClientThread(boolean On){
        this.startDraw=On;
        stopDraw=true;
    }
    ClientThread(Socket s, Bingo game, Consumer<Serializable> callback,ObjectOutputStream output,int player) throws IOException {
        this.socket = s;
        this.out= output;
        this.serverGame = game;
        this.callback = callback;
        players++;
        playerNumber = player;
        connections.put(playerNumber, true);
        outputClient.put(playerNumber, out);
        leaderBoard.put(playerNumber,0);
        this.startDraw=false;
        restart =false;
        try {
            in = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            System.out.println("Cannot set up input 1");
        }
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
        while (true) {
            if (this.startDraw) {
                sendNumber();
            }//this statement is for the separate client thread that is drawing all the number for the client;

        else {//the clientThreads that do not draw

                callback.accept("newClient");
                try {
                    send(this.serverGame.getBingoSheet());
                } catch (Exception e) {
                    System.out.println("sending bingo sheet");
                }

                setUpGame();
                System.out.println("Reached here by "+playerNumber);
                restart=false;
           }
        }
    }

    public void sendNumber(){
        while (stopDraw) {

            try {
                Thread.sleep(12000);
                Random r = new Random();
                int drawnNumber = r.nextInt((49) + 1);
                numbersDrawn.push(drawnNumber);
                System.out.println("Draw: " + drawnNumber);
                callback.accept("drawn"+" "+drawnNumber);
                for (ObjectOutputStream o : outputClient.values()) {
                    try {
                        sendtoAllClients("drawn" + " " + drawnNumber, o);
                    } catch (IOException e) {
                        System.out.println("error sending it to client");
                    }
                }

            } catch (InterruptedException e) {
                System.out.println("Error sleeping the thread while drawing numbers");
            }
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(stopDraw==false){
            numbersDrawn.clear();
        }
    }

    public void readData(){
        while(true){

          if(!restart){
                try {
                    Serializable data = (Serializable) in.readObject();
                    System.out.println(data.toString());
                    getInput(data.toString());

                } catch (Exception e) {
                    System.out.println(e);
                    }

                }
          else{
              restart=true;
              stopDraw=true;
              break;
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
                //Thread.sleep(2000);
                verifyBingo(parsedData);
                break;
            default:
                break;
        }
    }

    public void verifyBingo(String[] bingoToken){
        if(this.serverGame.verifyBingo(bingoToken)){
            stopDraw=false;//stops the drawer
            points++;
            leaderBoard.replace(playerNumber,points);
            callback.accept("gameWinner "+playerNumber+" "+points);
            try {
                Thread.sleep(2000);
                for(ObjectOutputStream x:outputClient.values()) {
                    try {
                        sendtoAllClients("gameWinner " + playerNumber+" "+points, x);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Thread.sleep(2000);

                restart =true;


            } catch (Exception e) {
                System.out.println("error sending bingo validator");
            }
        }
        else{
            try {
                send("invalidBingo");
                callback.accept("invalidBingo");
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
        String leader = "";
        for (Map.Entry mapElement : leaderBoard.entrySet()) {
            int playerID = (int)mapElement.getKey();

            // Add some bonus marks
            // to all the students and print it
            int score = (int)mapElement.getValue();

            leader=leader.concat(playerID+" "+score+" ");
        }
            callback.accept("leaderBoard "+leader+"end");
        try {
            send("leaderBoard "+leader+"end");
        } catch (Exception e) {
            System.out.println("Sending the leaderboard gone wrong");
        }
    }

    public void setUpGame(){

     while(true){
        try {

            Thread.sleep(1000);


            if (connections.size() < 4) {
                try {
                    Thread.sleep(1000);
                    send("waiting");
                    callback.accept("waiting");
                } catch (Exception e) {
                    System.out.println("error waiting");
                }
            }
            else if(connections.size()==4) {
                try {
                    Thread.sleep(1000);
                    send("gameReady");
                    callback.accept("gameReady");
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
        }
      readData();
    }


    public Stack getNumDrawn(){
        return numbersDrawn;
    }
}