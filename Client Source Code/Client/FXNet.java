// Jakub Krzeptowski-Mucha
// CS342
// Project 3


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class FXNet extends Application {

    private NetworkConnection conn;

    private int padding = 125;
    private String ipAddress = "127.0.0.1";
    private int port = 5555;
    private int playerID = -1;


    //GUI data members
    private Text title = new Text("BINGO");
    private Text ipText = new Text("IP( enter IP )");
    private Text portText = new Text("Port( enter port )");
    private Text playerIDText = new Text("PlayerID: " );
    private Text playerPointsText = new Text("Points: ");
    private Text serverStatusText = new Text("Not connected to server");
    private TextArea messagesFromServer = new TextArea();
    private TextField ipInputField = new TextField();
    private TextField portInputField = new TextField();
    private Set<Integer> drawnNumbers = new HashSet<>();

    private MyButton bingoBoard[][] = new MyButton[5][5]; 

    private Button exitBtn = new Button("Exit");
    private Button connectServerBtn = new Button("Connect Server");
    private Button drawnHistoryBtn = new Button("Drawn History");
    private Button bingoBtn = new Button("Bingo!");
    private Button leaderBoardBtn = new Button("Leaderboard");

    private Boolean ipSet = false;
    private Boolean portSet = false;

    private int bingoDimensionLength = 75;

    private String bingoNumbersLocations = "bingo 2 2"; //2 2 is the free space



    //Creates client GUI
    private Parent createContent() {
        int textAreaSize = 150;

        messagesFromServer.setPrefHeight(textAreaSize);

        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 50));

        guiButtonsSetup();

        GridPane root = new GridPane();
        root.setGridLinesVisible(true);

        //Setting columns of grid
        ColumnConstraints col0 = new ColumnConstraints(padding);
        root.getColumnConstraints().add(col0);
        for(int i=0; i<5; i++){
            ColumnConstraints column = new ColumnConstraints(bingoDimensionLength);
            root.getColumnConstraints().add(column);
        }
        ColumnConstraints col4 = new ColumnConstraints(padding);
        root.getColumnConstraints().add(col4);

        //Setting rows of grid
        RowConstraints r0 = new RowConstraints(20);
        root.getRowConstraints().add(r0);
        RowConstraints r1 = new RowConstraints(20);
        root.getRowConstraints().add(r1);
        for(int i=0; i<8; i++){
            RowConstraints row = new RowConstraints(bingoDimensionLength);
            root.getRowConstraints().add(row);
        }

        RowConstraints serverMessageRow = new RowConstraints(150);
        root.getRowConstraints().add(serverMessageRow);

        root.setAlignment(Pos.TOP_CENTER);
        root.setHgap(5);
        root.setVgap(5);

        //setConstraints(Node child, int columnIndex, int rowIndex, int columnspan, int rowspan, HPos halignment, VPos valignment, Priority hgrow, Priority vgrow)

        //Text
        GridPane.setConstraints(title, 1, 2, 5, 2, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(playerIDText,1,0,1,1, HPos.LEFT, VPos.CENTER);
        GridPane.setConstraints(playerPointsText,1,1,1,1, HPos.LEFT, VPos.CENTER);
        GridPane.setConstraints(serverStatusText,4,2,2,1, HPos.CENTER, VPos.TOP);

        //Port and IP input fields
        GridPane.setConstraints(ipInputField, 4, 0, 2, 1, HPos.CENTER, VPos.TOP);
        GridPane.setConstraints(portInputField, 4, 1, 2, 1, HPos.CENTER, VPos.TOP);
        GridPane.setConstraints(ipText, 6, 0, 1, 2, HPos.LEFT, VPos.TOP);
        GridPane.setConstraints(portText, 6, 1, 1, 1, HPos.LEFT, VPos.TOP);


        //Main buttons
        GridPane.setConstraints(exitBtn, 1, 9, 1, 1, HPos.CENTER, VPos.BOTTOM);
        GridPane.setConstraints(leaderBoardBtn, 4, 9, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(bingoBtn, 2, 9, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(drawnHistoryBtn, 4, 9, 2, 1, HPos.CENTER, VPos.BOTTOM);
        GridPane.setConstraints(connectServerBtn, 2, 9, 2, 1, HPos.CENTER, VPos.BOTTOM);


        for(int i=0; i<5; i++) {
            for (int k = 0; k < 5; k++) {
                GridPane.setConstraints(bingoBoard[i][k].getButton(), k+1, i+4, 1, 1, HPos.CENTER, VPos.TOP);
                root.getChildren().addAll(bingoBoard[i][k].getButton());
            }
        }

        GridPane.setConstraints(messagesFromServer, 1, 10, 5, 1, HPos.CENTER, VPos.CENTER);

        root.getChildren().addAll( messagesFromServer, title, playerIDText, playerPointsText, serverStatusText);
        root.getChildren().addAll(exitBtn, connectServerBtn, drawnHistoryBtn, leaderBoardBtn, bingoBtn);
        root.getChildren().addAll(ipInputField, portInputField, ipText, portText);
        return root;
    }

        // Add images
        private void setImages(ArrayList<MyButton> buttons){
        String moveImages[] = new String[]{"Resource/one.png", "Resource/two.png", "Resource/three.png",
            "Resource/four.png","Resource/five.png","Resource/six.png","Resource/seven.jpg",
            "Resource/eight.gif","Resource/nine.jpg","Resource/ten.jpg","Resource/eleven.png",
            "Resource/twelve.gif","Resource/thirteen.png","Resource/fourteen.jpg","Resource/fifteen.jpg",
            "Resource/sixteen.png","Resource/seventeen.jpg","Resource/eighteen.png","Resource/19.jpg",
            "Resource/twenty.png","Resource/twentyone.png","Resource/twentytwo.png","Resource/twentythree.png",
            "Resource/twentyfour.png","Resource/twentyfive.png","Resource/twentysix.png","Resource/twentyseven.png",
            "Resource/twentyeight.png","Resource/twentynine.png","Resource/thirty.png","Resource/thirtyone.png",
            "Resource/thirtytwo.jpg","Resource/thirtythree.png","Resource/thirtyfour.png","Resource/thirtyfive.png",
            "Resource/thirtysix.jpg","Resource/thirtyseven.jpg","Resource/thirtyeight.png","Resource/thirtynine.png",
            "Resource/forty.jpg","Resource/fortyone.jpg","Resource/fortytwo.png","Resource/fortythree.png",
            "Resource/fortyfour.jpg","Resource/fortyfive.png","Resource/fortysix.jpg","Resource/fortyseven.png",
            "Resource/fortyeight.png","Resource/fortynine.png","Resource/fifty.png", "Resource/freeSpace.jpg"
        };
        for(int i=0; i<50; i++) {
            buttons.get(i).setImage(moveImages[i]);
            buttons.get(i).setImagePath(moveImages[i]);
        }
        
                    //An example of how to set the pictures in each MyButton in the bingo sheet
    //Sets button images to reflect the 5 moves, p1 move, p2 move and winner
/*    private void setImages(ArrayList<MyButton> buttons){
        String moveImages[] = new String[]{"Resource/rock.png", "Resource/paper.png",
                "Resource/scissors.png", "Resource/lizard.png", "Resource/spock.png"};
        for(int i=0; i<5; i++) {
            buttons.get(i).setImage(moveImages[i]);
            buttons.get(i).setImagePath(moveImages[i]);
        }
    } */

        //array of random numbers 1-50
        int[] numArray = new int[50];
        for(int i = 0; i <  50; i++) {
            numArray[i] = i;
        }
        
        // put numbers 1-50 in random order
        shuffleArray(numArray);
        }    
        
     static void shuffleArray(int[] arr){
    // If running on Java 6 or older, use `new Random()` on RHS here
    Random rnd = ThreadLocalRandom.current();
    for (int i = arr.length - 1; i > 0; i--)
    {
      int index = rnd.nextInt(i + 1);
      // Simple swap
      int a = arr[index];
      arr[index] = arr[i];
      arr[i] = a;
    }
     
    
  }

    //Setup for GUI buttons upon creation of the GUI
    private void guiButtonsSetup(){
       for(int i=0; i<5; i++){
           for( int k=0; k<5; k++){
                bingoBoard[i][k] = new MyButton("[" + i + "]" + "[" + k + "]");
                bingoBoard[i][k].setButtonText();
                bingoBoard[i][k].getButton().setMinWidth(bingoDimensionLength);
                bingoBoard[i][k].getButton().setMinHeight(bingoDimensionLength);
                bingoBoard[i][k].getButton().setDisable(true);

                //Setting default images
                //bingoBoard[i][k].setDefault();
           }
       }

        setButtonActions();

        int buttonWidth = 150;
        int buttonHeight = 11;

        exitBtn.setMaxSize(buttonWidth, 50);
        connectServerBtn.setMaxSize(buttonWidth, buttonHeight);
        drawnHistoryBtn.setMaxSize(buttonWidth, buttonHeight);
        bingoBtn.setMaxSize(buttonWidth, buttonHeight);
        leaderBoardBtn.setMaxSize(buttonWidth, buttonHeight);

        drawnHistoryBtn.setDisable(true);
        connectServerBtn.setDisable(true);
        bingoBtn.setDisable(true);
        leaderBoardBtn.setDisable(true);

    }


//An example of how to set the pictures in each MyButton in the bingo sheet
/*    //Sets button images to reflect the 5 moves, p1 move, p2 move and winner
    private void setImages(ArrayList<MyButton> buttons){
        String moveImages[] = new String[]{"Resource/rock.png", "Resource/paper.png",
                "Resource/scissors.png", "Resource/lizard.png", "Resource/spock.png"};

        for(int i=0; i<5; i++) {
            buttons.get(i).setImage(moveImages[i]);
            buttons.get(i).setImagePath(moveImages[i]);
        }

    } */



    //Sets buttons and input field action events
    private void setButtonActions(){
        
        //Exit button
        exitBtn.setOnAction( eventAction -> Platform.exit());

        //Setting port number
        portInputField.setOnAction(event -> {
            String portNum = portInputField.getText();
            try {
                port = Integer.parseInt(portNum);
                portInputField.clear();
                portText.setText("Port (" + port + ")");
                portSet = true;
                messagesFromServer.appendText("Port set\n");
            }catch(Exception e){
                messagesFromServer.appendText("Invalid port\n");
                portSet = false;
            }

            if(portSet && ipSet){  connectServerBtn.setDisable(false); }
        });

        //Setting IP
        ipInputField.setOnAction(event -> {
            String ipAdd = ipInputField.getText();
            try {
                ipAddress = ipAdd;
                ipInputField.clear();
                ipText.setText("IP (" + ipAddress + ")");
                ipSet = true;
                messagesFromServer.appendText("IP set\n");
            }catch(Exception e){
                messagesFromServer.appendText("Invalid IP\n");
            }
            if(portSet && ipSet){ connectServerBtn.setDisable(false); }
        });

        //Connect to server button
        connectServerBtn.setOnAction(actionEvent -> {
            conn = createClient(ipAddress, port);
            try{
                conn.startConn();
                connectServerBtn.setDisable(true);
            }
            catch(Exception e){ }
        });

        //Requests leaderboard from server
        leaderBoardBtn.setOnAction(actionEvent -> {
            try{ conn.send("leaderBoard"); }
            catch(Exception e){}
        });

        //Requests drawn number history from server
        drawnHistoryBtn.setOnAction( actionEvent -> {
            try{ conn.send("history");}
            catch(Exception e){ }
        });

        //Sends servo drawn number locations
        bingoBtn.setOnAction((actionEvent -> {
            String bingoMessage = bingoNumbersLocations + " end";

            try{conn.send(bingoMessage);}
            catch(Exception e){}
        }));
    }



    //Interprets instructions from server and executes them accordingly
    private void interpretData(String data) {
        String[] parsedData = data.split(" ");

        switch (parsedData[0]){
            case "playerID":
                setPlayerID(parsedData);
                break;
            case "bingoSheet":
                setBingoSheet(parsedData);
                break;
            case "waiting":
                waitingForOthers();
                break;
            case "gameReady":
                setGameReady();
                break;
            case "history":
                history(parsedData);
                break;
            case "leaderBoard":
                leaderBoard(parsedData);
                break;
            case "drawn":
                drawnNumber(parsedData);
                break;
            case "invalidBingo":
                invalidBingo();
                break;
            case "gameWinner":
                gameWinner(parsedData);
                break;
            case "newGame":
                newGame();
                break;
            case "connectionClosed":
                connectionClosed();
                break;
            default:
                break;
        }
    }



    //Disables the bingo sheet on the GUI and clears the drawnNumbers hashmap
    private void newGame(){
        messagesFromServer.setText("New game!");
        for(int i=0; i<5; i++){
            for( int k=0; k<5; k++){
                bingoBoard[i][k].getButton().setDisable(true);
            }
        }
        drawnNumbers.clear();
    }



    //Displays the game winner to the GUI
    private void gameWinner(String [] tokens) {
        int winnerID = Integer.parseInt(tokens[1]);
        String points = tokens[2];
        if(Integer.compare(winnerID, playerID) == 0) {
            messagesFromServer.setText("You win! (pts: " + points + ")");
            playerPointsText.setText("Points: " + points);
        }else {
            messagesFromServer.setText("Player " + winnerID + " wins! " + "(pts: " + points + ")");
        }
    }



    //Displays an invalid bingo message to GUI
    private void invalidBingo(){ messagesFromServer.setText("You do not have Bingo."); }



    //Displays all the players and their points to GUI
    private void leaderBoard(String [] tokens){
        messagesFromServer.setText("Leaderboard: ");
        for(int i=1; !tokens[i].equals("end"); i+=2) {
            messagesFromServer.appendText("Player " + tokens[i] + " (pts: " + tokens[i+1] + "), ");
        }
    }



    //Displays the drawn number to GUI
    private void drawnNumber(String [] tokens){
        messagesFromServer.setText("Number drawn: " + tokens[1]);
        drawnNumbers.add(Integer.parseInt(tokens[1]));
    }



    //Sets the bingo sheet on the GUI
    private void setBingoSheet(String [] tokens){
        int a = 1;
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if((i == 2) && (j == 2)){
                    int x = i;
                    int y = j;
                    bingoBoard[i][j].setButtonText("FREE");
                    bingoBoard[i][j].getButton().setOnAction( actionEvent -> {
                        bingoBoard[x][y].getButton().setDisable(true);
                    });
                }else{
                    int num = Integer.parseInt(tokens[a]);
                    bingoBoard[i][j].setButtonText(tokens[a]);
                    a++;

                    int x = i;
                    int y = j;

                    EventHandler<ActionEvent> selectNummber = new EventHandler<ActionEvent>(){
                        public void handle(ActionEvent event){
                            int bingoNumber = num;
                            String xCoord = Integer.toString(x); // x location on bingo sheet
                            String yCoord= Integer.toString(y);;  // y location on bingo sheet
                            //System.out.println("Bingo number: " + bingoNumber);

                            if(drawnNumbers.contains(bingoNumber)){
                                Button b = (Button)event.getSource();
                                b.setDisable(true);
                                //adding the number location to the string that will be sent to server upon Bingo
                                bingoNumbersLocations = bingoNumbersLocations + " " + xCoord + " " + yCoord;

                            }
                            else{
                                messagesFromServer.setText("Number " + num + " was not drawn.");
                            }
                        }
                    };
                    bingoBoard[i][j].getButton().setOnAction(selectNummber);
                }
            }
        }
    }



    //If there are less than 4 players, client will be told to wait
    private void waitingForOthers(){ messagesFromServer.setText("Waiting for more players..."); }



    //Sets the playerID
    private void setPlayerID(String [] tokens) {
        serverStatusText.setText("Server connected");
        playerID = parseInt(tokens[1]);
        playerIDText.setText("PlayerID: " + playerID);
        playerPointsText.setText("Points: 0");

    }



    //Displays the drawn number history to GUI
    private void history(String [] tokens){
        messagesFromServer.setText( "Drawn number history: ");
        for(int i=1; !tokens[i].equals("end"); i++) {
            messagesFromServer.appendText(tokens[i] + ", ");
        }
    }



    //Enables bingo sheet and notifies the client that the game has begun
    private void setGameReady(){
        for(int i=0; i<5; i++) {
            for (int k = 0; k < 5; k++) {
                bingoBoard[i][k].getButton().setDisable(false);
            }
        }

        drawnHistoryBtn.setDisable(false);
        leaderBoardBtn.setDisable(false);
        bingoBtn.setDisable(false);
        messagesFromServer.setText("The game has begun!");

    }



    //Updates GUI to indicate server is closed
    private void connectionClosed(){
        messagesFromServer.setText("Server connection closed ");
        connectServerBtn.setDisable(false);
    }



    //Launches program
    public static void main(String[] args) { launch(args); }



    //Creates client GUI
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }



    //Closed connections, used when terminating program
    @Override
    public void stop() throws Exception {
        try{  conn.closeConn(); }
        catch(Exception e){ }
    }



    //Creates a connection to a server with an ip and port number set in parameters
    private Client createClient(String ip, int portNum){
        return new Client(ip, portNum, data -> {
            Platform.runLater(()-> {
                String serverData = data.toString();
                System.out.println("Server: " + serverData);
                interpretData(serverData);
            });
        });
    }

}
