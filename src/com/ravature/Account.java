package com.ravature;


public class Account {
    private int accountNumber;
    private String accountName;
    private int balance;
    public enum AccountType{ Saving, Checking }

    AccountType _accountType;
    private int ownerID;

    public Account(){}
    public Account(String accountName, AccountType accType, int balance){
        _accountType = accType;
        this.balance = balance;
        this.accountName = accountName;
    }
    public void setAccountName(String accountName){this.accountName = accountName;}
    public String getAccountName() {return accountName;}

    public void set_accountType(AccountType Type){_accountType = Type;}
    public AccountType getAccountType(){return _accountType;}

    public int getBalance() {return balance;}
    public void setBalance(int bal){balance = bal;}

    public void setOwnerID(int ownerId){ownerID = ownerId;}
    public int getOwnerID(){return ownerID;}


}
