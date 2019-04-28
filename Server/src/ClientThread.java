import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;



import java.io.ObjectInputStream;

public class ClientThread extends Thread{
    private Socket socket;
    private ObjectOutputStream out;
    private Bingo serverGame;
    private Consumer<Serializable> callback;


    private static int players = 0;
    private int playerNumber;

   //private static volatile hashmap for all players
    private static volatile HashMap<Integer,Boolean> connections=new HashMap<>();
    private static volatile HashMap<Integer,ObjectOutputStream> outputClient=new HashMap<>();




    ClientThread(Socket s, Bingo game, Consumer<Serializable> callback) throws IOException {
        this.socket = s;
        this.out=new ObjectOutputStream(this.socket.getOutputStream());
        this.serverGame = game;
        this.callback = callback;
        players++;
        playerNumber = players;
        connections.put(playerNumber,true);
        outputClient.put(playerNumber,out);
    }

    //Returns socket connected to server
    public Socket getSocket(){ return this.socket; }

    //Sends data from server to client
    public void send(Serializable data,ObjectOutputStream out) throws Exception{ out.writeObject(data); }

    //Sets player one and player two. Once both are connected, will listen for any input from clients
    public void run(){
        try(
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){

            socket.setTcpNoDelay(true);

            if(connections.size()<4){
                try{
                    send("waiting",outputClient.get(connections.size()));}// this will send it to the client according to the
                    catch (Exception ex) { }
                    callback.accept("playerConnected "+playerNumber);
            }



            while(connections.size()>=4) {
                try {
                    outputClient.values().forEach(e->{
                        try {
                            send("gameReady",e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    });
                    while (true) {
                        Serializable data = (Serializable) in.readObject();
                        //getInput(data.toString());
                    }}
                    catch(Exception e){
                        System.out.println(e);
                    }
                }

        } catch(Exception e){  }
    }


}

