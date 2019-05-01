// Jakub Krzeptowski-Mucha
// CS342
// Project 3


import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

public abstract class NetworkConnection {

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;
    private ObjectOutputStream out;
    ArrayList<ClientThread> AllClients= new ArrayList<>();





    private static volatile Boolean serverReset = false;

    public void setServerReset(Boolean bool){ serverReset = bool; }

    public NetworkConnection(Consumer<Serializable> callback){
        this.callback = callback;
        connthread.setDaemon(true);
    }

    //Starts the thread
    public void startConn() throws Exception{ connthread.start(); }

    //Sends data to client
    public void send(Serializable data, int playerNumber) throws Exception{ connthread.getConnectedClients().get(playerNumber).writeObject(data); }

    //Closes all client sockets
    public void closeConn() throws Exception {
        if (AllClients.isEmpty()) { }
        else { for (ClientThread s : AllClients) { s.getSocket().close(); } }
    }


    abstract protected int getPort();

    class ConnThread extends Thread{

         ServerSocket mainServer;
         private volatile int numPlayers =0;

         private HashMap<Integer,ObjectOutputStream> connectedClients = new HashMap<>();//keeps track of the connected clients

         private HashMap<Integer,Bingo> clientGames=new HashMap<>();// all the games where the playerId is the key and the game is the value


        private volatile Stack<Integer> numbersDrawn = new Stack<>();
        //Creates a server socket and an infinite loop to connect client sockets to the server
        public void run(){
            while(true) {
                //Server socket is made and listens for new clients
                try (ServerSocket server = new ServerSocket(getPort())) {
                    callback.accept("serverConnected");
                    this.mainServer=server;

                    while (true) {
                        Socket client=server.accept();
                        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                        numPlayers++;

                        System.out.println("The number of players"+ numPlayers);
                        connectedClients.put(numPlayers,output);
                        clientGames.put(numPlayers,new Bingo(numPlayers));
                        send("playerID" + " " + numPlayers,numPlayers);

                        ClientThread ct = new ClientThread(client,clientGames.get(numPlayers),callback,output,numPlayers);
                        AllClients.add(ct);


                        ct.setDaemon(true);
                        ct.start();
                        drawNumbers();

                    }
                    }
                 catch (Exception e) {
                    serverReset = false;
                    callback.accept("serverNotConnected");
                    while (!serverReset) {
                    }
                }}
        }

        public HashMap<Integer, ObjectOutputStream> getConnectedClients() {
            return connectedClients;
        }

        public HashMap<Integer, Bingo> getClientGames() {
            return clientGames;
        }

        public void drawNumbers(){
            if(connectedClients.size()==4) {
                boolean startDraw = true;
                while (startDraw) {

                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        System.out.println("Error sleeping the thread while drawing numbers");
                    }
                    Random r = new Random();
                    int drawnNumber = r.nextInt((49 - 0) + 1);
                    numbersDrawn.push(drawnNumber);
                    for(ClientThread x:AllClients){
                        x.getNumDrawn().push(drawnNumber);
                    }

                    int sendNumber = numbersDrawn.peek();
                    try {
                        for(Integer x:connectedClients.keySet())
                            send("drawn " + sendNumber,x);
                    } catch (Exception e) {
                        System.out.println("error sending number");
                    }
                    for(ClientThread x:AllClients){
                        if(x.getVerify()){
                            startDraw=false;
                            Thread.yield();
                        }
                    }

                }
            }
        }




    }
}

