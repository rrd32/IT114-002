import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
/** Sources *
 * http://campus.murraystate.edu/academic/faculty/wlyle/325/ch33.pdf
 * https://personal.utdallas.edu/~dheroy/4331code/book
 */
public class Server extends JFrame {
        public static void main(String[] args) {
        Server display = new Server();
    }

    public Server() {
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        setSize(500, 500);
        setTitle("Server for TicTacToe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            ServerSocket serverSocket = new ServerSocket(8000);

            while (true) {
                textArea.append("Waiting for players to join session\n");

                Socket playerOne = serverSocket.accept();
                Socket playerTwo = serverSocket.accept();

                textArea.append("Player 1 joined lobby \n");
                new DataOutputStream(playerOne.getOutputStream()).writeInt(1);

                textArea.append("Player 2 joined lobby \n");
                new DataOutputStream(playerTwo.getOutputStream()).writeInt(2);

                textArea.append("Starting a new lobby for the two players connected.");
                Lobby task = new Lobby(playerOne, playerTwo);
                Thread lobby = new Thread(task);
                lobby.start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

}

