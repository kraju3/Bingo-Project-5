import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BingoTest {
    Bingo game1;
    Bingo game2;
    Bingo game3;

    @BeforeEach
    void init(){
        game1=new Bingo();game2=new Bingo();game3=new Bingo();
    }

    @Test
    void initCheck() {
        game1.printBoard(game1.getCheckBingo());
        game2.printBoard(game2.getCheckBingo());
        game3.printBoard(game3.getCheckBingo());
        assertTrue(game1.getCheckBingo()[2][2]==1);
        assertTrue(game2.getCheckBingo()[2][2]==1);
        assertTrue(game3.getCheckBingo()[2][2]==1);
    }

    @Test
    void setBoard() {
        System.out.println("Game 3 Board");
        System.out.println();
        game3.printBoard(game3.getPlayerBoard());
        System.out.println();
        System.out.println("Game 2 Board");
        game2.printBoard(game2.getPlayerBoard());
        System.out.println();
        System.out.println("Game1 Board");
        game1.printBoard(game1.getPlayerBoard());


        System.out.println();

        System.out.println("Game 1 sheet: "+game1.getBingoSheet());
        System.out.println("Game 2 sheet: "+game2.getBingoSheet());
        System.out.println("Game 3 sheet: "+game3.getBingoSheet());

    }

    @Test
    void generateRandomIntIntRange() {
    }
}