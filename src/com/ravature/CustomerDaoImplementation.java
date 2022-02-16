package com.ravature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CustomerDaoImplementation implements CustomerDao{
    Connection connection;

    private Customer verifiedCustomer = new Customer();
    public CustomerDaoImplementation(){
        this.connection = ConnectionFactory.getConnection();
    }
    //Prefomed by me or Employee START
    @Override
    public void addCustomer(Customer customer) throws SQLException {
        //assign customer employee representative
        String sql2 = "SELECT id FROM employee";
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql2);
        ResultSet resultSet = preparedStatement1.executeQuery();
        int load_balancer = 0;
        while (resultSet.next()){
            load_balancer = getRandomInt(100, resultSet.getInt("id"));
        }


        //add a customer
        String sql = "insert into customer (name, email, password, Employee_id) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getEmail());
        preparedStatement.setString(3, customer.getPassword());
        preparedStatement.setInt(4, load_balancer);

        int count = preparedStatement.executeUpdate();
        if(count > 0){
            System.out.println("New customer saved.");
        }else{
            System.out.println("Oops!, something went wrong");;
        }
    }

    @Override
    public void updateCustomer(Customer customer, int id) throws SQLException {
        String sql = "UPDATE customer SET name = ?, email = ?, password = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getEmail());
        preparedStatement.setString(3, customer.getPassword());
        preparedStatement.setInt(4, id);
        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("Customer updated");
        else
            System.out.println("That Employee does not exist or duplicate credentials used");
    }

    @Override
    public void deleteCustomer(Customer customer, int id) throws SQLException {
        String sql = "DELETE FROM customer WHERE id = ? OR name = ? OR email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, customer.getName());
        preparedStatement.setString(3, customer.getEmail());
        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("Employee " + customer.getName() + " Was removed from the system.");
        else
            System.out.println("That customer does not exist");
    }

    @Override
    public Account createAccount(int customerID) throws SQLException {
        Scanner accountprompt = new Scanner(System.in);
        Account derivedAccount = new Account();

        System.out.print("Please enter the account name of the account you would like to create: " );
        derivedAccount.setAccountName(accountprompt.nextLine());
        System.out.print("Account type (enter Saving/Checking): ");
        derivedAccount.set_accountType(Account.AccountType.valueOf(accountprompt.nextLine()));
        System.out.print("How much would you like to deposit: ");
        try {
            derivedAccount.setBalance(accountprompt.nextInt());
        }catch (InputMismatchException e){
            System.out.println("Invalid input");
        }

        //account
        String sql = "insert into account (account_name, type, balance, customer_id) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, derivedAccount.getAccountName());
        preparedStatement.setString(2, derivedAccount._accountType.toString());
        preparedStatement.setInt(3, derivedAccount.getBalance());
        preparedStatement.setInt(4, customerID);

        int count = preparedStatement.executeUpdate();
        if(count > 0){
            System.out.println("New Account saved.");
        }else{
            System.out.println("Oops!, something went wrong");;
        }

        System.out.println("Thank you your account will soon be approved if things checkout");
        return derivedAccount;
    }

    //END START Customer operations...
    @Override
    public void showAccount(int customerID) throws SQLException {
        String sql = "SELECT * FROM account WHERE customer_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, customerID);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("you have "+ resultSet.getFetchSize() + " Accounts");;
        while (resultSet.next()){
            System.out.println("account: " + resultSet.getInt(1) + " " + resultSet.getString(3) + " "+ resultSet.getString(2)
            +  " Balance: " + resultSet.getInt(4));
        }
    }

    @Override
    public void withdrawMoney(int customerID) throws SQLException {
        //get current account balance
        Scanner ops = new Scanner(System.in);
        Account deposit = new Account();
        boolean accountverifie = false;
        int balance = 0;
        System.out.println("Please enter the name of the acount you want to Withdraw from: ");
        deposit.setAccountName(ops.nextLine());
        String sql2 = "SELECT balance, verified from account WHERE account_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql2);
        preparedStatement.setString(1, deposit.getAccountName());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            balance = resultSet.getInt(1);
            accountverifie = resultSet.getBoolean(2);
        }
        System.out.print("How much would you like to Withdraw: ");
        try {
            balance -= ops.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Invalid value entered");
        }
        if (accountverifie && balance > 0) {
            //Upadate the account balance
            String sql = "UPDATE account SET balance = ? WHERE account_name = ?";
            PreparedStatement prepstate2 = connection.prepareStatement(sql);
            prepstate2.setInt(1, balance);
            prepstate2.setString(2, deposit.getAccountName());
            int count = prepstate2.executeUpdate();
            if (count > 0)
                System.out.println("Transaction successful new balance: "+ balance);
            else
                System.out.println("Transaction failed");
        }else{
            System.out.println("OOps something went wrong!");
        }
    }

    @Override
    public void depositeMoney(int customerID) throws SQLException {
        //get current account balance
        Scanner ops = new Scanner(System.in);
        Account deposit = new Account();
        boolean accountverifie = false;
        int balance = 0;
        System.out.print("Please enter the name of the acount you want to deposite to: ");
        deposit.setAccountName(ops.nextLine());
        String sql2 = "SELECT balance, verified from account WHERE account_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql2);
        preparedStatement.setString(1, deposit.getAccountName());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            balance = resultSet.getInt(1);
            accountverifie = resultSet.getBoolean(2);
        }
        System.out.print("How much would you like to deposite: ");
        try {
            balance += ops.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Invalid value entered");
        }
        if (accountverifie && ops.nextInt() > 0) {
            //Upadate the account balance
            String sql = "UPDATE account SET balance = ? WHERE account_name = ?";
            PreparedStatement prepstate2 = connection.prepareStatement(sql);
            prepstate2.setInt(1, balance);
            prepstate2.setString(2, deposit.getAccountName());
            int count = prepstate2.executeUpdate();
            if (count > 0)
                System.out.println("Transaction successful");
            else
                System.out.println("Transaction failed");
        }else{
            System.out.println("Please wait for this account to be verified before perfoming transacitons");
        }
    }

    @Override
    public void tranferMoney(int cutomerID) throws SQLException {
        //get current account balance
        Scanner ops = new Scanner(System.in);
        Account deposit = new Account();
        Account withdrawn = new Account();
        boolean accountverified = false;
        int balanceFrom = 0;
        int balanceTo = 0;
        int amount = 0;
        //Get balance From
        System.out.println("Please enter the name of the account you want to Transfer from: ");
        withdrawn.setAccountName(ops.nextLine());
        String sql2 = "SELECT balance, verified from account WHERE account_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql2);
        preparedStatement.setString(1, withdrawn.getAccountName());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            balanceFrom = resultSet.getInt(1);
            accountverified = resultSet.getBoolean(2);
        }
        System.out.print("How much would you like to Transfer: ");
        try {
            amount= ops.nextInt();
            balanceFrom -= amount;
            withdrawn.setBalance(balanceFrom);
            ops.nextLine();
        }catch (InputMismatchException e){
            System.out.println("Invalid value entered");
        }
        if (accountverified && withdrawn.getBalance() > 0) {
            System.out.print("Please enter the name of the account you want to Transfer to: ");
            deposit.setAccountName(ops.next());
            PreparedStatement preparedStatementD = connection.prepareStatement(sql2);
            preparedStatementD.setString(1, deposit.getAccountName());
            ResultSet resultSetD = preparedStatementD.executeQuery();
            while(resultSetD.next()) {
                balanceTo = resultSetD.getInt(1);
                accountverified = resultSetD.getBoolean(2);
            }
                balanceTo += amount;
                deposit.setBalance(balanceTo);

            //Upadate the account balances
            int count = 0;
            String sql = "UPDATE account SET balance = ? WHERE account_name = ?";
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    PreparedStatement prepstate2 = connection.prepareStatement(sql);
                    prepstate2.setInt(1, withdrawn.getBalance());
                    prepstate2.setString(2, withdrawn.getAccountName());
                    count = prepstate2.executeUpdate();
                } else if (i == 1){
                    PreparedStatement prepstate3 = connection.prepareStatement(sql);
                    prepstate3.setInt(1, deposit.getBalance());
                    prepstate3.setString(2, deposit.getAccountName());
                    count = prepstate3.executeUpdate();
                }
                if (count > 0)
                    System.out.println("Transaction successful");
                else
                    System.out.println("Transaction failed");
            }
        }else{
            System.out.println("Please wait for this account to be verified before perfoming transacitons");
        }
    }

    @Override
    public void getRepInfo(int repID) throws SQLException {
        String sql = "SELECT * FROM employee WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, repID);
        ResultSet resultSet = preparedStatement.executeQuery();
        Employee foundEmployee = new Employee();
        while (resultSet.next()){
            //System.out.print(resultSet.getInt(1));
            foundEmployee.setName(resultSet.getString(2));
            foundEmployee.setEmail(resultSet.getString(3));
        }
        System.out.println("Your rep is: " + foundEmployee.getName() + " Contact them at: " + foundEmployee.getEmail());

    }

    @Override
    public List<Customer> getCustomers() throws SQLException {
        return null;
    }

    @Override
    public Customer getCustomer(String username) throws SQLException {
        String sql = "SELECT * FROM customer WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        Customer foundcustomer = new Customer();
        while (resultSet.next()){
            //System.out.print(resultSet.getInt(1));
            foundcustomer.setName(resultSet.getString(2));
            foundcustomer.setEmail(resultSet.getString(3));
            foundcustomer.setPassword(resultSet.getString(4));
            foundcustomer.setRepID(resultSet.getInt(5));
            foundcustomer.setId(resultSet.getInt(1));
        }

        return foundcustomer;
    }
    private int getRandomInt(int min, int max){
        Integer rannum = (int)Math.floor(Math.random() * (Math.ceil(max) - Math.floor(min))+ Math.ceil(min));
        return rannum;
    }
}
