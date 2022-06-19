# WLFB: Bank Application - Brunel University
The following project is part of an assignment submission. 

It was required to implement a networking application with Java using a Single-Server, Multi-Client architecture in order to manage multiple bank accounts simultaneously.

The following program makes use of threads in order to handle client requests concurrently and keeps track of each user's balance. Clients can add, substract and transfer money between accounts.

## **How to run this project**
---
The implementation currently needs an Integrated Development Environment (IDE) software in order to run. The program is configured to run locally using the console terminals.

**Software recommended:** Eclipse IDE, IntelliJ IDEA

* Once the project is opened, proceed to run the BankServer.java in one terminal.
* After starting the server, open each client in a separate terminal in your IDE program.

**Request Syntax** *(For Clients)*
* Deposit - `add [amount]`
* Withdraw - `subtract [amount]`
* Transfer - `transfer [amount] [recipient account]`

**NOTE:** For simplicity, recipient accounts are defined as "a", "b", and "c".

## **Sample Transactions**
---
### **1. Deposit Money**
![Test 1](/Captures/Test%201.png)
### **1. Withdraw Money**
![Test 2](/Captures/Test%202.png)
### **1. Transfer Money**
![Test 3](/Captures/Test%203%20.png)

