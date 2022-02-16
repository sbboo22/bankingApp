package com.ravature;

public class CustomerDaoFacotry {
    private static CustomerDao cdao;

    private CustomerDaoFacotry(){

    }

    public static CustomerDao getCustomerDao(){
        if(cdao==null){
            cdao = new CustomerDaoImplementation();
        }
        return cdao;
    }
}
