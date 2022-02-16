package com.ravature;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

class RunSupport{
    EmployeeDao edao = EmployeeDaoFactory.getEmployeeDao();
    CustomerDao cdao = CustomerDaoFacotry.getCustomerDao();
    static boolean dbAccess = false;
    public String logeduser = "";
    Employee opertingEmployee = new Employee();
    Customer operatingCustomer = new Customer();
    public boolean welcome() throws SQLException {
        boolean validChoice = false;
        Scanner operator1 = new Scanner(System.in);
//        Customer operatingCustomer = new Customer();
//        Employee operatingEmployee = new Employee();
        System.out.print("Welcome to shawns banking are you a *Customer* or *Employee*: ");
        String userType = operator1.nextLine();
        while (!userType.equals("Customer") && !userType.equals("Employee")){
            System.out.print("Welcome to shawns banking are you a *Customer* or *Employee*: ");
            userType = operator1.nextLine();
        }
        if (userType.equals("Customer")){
            int attempts = 0;
            boolean exist = false;
            while(!exist && attempts < 3) {
                System.out.print("Welcome enter your customer username: ");
                validChoice = true;
                logeduser = userType;
                userType = operator1.nextLine();
                operatingCustomer = cdao.getCustomer(userType);
                if (operatingCustomer.getName() == null){
                    System.out.println("That user name does not exist in our system");
                }else{
                    exist = true;
                }
            }
            System.out.print("Hello " + operatingCustomer.getName() + " please enter your password: ");
            userType = operator1.nextLine();
            if(userType.equals(operatingCustomer.getPassword())){
                System.out.println("Access Granted");
                dbAccess = true;
            } else  {
                System.out.println("Bad credentials");
            }

        }else if(userType.equals("Employee")){
            int attempts = 0;
            boolean exist = false;
            while(!exist && attempts < 3) {
                System.out.print("Enter your employee username: ");
                validChoice = true;
                logeduser = userType;
                userType = operator1.nextLine();
                attempts++;
                opertingEmployee = edao.getEmployeeByUserName(userType);
                if (opertingEmployee.getName() == null){
                    System.out.println("That user name does not exist in our system");
                }else{
                    exist = true;
                }
            }
            System.out.print("Hello "+ opertingEmployee.getName() + " please enter your password: ");
            userType = operator1.nextLine();
            if (userType.equals(opertingEmployee.getPassword())){
                System.out.println("ACCESS GRANTED");
                dbAccess = true;
            }else{
                System.out.print("Invalid password");
                dbAccess = false;
            }
        }else{
            System.out.print("Invalid selection enter *Quit* to quit");
            validChoice = true;
        }
        return validChoice;
    }

    public boolean customerOperations() throws SQLException {
        Scanner operation = new Scanner(System.in);
        Account newAccount = new Account();
        boolean operationsCompleted = false;
        while (!operationsCompleted) {
            System.out.println("Please enter the operation you would like to perform: \n" +
                    "Create Account(Create)\tAccounts(Accounts)\tWithdraw funds(Withdraw)\tDeposite funds(Deposite)\t" +
                    "Transfer funds(Transfer)\tGet representative(Rep)\tQuit(Quit)");
            switch (operation.nextLine()){
                case "Create":
                    newAccount = cdao.createAccount(operatingCustomer.getId());
                    break;
                case "Accounts":
                    cdao.showAccount(operatingCustomer.getId());
                    break;
                case "Withdraw":
                    cdao.withdrawMoney(operatingCustomer.getId());
                    break;
                case "Deposite":
                    cdao.depositeMoney(operatingCustomer.getId());
                    break;
                case "Transfer":
                    cdao.tranferMoney(operatingCustomer.getId());
                    break;
                case "Rep":
                    cdao.getRepInfo(operatingCustomer.getrepID());
                    break;
                case "Quit":
                    System.out.println("Thank you for banking with shawnsbank.");
                    operationsCompleted = true;
                    break;
                default:
                    System.out.println("invalid input.");
                    break;
            }

        }
        return operationsCompleted;
    }
    public boolean EmployeeOperations() throws SQLException{
        Scanner operation = new Scanner(System.in);
        Account newAccount = new Account();
        Customer newCustomer = new Customer();
        boolean operationsCompleted = false;
        while (!operationsCompleted) {
            System.out.println("Please enter the operation you would like to perform: \n" +
                    "Create User(Create)\tMy Customers(Customers)\tVerify Accounts(Verify))\tQuit(Quit)");
            switch (operation.nextLine()){
                case "Create":
                    System.out.print("Customer name: ");
                    newCustomer.setName(operation.nextLine());
                    System.out.print("Customer email: ");
                    newCustomer.setEmail(operation.nextLine());
                    System.out.print("Customer password: ");
                    newCustomer.setPassword(operation.nextLine());
                    newCustomer.setRepID(opertingEmployee.getId());
                    cdao.addCustomer(newCustomer);
                    break;
                case "Customers":
                    edao.assignedCustomers(opertingEmployee.getId());
                    break;
                case "Verify":
                    edao.pendingAccounts(opertingEmployee.getId());
                    break;
                case "Quit":
                    System.out.println("Great work today " + opertingEmployee.getName());
                    operationsCompleted = true;
                    break;
                default:
                    System.out.println("invalid input.");
                    break;
            }

        }
        return operationsCompleted;
    }
    public RunSupport(){
    }
}

public class Main {



    public static void main(String[] args) throws SQLException {
        EmployeeDao edao = EmployeeDaoFactory.getEmployeeDao();
        CustomerDao cdao = CustomerDaoFacotry.getCustomerDao();
        RunSupport running = new RunSupport();
//        Employee newWorker = new Employee("Young Blood", "young@gmail.com", "enough");
//
//        Customer newBlood = new Customer("Jeson Jankeins", "jeson@gmail.com", "throw");
//        cdao.updateCustomer(newBlood, 2005);
////        Employee queeredWorker = edao.getEmployeeByUserName("young@gmail.com");
////        System.out.println(queeredWorker.getName());
        while (!running.welcome()){
            running.welcome();
        }
        if (running.dbAccess == true && running.logeduser.equals("Customer")){
            running.customerOperations();
        }else if (running.dbAccess == true && running.logeduser.equals("Employee")){
            running.EmployeeOperations();
        }

    }
}
