import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;

class BingoTest {
    Bingo game1;
    Bingo game2;
    Bingo game3;

    @BeforeEach
    void init(){
        game1=new Bingo(1);game2=new Bingo(2);game3=new Bingo(3);
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
        BlockingDeque<Integer> queue = new LinkedBlockingDeque<>();
        TimerTask task = new TimerTask() {
            public void run() {
                Random r = new Random();
                try {
                    queue.put(r.nextInt((151 - 0) + 1) + 0);
                } catch (InterruptedException e) {
                    System.out.println("error trying to send random numbe");
                }
                // or use whatever method you chose to generate the numbe
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task, 0, 2000);
        System.out.println(queue.getLast());



    }
}