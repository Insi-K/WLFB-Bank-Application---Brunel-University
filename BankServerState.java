import java.util.Scanner;


//Current issues in Class:
/*
 * Transfer can only happen on different accounts - Add check requirements [Done]
 * Check whether ending account is defined (cannot transfer to a D client when it does not exist) [Done]
 * Transfer only recognises capital letter ID's - Modification is required [Done]
 * ID variable may be changed back to string to perform ignoreCase functions [Not required]
 */
public class BankServerState 
{
	String ThreadID;
	private static double client_A, client_B, client_C;
	boolean accessing = false;
	int threadsInCue = 0;
	
	//Constructor
	BankServerState(double startingBalance)
	{
		//Set up the clients accounts
		client_A = startingBalance;
		client_B = startingBalance;
		client_C = startingBalance;
	}
	
	//Attempt to acquire a lock
	public synchronized void acquireLock() throws InterruptedException
	{
		Thread me = Thread.currentThread(); //Reference to current Thread (Self)
		System.out.println("BankServer" + me.getName() + " is trying to acquire a lock!");
		++threadsInCue;//Increase Thread Cue Counter
		
		//Put Thread in waiting Cue while Lock is not released
		while(accessing)
		{
			System.out.println(me.getName() + " is currently in Cue");
			wait();
		}
		
		//Once the lock has been released:
		--threadsInCue;//Reduce Thread Cue Counter
		accessing = true;
		System.out.println("Lock acquired successfully for :" + "BankServer" + me.getName());
	}
	
	//Release lock
	public synchronized void releaseLock()
	{
		//Release the lock and notify all Threads in Cue
		accessing = false;
		notifyAll();
		//Print to the console the recent action
		Thread me = Thread.currentThread();
		System.out.println("BankServer" + me.getName() + ": released Lock");
	}
	
	//process input method
	public synchronized String processInput(char ThreadID, String clientInput)
	{
		//Message received from "X" Client (Print in server console)
		System.out.println("Client: " + clientInput + " [Message recevied from Client_" + ThreadID + "]");
		
		char eClient; //The recipient client (This will be used on transfer transactions only)
		String request, theOutput; //Client request and Server response variables
		double amount;//Amount of money to manipulate
		boolean success;
		
		//Use a scanner to split up the client input
		Scanner scanRequest = new Scanner(clientInput);
		
		//Get the first part of the input and change it to lower case to include both Upper and Lower case inputs
		request = scanRequest.next().toLowerCase();
		
		//Only continue processing if the user has specified an amount
		if(scanRequest.hasNextDouble())
		{
			amount = scanRequest.nextDouble();
			
			//Keep if amount specified is a positive number
			if(amount > 0)
			{
				//Check if the requested action is valid
				switch(request)
				{
				case "add"://Deposit money
					success = AddMoney(ThreadID, amount);//Perform action and check if it's successful
					
					if(success)
					{
						//Transaction successful
						theOutput = "Client_" + ThreadID + " deposited " + amount + " units";
					}
					else
					{
						//Fail on transaction
						theOutput = "Request Failed - Client account does not exist";
					}
					
					break;
				case "subtract"://Withdraw money
					success = SubtractMoney(ThreadID, amount);//Perform action and check if it's successful
					
					if(success)
					{
						//Transaction successful
						theOutput = "Client_" + ThreadID + " has withdrawn " + amount + " units";
					}
					else
					{
						//Fail on transaction
						theOutput = "Request Failed - Client account does not exist";
					}
					
					break;
				case "transfer"://Transfer money from account A to account B
					if(scanRequest.hasNext())
					{
						//Get the recipient's ID and change it to uppercase (this allows the client to send any case input)
						eClient = Character.toUpperCase(scanRequest.next().charAt(0));
						
						//Check that the recipient is not the same as the sender
						if(ThreadID != eClient)
						{
							//Check if the transaction fails at any point
							success = SubtractMoney(ThreadID, amount);
							success = AddMoney(eClient, amount);
							
							if(success)
							{
								//Transaction successful
								theOutput = "Client_" + ThreadID + " transfered " + amount + " units to Client_" + eClient; 
							}
							else //In case AddMoney fails, undo the first transaction (subtract funds from client)
							{
								AddMoney(ThreadID, amount);
								theOutput = "Request Failed - Client account does not exist";
							}
						}
						else//In case the sender is the same as recipient
						{
							theOutput = "Request Failed - Recipient cannot be the same as the sender";
						}
					}
					else//If no recipient was found on input
					{
						theOutput = "Syntax Error - Recipient account was not specified. Correct syntax for this action: 'transfer <Amount> <Account ID>'";
					}
					break;
				default://Action is not a valid one
					theOutput = "Syntax Error - Requested action is not recognized. Valid requests: <add>//<subtract>//<transfer> (syntax is not case sensitive)";
				}
			}
			else//Specified amount is 0 or lower
			{
				theOutput = "Syntax Error - Invalid <Amount>. Can only be bigger than 0";
			}
		}
		else//Client has not specified amount
		{
			theOutput = "Syntax Error - Invalid Input. Amount was not defined or is invalid (has to be a number). Accepted Syntax: '<Server Action> <Amount>'";
		}
		
		//This is only used to check the account's balance
		CheckBalances();
		
		System.out.println(theOutput + " [Message sent to Client_" + ThreadID + "]");
		return theOutput;
		
	}
	
	//Add money function (on SELF account) == Deposit money
		public static boolean AddMoney(char client, double quantity)
		{
			//Tells whether action was successful (if client exists)
			boolean isRequestSuccess = false;
			
			//If the client account exists, retrieve it and perform operation
			switch(client)
			{
				case 'A':
					client_A += quantity;
					isRequestSuccess = true;
					break;
				case 'B':
					client_B += quantity;
					isRequestSuccess = true;
					break;
				case 'C':
					client_C += quantity;
					isRequestSuccess = true;
					break;
				default://Client does not exist
					isRequestSuccess = false;
					break;
			}
			
			return isRequestSuccess;
		}
		
		//Subtract money function (on SELF account) == Draw money
		public static boolean SubtractMoney(char client, double quantity)
		{	
			//Tells whether action was successful (if client exists)
			boolean isRequestSuccess = false;
			
			//If the client account exists, retrieve it and perform operation
			switch(client)
			{
				case 'A':
					client_A -= quantity;
					isRequestSuccess = true;
					break;
				case 'B':
					client_B -= quantity;
					isRequestSuccess = true;
					break;
				case 'C':
					client_C -= quantity;
					isRequestSuccess = true;
					break;
				default://Client does not exist
					isRequestSuccess = false;
					break;
			}
			
			return isRequestSuccess;
		}
		
		//Only used to keep track of the accounts
		public static void CheckBalances()
		{
			System.out.println("Current Account balances:");
			System.out.println("Client_A: " + client_A);
			System.out.println("Client_B: " +client_B);
			System.out.println("Client_C: " +client_C);
		}

}
