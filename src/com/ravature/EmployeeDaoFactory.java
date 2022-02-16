package com.ravature;

public class EmployeeDaoFactory {

    private static EmployeeDao edao;

    private EmployeeDaoFactory(){

    }

    public static EmployeeDao getEmployeeDao(){
        if(edao==null){
            edao = new EmployeeDaoImplementation();
        }
        return edao;
    }
}
