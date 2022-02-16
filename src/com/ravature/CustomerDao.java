package com.ravature;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDao {
    void addCustomer(Customer customer) throws SQLException;
    void updateCustomer(Customer customer, int id) throws SQLException;
    void deleteCustomer(Customer customer, int id) throws SQLException;
    Account createAccount(int customerID) throws SQLException;
    void showAccount(int customerID) throws SQLException;
    void withdrawMoney(int customerID) throws SQLException;
    void depositeMoney(int customerID) throws SQLException;
    void tranferMoney(int cutomerID) throws SQLException;
    void getRepInfo(int repID) throws SQLException;

    List<Customer> getCustomers() throws SQLException;
    Customer getCustomer(String username) throws SQLException;

}
