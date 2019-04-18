import javafx.scene.control.Button;

public class Bingo {


}

class BingoButton extends Button {//extends button so it's easier for the GUI team to develop the board
    private String number;//could possibly be an int too

    BingoButton(String s) {
        this.number = s;

    }

    public String getNumber() {
        return this.number;
    }
}
