import java.io.*;
import java.net.*;

public class BankServerThread extends Thread 
{
	//Thread Variables
	private Socket BThreadSocket = null;
	private BankServerState sharedBankStateObj;//The sharedState created in the BankServer class
	private char BankThreadID; //ID of the client (A/B/C)
	private String ThreadName; //Name of the thread (only used to display at initialisation)
	
	//Thread Setup
	public BankServerThread(Socket BSSocket, char ClientID, BankServerState sharedObj, int ThreadNo)
	{
		this.BThreadSocket = BSSocket;
		sharedBankStateObj = sharedObj;
		BankThreadID = ClientID;
		ThreadName = "BankServerThread-" + (ThreadNo);
	}
	
	//This is equivalent to the main method. Must be called in an external class with the [.start] method
	public void run()
	{
		try 
		{
			//Initialise Thread
			System.out.println("New thread: " + ThreadName + " initialising");
			PrintWriter out = new PrintWriter(BThreadSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(BThreadSocket.getInputStream()));
			String inputLine, outputLine;
			
			//This is run always if the message received is not NULL
			while ((inputLine = in.readLine()) != null)
			{
				try
				{
					sharedBankStateObj.acquireLock();//Acquire ServerState Lock
					outputLine = sharedBankStateObj.processInput(BankThreadID, inputLine);//Process the client's request
					out.println(outputLine); //
					sharedBankStateObj.releaseLock();
				}
				catch(InterruptedException e)
				{
					System.err.println("Failed to acquire lock on reading: " + e);
				}
			}
			
			//Terminate the connection
			out.close();
			in.close();
			BThreadSocket.close();
		}
		//Error during program
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
