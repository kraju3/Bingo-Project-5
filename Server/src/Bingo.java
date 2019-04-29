import java.util.Random;

public class Bingo {
    private int playerBoard[][] = new int[5][5];



    private int checkBingo [][] = new int[5][5];

    private int playerID;

    private String bingoSheet = "";

        //String diagonal1 = "bingo 0 0 1 1 2 2 3 3 4 4 end"; //Bingo for diagonal of top left to bottom right
        //String diagonal2 = "bingo 0 4 1 3 2 2 3 1 4 0 end"; //Bingo for diagonal of top right to bottom left
        //String row = "bingo 0 0 0 1 0 2 0 3 0 4 end";       //Bingo for row0
        //String column = "bingo 3 0 3 1 3 2 3 3 3 4 end";    //Bingo for column3
        //String bingoBoard = "bingo 3 0 1 1 1 4 3 3 3 4 end";  //As is set no bingo
        //String[] parsedBoard = bingoBoard.split(" ");


        Bingo(int ID){
           //this function sets the playerboard for each client

            this.playerID=ID;
            setBoard();

            initCheck();
        }

        public void initCheck(){
            int i=0,j=0;
            for(i =0;i<5;i++){
                for( j=0;j<5;j++){
                    if(i==2&&j==2){checkBingo[i][j]=1;}
                    else{checkBingo[i][j]=0;}
                }
            }
        }
        public void setBoard(){
        int i=0,j=0;
        bingoSheet=bingoSheet.concat("bingoSheet ");
        for(i =0;i<5;i++){

            for( j=0;j<5;j++){
                if(i==2&&j==2){playerBoard[i][j]=1;}
                else{playerBoard[i][j]=generateRandomIntIntRange(0,151);
                 bingoSheet=bingoSheet.concat(playerBoard[i][j]+" ");
                }
            }
        }
        //bingoSheet=bingoSheet.concat("end");

        }
        public static int generateRandomIntIntRange(int min, int max) {
            Random r = new Random();
            return r.nextInt((max - min) + 1) + min;
        }
    public int[][] getPlayerBoard() {
        return playerBoard;
    }

    public int[][] getCheckBingo() {
        return checkBingo;
    }

    public String getBingoSheet() {
        return bingoSheet;
    }

    // Checks every possibility of bingo on the sheet. The rows, columns and diagonals will be individually added up.
    //  If their sum is 5 then there is a drawn number in every element along that path and therefore bingo.
    public boolean verifyBingo(int [][] pBoard){
        //Checking rows
        for(int i=0; i<5; i++){
            int sum = 0;
            for(int j=0; j<5; j++){ sum+= pBoard[i][j]; }
            if(sum == 5){
                System.out.println("Bingo on row " + i);
                return true;
            }
        }

        //Checking columns
        for(int i=0; i<5; i++){
            int sum = 0;
            for(int j=0; j<5; j++){ sum+= pBoard[j][i]; }
            if(sum == 5){
                System.out.println("Bingo on column " + i);
                return true;
            }
        }

        //Checking diagonals
        int diagonal1 = pBoard[0][0] + pBoard[1][1] + pBoard[2][2] + pBoard[3][3] + pBoard[4][4];
        int diagonal2 = pBoard[0][4] + pBoard[1][3] + pBoard[2][2] + pBoard[3][1] + pBoard[4][0];

        if(diagonal1 == 5){
            System.out.println("Diagnonal1 (top left to bottom right) bingo");
            return true;
        }
        if(diagonal2 == 5){
            System.out.println("Diagonal2 (top right to bottom left) bingo");
            return true;
        }

        return false;
    }

    // Parses the bingo instruction sent from the client and inputs each coordinate into a 2d array. If a drawn number
    // is present at a coordinate then there is a 1, else 0.
    public void parseBingo(String[] tokens, int [][] playerBoard){
        for(int i=1; !tokens[i].equals("end"); i+=2) {
            int x = Integer.parseInt(tokens[i]);
            int y = Integer.parseInt(tokens[i+1]);
            playerBoard[x][y] = 1;
        }
    }

    //Displays the bingo board of the locations of the drawn numbers to the console
    public void printBoard(int [][] playerBoard){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                System.out.print(playerBoard[i][j] + " ");
            }
            System.out.println();
        }
    }


}

