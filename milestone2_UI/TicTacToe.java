import java.awt.font.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {

	private static JButton buttons[] = new JButton[9];

	private static int[][] combinationAccepted = new int[][] {
		{0, 4, 8}, {2, 4, 6},
		{0, 3, 6}, {1, 4, 7}, {2, 5, 8},
		{0, 1, 2}, {3, 4, 5}, {6, 7, 8}
	};


	public static int xoCounter=0;
	private static class MyButton extends JButton implements ActionListener {

		boolean gameOver=false;
		String letter;

		public MyButton()
		{
			super();
			letter=" ";

			setText(letter);
			addActionListener(this);
		}
		public void actionPerformed(ActionEvent e) {
			if((xoCounter%2)==0 && getText().equals(" ") && gameOver==false){
				letter="X";
				xoCounter=xoCounter+1;
				System.out.println(letter + "\n"+xoCounter);
			} else if((xoCounter%2)==1 && getText().equals(" ") && gameOver==false) {
				letter="O";
				xoCounter=xoCounter+1;
				System.out.println(letter + "\n"+xoCounter);
			}

			setText(letter);
			for(int i=0; i<=7; i++){
				if( buttons[combinationAccepted[i][0]].getText().equals(buttons[combinationAccepted[i][1]].getText()) &&
					buttons[combinationAccepted[i][1]].getText().equals(buttons[combinationAccepted[i][2]].getText()) &&
					buttons[combinationAccepted[i][0]].getText() != " ")
					{
						gameOver = true;
					}
			}

			if(gameOver == true){
				System.exit(0);
			}
		}

	}

	public static void main (String[] args)
	{

		JFrame frame = new JFrame ("Server-Client [Tic Tac Toe]");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);


		JPanel panel = new JPanel();
		panel.setLayout (new GridLayout (3, 3));
		panel.setBorder (BorderFactory.createLineBorder (Color.white, 3));
		panel.setBackground (Color.black);


		for(int i=0; i<=8; i++){
			buttons[i] = new MyButton();
			panel.add(buttons[i]);
		}

		frame.pack();
		frame.setVisible(true);
		frame.getContentPane().add (panel);
		frame.setSize(300, 300);


	}

}