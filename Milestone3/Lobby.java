import java.io.*;
import java.net.*;
/** Sources *
 * http://campus.murraystate.edu/academic/faculty/wlyle/325/ch33.pdf
 * https://personal.utdallas.edu/~dheroy/4331code/book
 */
class Lobby implements Runnable {
    private Socket playerOne;
    private Socket playerTwo;
   
    // 2D Array for our tictactoe game values
    private char[][] cell = new char[3][3];

    // TicTacToe players + Board
    public Lobby(Socket playerOne, Socket playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cell[i][j] = ' ';
            }
        }
    }

    private boolean gameStatus(char token) {
        if ((cell[0][0] == token) && (cell[1][1] == token) && (cell[2][2] == token)){
            return true;
        }
        if ((cell[0][2] == token) && (cell[1][1] == token) && (cell[2][0] == token)){
            return true;
        }
        for (int i = 0; i < 3; i++) {
            if ((cell[i][0] == token) && (cell[i][1] == token) && (cell[i][2] == token)) {
                return true;
            }
        }
        for (int j = 0; j < 3; j++) {
            if ((cell[0][j] == token) && (cell[1][j] == token) && (cell[2][j] == token))
                return true;
        }
        return false;
    }

    private boolean gameFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cell[i][j] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void run() {
        try {
            
            // For communicating with message to and receiving back from Server

            // For player 1:
            DataInputStream fromPlayer1 = new DataInputStream(playerOne.getInputStream());
            DataOutputStream toPlayer1 = new DataOutputStream(playerOne.getOutputStream());
            
            // For player 2:
            DataInputStream fromPlayer2 = new DataInputStream(playerTwo.getInputStream());
            DataOutputStream toPlayer2 = new DataOutputStream(playerTwo.getOutputStream());

            //Player Joined:
            toPlayer1.writeInt(1);   

            //Start the game
            while (true) 
            {
                // Check if player 1 has won
                // Else -> Full
                // Check if player 2 has won
                // Else -> Full
                int row = fromPlayer1.readInt();
                int column = fromPlayer1.readInt();
                cell[row][column] = 'X';
                boolean player1Status = gameStatus('X');

                if (player1Status) {
                    toPlayer1.writeInt(1);
                    toPlayer2.writeInt(1);
                    DataOutputStream out = new DataOutputStream(toPlayer2);
                    out.writeInt(row);
                    out.writeInt(column);
                    break;
                //full?
                } else if (gameFull()) {
                    toPlayer1.writeInt(3);
                    toPlayer2.writeInt(3);
                    DataOutputStream out = new DataOutputStream(toPlayer2);
                    out.writeInt(row);
                    out.writeInt(column);
                    break;
                //Player 1's turn
                } else {
                    toPlayer2.writeInt(4);
                    DataOutputStream out = new DataOutputStream(toPlayer2);
                    out.writeInt(row);
                    out.writeInt(column);
                }

                row = fromPlayer2.readInt();
                column = fromPlayer2.readInt();
                cell[row][column] = 'O';
                boolean player2Status = gameStatus('O');

                //check if second player won
                if (player2Status) {
                    // Write player 2 won
                    toPlayer1.writeInt(2);
                    toPlayer2.writeInt(2);
                    DataOutputStream out = new DataOutputStream(toPlayer1);
                    out.writeInt(row);
                    out.writeInt(column);
                    break;
                //Second players turn
                } else {
                    toPlayer1.writeInt(4);
                    DataOutputStream out = new DataOutputStream(toPlayer1);
                    out.writeInt(row);
                    out.writeInt(column);
                }
            }
        } catch (IOException e) {
            System.err.println("e");
        }
    }
}
