// Jakub Krzeptowski-Mucha
// CS342
// Project 3


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnection {

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;

    public NetworkConnection(){
        connthread.setDaemon(true);
    }

    public NetworkConnection(Consumer<Serializable> callback){
        this.callback = callback;
        connthread.setDaemon(true);
    }

    //Starts the thread to connect to the server
    public void startConn() throws Exception{ connthread.start(); }

    //Sends data from the client to the server
    public void send(Serializable data) throws Exception{ connthread.out.writeObject(data); }

    //Closes the socket connected to the server
    public void closeConn() throws Exception {
        if (connthread.socket == null) { ;}
        else{connthread.socket.close();}
    }

    abstract protected String getIP();
    abstract protected int getPort();

    class ConnThread extends Thread{
        private Socket socket;
        private ObjectOutputStream out;

        //Creates a socket to connect to a server. Once connected will listen for output from the server
        public void run(){
            try(Socket socket = new Socket(getIP(), getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {


                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                //Reading in data from server
                while(true){
                    Serializable fromServerData = (Serializable) in.readObject();
                    callback.accept(fromServerData);
                }
            }catch(Exception e) { callback.accept("connectionClosed"); }
        }
    }
}