import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Client extends NetworkConnection {

    private String ip;
    private int port;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public Client(String ip, int port, Consumer<Serializable> callback){
        super(callback);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected String getIP() {
        //TODO Auto-generated constructor stub
        return this.ip;
    }

    @Override
    protected  int getPort() {
        //TODO Auto-generated constructor stub
        return this.port;
    }
}