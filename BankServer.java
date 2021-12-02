import java.io.*;
import java.net.*;


public class BankServer {

	public static void main(String[] args) throws IOException
	{		
		//Server setup variables
		ServerSocket BSSocket = null; //Socket allows incoming connections to communicate with the end server
		boolean listening = true;
		String ServerName = "WLFB Bank Server"; //Name of this server
		int ServerPort = 4444; //Port to be used in Socket
		
		double startAccount = 1000; //Client Accounts Starting Balance
		
		//Create Shared Object
		BankServerState sharedBankState = new BankServerState(startAccount);
		
		//Initialise Server Socket
		try
		{
			BSSocket = new ServerSocket(ServerPort);
		}
		//Startup Error
		catch(IOException e)
		{
			System.err.println("Could not start " + ServerName + "on port " + ServerPort);
			System.exit(1);
		}
		
		//Server Setup successful message
		System.out.println(ServerName + " is now running.");
		
		while(listening)
		{
			//Create three new Threads (one for each client) and initialize them
			//EXAMPLE: new BankServerThread(socket.accept(), "[Thread name HERE]", [Shared object reference HERE], Thread Number).start;
			
			new BankServerThread(BSSocket.accept(), 'A', sharedBankState, 0).start();
			new BankServerThread(BSSocket.accept(), 'B', sharedBankState, 1).start();
			new BankServerThread(BSSocket.accept(), 'C', sharedBankState, 2).start();
		}
		//close the Server
		BSSocket.close();
	}
}
