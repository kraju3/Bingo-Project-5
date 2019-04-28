// Jakub Krzeptowski-Mucha
// CS342
// Project 3


import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends NetworkConnection {
    private volatile static int port;
    private volatile static String IP;

    public Server(int port, Consumer<Serializable> callback) {
        super(callback);
        //TODO Auto-generated constructor stub
        this.port = port;
    }

    @Override
    protected int getPort() {
        //TODO Auto-generated constructor stub
        return port;
    }


}