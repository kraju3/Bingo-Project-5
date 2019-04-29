

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXNet extends Application{


    private NetworkConnection  conn = createServer();
    private TextArea messages = new TextArea();

    private Parent createContent() {
        messages.setPrefHeight(550);
        TextField input = new TextField();

        input.setOnAction(event -> {
            String message = input.getText();
            input.clear();

            messages.appendText("server: " + message + "\n");
            try {
                conn.send(message);
            }
            catch(Exception e) {

            }

        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);

        return root;



    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();

    }

    @Override
    public void init() throws Exception{
        conn.startConn();
    }

    @Override
    public void stop() throws Exception{
        conn.closeConn();
    }

    private Server createServer() {
        return new Server(5555, data-> {
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
            });
        });
    }


}
