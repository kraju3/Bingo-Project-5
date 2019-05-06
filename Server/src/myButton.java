
// Jakub Krzeptowski-Mucha
// CS342
// Project 3


import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class myButton {
    private String name;
    private Button button = new Button();
    private String imagePath = "";

    private static HashMap<String, String> imgPaths = new HashMap<>();

    static{
        imgPaths.put("1", "Resource/one.png");
        imgPaths.put("2", "Resource/two.png");
        imgPaths.put("3", "Resource/three.png");
        imgPaths.put("4", "Resource/four.png");
        imgPaths.put("5", "Resource/five.png");
        imgPaths.put("6", "Resource/six.png");
        imgPaths.put("7", "Resource/seven.jpg");
        imgPaths.put("8", "Resource/eight.gif");
        imgPaths.put("9", "Resource/nine.jpg");
        imgPaths.put("10", "Resource/ten.jpg");
        imgPaths.put("11", "Resource/eleven.png");
        imgPaths.put("12", "Resource/twelve.gif");
        imgPaths.put("13", "Resource/thirteen.png");
        imgPaths.put("14", "Resource/fourteen.jpg");
        imgPaths.put("15", "Resource/fifteen.jpg");
        imgPaths.put("16", "Resource/sixteen.png");
        imgPaths.put("17", "Resource/seventeen.jpg");
        imgPaths.put("18", "Resource/eighteen.png");
        imgPaths.put("freeSpace", "Resource/freeSpace.jpg");
        imgPaths.put("20", "Resource/twenty.png");
        imgPaths.put("21", "Resource/twentyone.png");
        imgPaths.put("22", "Resource/twentytwo.png");
        imgPaths.put("23", "Resource/twentythree.png");
        imgPaths.put("24", "Resource/twentyfour.png");
        imgPaths.put("25", "Resource/twentyfive.png");
        imgPaths.put("26", "Resource/twentysix.png");
        imgPaths.put("27", "Resource/twentyseven.png");
        imgPaths.put("28", "Resource/twentyeight.png");
        imgPaths.put("29", "Resource/twentynine.png");
        imgPaths.put("30", "Resource/thirty.png");
        imgPaths.put("31", "Resource/thirtyone.png");
        imgPaths.put("32", "Resource/thirtytwo.jpg");
        imgPaths.put("33", "Resource/thirtythree.png");
        imgPaths.put("34", "Resource/thirtyfour.png");
        imgPaths.put("35", "Resource/thirtyfive.png");
        imgPaths.put("36", "Resource/thirtysix.jpg");
        imgPaths.put("37", "Resource/thirtyseven.jpg");
        imgPaths.put("38", "Resource/thirtyeight.png");
        imgPaths.put("39", "Resource/thirtynine.png");
        imgPaths.put("40", "Resource/forty.jpg");
        imgPaths.put("41", "Resource/fortyone.jpg");
        imgPaths.put("42", "Resource/fortytwo.png");
        imgPaths.put("43", "Resource/fortythree.png");
        imgPaths.put("44", "Resource/fortyfour.jpg");
        imgPaths.put("45", "Resource/fortyfive.png");
        imgPaths.put("46", "Resource/fortysix.jpg");
        imgPaths.put("47", "Resource/fortyseven.png");
        imgPaths.put("48", "Resource/fortyeight.png");
        imgPaths.put("49", "Resource/fortynine.png");
        imgPaths.put("50", "Resource/fifty.png");
        imgPaths.put("19", "Resource/19.jpg");
    }

    myButton(String name){
        this.name = name;
    }

    public void setButtonText(){ button.setText(this.name);}

    public void setButtonText(String name){ button.setText(name);}

    //Sets image path for button to be set
    public void setImagePath(String path){ imagePath = path; }

    //Returns image path
    public String getImagePath(){ return imagePath; }

    //Returns button
    public Button getButton(){ return button; }

    //Returns name
    public String getName(){ return name; }

    //Returns corresponding image path from a player move
    public String getPathFromName(String moveName){ return imgPaths.get(moveName); }

    //Sets button image
    public void setImage(String path){
        ImageView v = new ImageView(path);
        v.setFitHeight(70);
        v.setFitWidth(70);
        v.setPreserveRatio(true);
        button.setGraphic(v);
    }

    //Resets button image to be blank
    public void setDefault(){
        this.setImage("Resource/white.png");
    }

}
