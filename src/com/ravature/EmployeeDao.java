package com.ravature;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao {
    void addEmployee(Employee employee) throws SQLException;
    void updateEmployee(Employee employee, int id) throws SQLException;
    void deleteEmployee(Employee employee, int id) throws SQLException;
    List<Employee> getEmployees() throws SQLException;
    Employee getEmployeeByUserName(String userName) throws SQLException;
    void assignedCustomers(int empID) throws SQLException;
    void pendingAccounts(int empID) throws SQLException;
}
