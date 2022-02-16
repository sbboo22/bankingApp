package com.ravature;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class EmployeeDaoImplementation implements EmployeeDao{
    Connection connection;

    public EmployeeDaoImplementation(){
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public void addEmployee(Employee employee) throws SQLException {
        String sql = "insert into employee (name, email, password) values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, employee.getName());
        preparedStatement.setString(2, employee.getEmail());
        preparedStatement.setString(3, employee.getPassword());
        int count = preparedStatement.executeUpdate();
        if(count > 0){
            System.out.println("Employee saved");
        }else{
            System.out.println("Their were duplicate values");;
        }
    }

    @Override
    public void updateEmployee(Employee employee, int id) throws SQLException {
        String sql = "UPDATE employee SET name = ?, email = ?, password = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, employee.getName());
        preparedStatement.setString(2, employee.getEmail());
        preparedStatement.setString(3, employee.getPassword());
        preparedStatement.setInt(4, id);
        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("Employee updated");
        else
            System.out.println("That Employee does not exist or duplicate credentials used");
    }

    @Override
    public void deleteEmployee(Employee employee, int id) throws SQLException {
        String sql = "DELETE FROM employee WHERE id = ? OR name = ? OR email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, employee.getName());
        preparedStatement.setString(3, employee.getEmail());
        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("Employee " + employee.getName() + " Was removed from the system.");
        else
            System.out.println("That Employee does not exist");

    }

    @Override
    public List<Employee> getEmployees() throws SQLException {
        String sql = "SELECT * FROM employee";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        LinkedList<Employee> emp = new LinkedList<Employee>();
        StringBuilder name = new StringBuilder(50);
        StringBuilder email = new StringBuilder(50);
        StringBuilder password = new StringBuilder(20);

        while(resultSet.next()){
            int id = resultSet.getInt(1);
            name.replace(0,49,resultSet.getString(2));
            email.replace(0,49, resultSet.getString(3));
            addEmployee(new Employee(name.toString(),email.toString(), password.toString()));
            System.out.println("ID: " + id + ", Name-" + resultSet.getString(2) + ", Email-" + resultSet.getString(3) + ", Password-" + resultSet.getString(4));
        }

        return null;
    }

    @Override
    public Employee getEmployeeByUserName(String userName) throws SQLException {
        String sql = "SELECT * FROM employee WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        ResultSet resultSet = preparedStatement.executeQuery();
        Employee foundEmployee = new Employee();
        while (resultSet.next()){
            //System.out.print(resultSet.getInt(1));
            foundEmployee.setName(resultSet.getString(2));
            foundEmployee.setEmail(resultSet.getString(3));
            foundEmployee.setPassword(resultSet.getString(4));
            foundEmployee.setId(resultSet.getInt(1));
        }

        return foundEmployee;
    }

    @Override
    public void assignedCustomers(int empID) throws SQLException {
        Scanner ops = new Scanner(System.in);
        String sql = "SELECT * FROM customer WHERE Employee_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, empID);
        ResultSet resultSet = preparedStatement.executeQuery();
        Customer foundcustomer = new Customer();
        while (resultSet.next()){
            //System.out.print(resultSet.getInt(1));
            foundcustomer.setName(resultSet.getString(2));
            foundcustomer.setEmail(resultSet.getString(3));
            foundcustomer.setPassword(resultSet.getString(4));
            foundcustomer.setRepID(resultSet.getInt(5));
            foundcustomer.setId(resultSet.getInt(1));
            System.out.println(foundcustomer.getId() + " "+ foundcustomer.getName() + " "+foundcustomer.getEmail());
            System.out.print("Do you need this customers Associated Accounts? (Y)es (N)o: ");
            String furtherinfo = ops.nextLine();
            if (furtherinfo.equals("Y") || furtherinfo.equals("Yes")){
                String sqlAA = "CALL getCustomersAccounts(?)";
                PreparedStatement associateAccounts = connection.prepareStatement(sqlAA);
                associateAccounts.setInt(1, foundcustomer.getId());
                ResultSet resultSet2 = associateAccounts.executeQuery();
                Account foundAccounts = new Account();
                String typehelp = "";
                System.out.print("Associated accounts: \n");
                while (resultSet2.next()){
                    foundAccounts.setBalance(resultSet2.getInt(4));
                    foundAccounts.setAccountName(resultSet2.getString(2));
                    typehelp = resultSet2.getString(3);
                    System.out.println(foundAccounts.getAccountName() +"  " + typehelp + ": " + foundAccounts.getBalance());
                }
                System.out.println();
            }else{

            }
        }



    }

    @Override
    public void pendingAccounts(int empID) throws SQLException {
        Scanner interact = new Scanner(System.in);
        Customer actCustomer = new Customer();
        String goNogo = "";
        int aproveOrDeny = 0;
        System.out.print("Check for pending accounts customer name: ");
        actCustomer.setName(interact.nextLine());
        String sql = "SELECT * FROM customer WHERE Employee_id = ? And name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, empID);
        preparedStatement.setString(2, actCustomer.getName());
        ResultSet resultSet = preparedStatement.executeQuery();
        Customer foundcustomer = new Customer();
        while (resultSet.next()){
            //System.out.print(resultSet.getInt(1));
            foundcustomer.setName(resultSet.getString(2));
            foundcustomer.setId(resultSet.getInt(1));
        }
        System.out.println(foundcustomer.getId());

        String sqlC = "SELECT * FROM account WHERE customer_id = ? AND verified = ?";
        PreparedStatement unverifiedAccounts = connection.prepareStatement(sqlC);
        unverifiedAccounts.setInt(1,foundcustomer.getId());
        unverifiedAccounts.setInt(2, 0);
        ResultSet unaprovedAccounts = unverifiedAccounts.executeQuery();
        Account needAproval = new Account();
        while (unaprovedAccounts.next()){
            needAproval.setAccountName(unaprovedAccounts.getString(2));
            needAproval.setBalance(unaprovedAccounts.getInt(4));
            String enmTransfer = unaprovedAccounts.getString(3);
            if (enmTransfer == "Checking")
                needAproval.set_accountType(Account.AccountType.Checking);
            else
                needAproval.set_accountType(Account.AccountType.Saving);
            System.out.println(foundcustomer.getName() + " Requested to make a new " + needAproval.getAccountType() +
                    " With a balance of " + needAproval.getBalance());
            System.out.println("(A)pprove / (D)ney");
            goNogo = interact.nextLine();
            if (goNogo.equals("A") || goNogo.equals("Approve")){
                String sqlV = "UPDATE account SET verified = ? WHERE account_number = ?";
                PreparedStatement sendVerification = connection.prepareStatement(sqlV);
                sendVerification.setInt(1,1);
                sendVerification.setInt(2, unaprovedAccounts.getInt(1));
                sendVerification.executeUpdate();
            } else {
                String sqlDelete = "DELETE FROM account WHERE account_number = ?";
                PreparedStatement denyAccount = connection.prepareStatement(sqlDelete);
                denyAccount.setInt(1,unaprovedAccounts.getInt(1));
                denyAccount.executeUpdate();
            }

        }


    }
}
