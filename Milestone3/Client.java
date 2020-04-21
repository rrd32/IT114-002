import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
/** Sources *
 * http://campus.murraystate.edu/academic/faculty/wlyle/325/ch33.pdf
 * https://personal.utdallas.edu/~dheroy/4331code/book
 */
public class Client extends JApplet implements Runnable {

    Socket socket;
    private char connection = ' ';
    private char otherConnect = ' ';
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private JLabel myLabel = new JLabel();
    private JLabel LabelStat = new JLabel();
    private Block [][] cell = new Block[3][3];
    private int onRow;
    private int onCol;
    private boolean yourTurn = false;
    private boolean gameCont = true;
    private boolean IdleStat = true;
    private boolean isStandAlone = false;

    public void init() {
        JPanel mypanel = new JPanel();
        mypanel.setLayout(new GridLayout(3,3,0,0));
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3;j ++)
            mypanel.add(cell[i][j] = new Block(i,j));

        mypanel.setPreferredSize(new Dimension(640, 480));
        mypanel.setBorder(new LineBorder(Color.black, 1));
        myLabel.setHorizontalAlignment(JLabel.CENTER);
        myLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        myLabel.setBorder(new LineBorder(Color.black, 1));
        LabelStat.setBorder(new LineBorder(Color.black, 1));

        add(myLabel, BorderLayout.NORTH);
        add(mypanel, BorderLayout.CENTER);
        add(LabelStat,BorderLayout.SOUTH);


        try {
            //if it is standalone connect to the localhost
            if (isStandAlone)
                socket = new Socket("localhost", 8000);
            else
                socket = new Socket(getCodeBase().getHost(), 8000);

            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            System.err.println(ex);
        }

        Thread thread = new Thread(this);
        thread.start();
    }

     //  Draw Board, Blocks & Paint UI
     public class Block extends JPanel {
        private int row, column;

        private char token = ' ';

        public Block (int row, int column) {
            this.row = row;
            this.column = column;
            setBorder(new LineBorder(Color.black, 1));
            addMouseListener(new ClickListener());
        }

        public char getToken() {
            return token;
        }

        public void setToken(char c) {
            token = c;
            repaint();
        }

        //draw the tokens X and Y on the applet
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (token == 'X') {
                g.drawLine(10, 10, getWidth() - 10, getHeight() - 10);
                g.drawLine(getWidth() - 10, 10, 10, getHeight() - 10);
            }
            else if (token == 'O') {
                g.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
            }
        }

        private class ClickListener extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((token == ' ') && yourTurn) {
                    setToken(connection);
                    yourTurn = false;
                    onRow = row;
                    onCol = column;
                    LabelStat.setText("Waiting for the other player to move");
                    IdleStat = false;
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            int player = fromServer.readInt();

            if(player == 1) {
                connection = 'X';
                otherConnect = 'O';
                myLabel.setText("Player 1 connected");
                LabelStat.setText("Waiting for second player");

                //notification that player 2 joined
                fromServer.readInt();

                LabelStat.setText("Player 2 joined, Your Turn");

                yourTurn = true;
            }
            //if second player then game can start
            else if (player == 2) {
                connection = 'O';
                otherConnect = 'X';
                myLabel.setText("Player 2 connected");
                LabelStat.setText("Waiting for other players move");
            }

            while (gameCont) {
                if (player == 1) {
                    while (IdleStat) {
                        Thread.sleep(100);
                    }
                    IdleStat = true;
                    toServer.writeInt(onRow);
                    toServer.writeInt(onCol);
                    serverStatus();
                }
                else if (player == 2) {
                    serverStatus();
                    while (IdleStat) {
                        Thread.sleep(100);
                    }
                    IdleStat = true;
                    toServer.writeInt(onRow);
                    toServer.writeInt(onCol);
                }
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        } catch (InterruptedException ex) {}
    }

    // Player 1 else Player 2 else Draw else Continue....
    private void serverStatus() throws IOException {
        int status = fromServer.readInt();
        if (status == 1) {
            gameCont = false;
            if (connection == 'X') {
                LabelStat.setText("I Won! (X)");
            }
            else if (connection == 'O') {
                LabelStat.setText("Player 1 (X) has won!");
                int row = fromServer.readInt();
                int column = fromServer.readInt();
                cell[row][column].setToken(otherConnect);
            }
        }
        else if (status == 2) {
            gameCont = false;
            if (connection == 'O') {
                LabelStat.setText("I Won! (O)");
            }
            else if (connection == 'X') {
                LabelStat.setText("Player 2 (O) has won!");
                int row = fromServer.readInt();
                int column = fromServer.readInt();
                cell[row][column].setToken(otherConnect);
            }
        }
        else if (status == 3) {
            gameCont = false;
            LabelStat.setText("Game Draw");

            if (connection == 'O') {
                int row = fromServer.readInt();
                int column = fromServer.readInt();
                cell[row][column].setToken(otherConnect);
            }
        }
        else {
            int row = fromServer.readInt();
            int column = fromServer.readInt();
            cell[row][column].setToken(otherConnect);
            LabelStat.setText("Your turn");
            yourTurn = true;
        }
    }
	  public static void main(String[] args) {
	    // Create a frame
	    JFrame frame = new JFrame("Applet Client");
	    Client applet = new Client();
	    applet.isStandAlone = true;

        String host = "localhost";

	    // Get host
	    if (args.length == 1) host = args[0];

	    // Add the applet instance to the frame
	    frame.add(applet, java.awt.BorderLayout.CENTER);

	    // Invoke init() and start()
	    applet.init();
	    applet.start();

	    // Display the frame
	    frame.pack();
        frame.setVisible(true);
  }
}
