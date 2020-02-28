import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class SampleSocketServerPart1
{
	int port = 3002;
	public void start(int port) throws IOException
	{
		this.port = port;
		System.out.println("Waiting for client");

		ServerSocket serverSocket = new ServerSocket(port);
        while(true)
        {
			Socket client = null;
			try{
				//Accepts multiple clients via Thread Class defined below
				client = serverSocket.accept();
				System.out.println("new client :)");
				CThread clientServer = new CThread(client, serverSocket);
				clientServer.run();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
        }
	}
	private static class CThread extends Thread
	{
		Socket s = null;
		ServerSocket serverSocket = null;
		PrintWriter out;
		BufferedReader in;
		//Scanner keyboard = new Scanner(System.in);

		CThread(Socket s, ServerSocket serverSocket)throws IOException
		{
			this.s = s;
			this.serverSocket = serverSocket;
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			System.out.println("Client connected, waiting for message");
		}

		@Override
		public void run()
		{
			try{
				String fromClient = "";
				String toClient = "";
				while ((fromClient = in.readLine()) != null) {
					if ("kill server".equalsIgnoreCase(fromClient)) {
						System.out.println("Client killed server");
						break;
					}
					else {
						System.out.println("From client: " + fromClient);
					}
				}
				s.close();
				in.close();
				out.close();
				serverSocket.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args)throws IOException {
		System.out.println("Starting Server");

		SampleSocketServerPart1 server = new SampleSocketServerPart1();
		server.start(3002);
		System.out.println("Server Stopped");
	}
}