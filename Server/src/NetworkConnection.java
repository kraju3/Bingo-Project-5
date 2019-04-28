// Jakub Krzeptowski-Mucha
// CS342
// Project 3


import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public abstract class NetworkConnection {

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;

    private static volatile Boolean serverReset = false;

    public void setServerReset(Boolean bool){ this.serverReset = bool; }

    public NetworkConnection(Consumer<Serializable> callback){
        this.callback = callback;
        connthread.setDaemon(true);
    }

    //Starts the thread
    public void startConn() throws Exception{ connthread.start(); }

    //Sends data to client
    public void send(Serializable data, ObjectOutputStream out) throws Exception{ out.writeObject(data); }

    //Closes all client sockets
    public void closeConn() throws Exception {
        if (connthread.connectedClients.isEmpty()) { }
        else { for (Socket s : connthread.connectedClients.keySet()) { s.close(); } }
    }


    abstract protected int getPort();

    class ConnThread extends Thread{
        private HashMap<Socket,ObjectOutputStream> connectedClients = new HashMap<>();//keeps track of the connected clients

        private HashMap<Integer,Bingo> clientGames=new HashMap<>();// all the games where the playerId is the key and the game is the value

        private volatile int numPlayers =0;



        //Creates a server socket and an infinite loop to connect client sockets to the server
        public void run(){
            while(true) {
                //Server socket is made and listens for new clients
                try (ServerSocket server = new ServerSocket(getPort())) {
                    callback.accept("serverConnected");
                    while (true) {
                        Socket client=server.accept();
                        numPlayers++;

                        connectedClients.put(client,new ObjectOutputStream(client.getOutputStream()));
                        clientGames.put(numPlayers,new Bingo(numPlayers));

                        send("playerID "+numPlayers,connectedClients.get(numPlayers));
                        ClientThread ct = new ClientThread(client,clientGames.get(numPlayers),callback);
                        ct.start();

                        if(connectedClients.size()>=4){
                            connectedClients.values().forEach(e->{
                                try {
                                    send("gameReady",e);
                                } catch (Exception e1) {
                                    System.out.println("Error sending instruction for gameReady to client-//NetworkConnection");
                                }
                            });
                        }

                        callback.accept("newClient");
                    }
                } catch (Exception e) {
                    serverReset = false;
                    callback.accept("serverNotConnected");
                    while(!serverReset){}
                }
            }
        }
    }
}