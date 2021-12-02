import java.io.*;
import java.net.*;

public class BankClient_C {

	public static void main(String[] args) throws IOException
	{
		//Client Socket Setup
		Socket BCSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		int ServerPort = 4444;
		String ServerID  = "localhost";//ID of Server. localhost is used for testing on the same machine
		String ClientID = "BankClient_C";
		
		//Perform connection to Server
		try
		{
			BCSocket = new Socket(ServerID, ServerPort);
			out = new PrintWriter(BCSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(BCSocket.getInputStream()));
		}
		//Errors found during connection attempt
		catch(UnknownHostException e)
		{
			System.err.println("Don't know about host: " + ServerID);
			System.exit(1);
		}
		catch (IOException e)
		{
			System.err.println("Could not get I/O for the connection to port " + ServerPort);
			System.exit(1);
		}

		//Client INPUT setup
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromServer, fromUser;
		
		//Connection successful message
		System.out.println("Initialized " + ClientID + " program client and IO connections");
		
		//Managing the communication with the Server
		while (true)
		{
			//Read user's INPUT
			fromUser = stdIn.readLine();
			if(fromUser != null)
			{
				//Send MESSAGE to server
				System.out.println(ClientID + ": sending message to BankServer...");
				out.println(fromUser);
			}
			//Receive server's MESSAGE
			fromServer = in.readLine();
			System.out.println("Server: " + fromServer + " [message received from BankServer]");
		}
		
		/* This is not run as while loop is always running
		out.close();
		in.close();
		stdIn.close();
		BCSocket.close();
		*/
	}

}
