
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
        imgPaths.put("Rock", "Resource/rock.png");
        imgPaths.put("Paper", "Resource/paper.png");
        imgPaths.put("Scissors", "Resource/scissors.png");
        imgPaths.put("Lizard", "Resource/lizard.png");
        imgPaths.put("Spock", "Resource/spock.png");
        imgPaths.put("tie", "Resource/tie.png");
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
