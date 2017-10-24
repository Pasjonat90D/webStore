package com.myProject.webStore.domain;

public class Customer {

    private String customerId;
    private String name;
    private String address;
    private boolean noOfOrdersMade;

    public Customer(String customerId, String name, String address){
        this.customerId = customerId;
        this.name = name;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId.equals(customer.customerId);
    }

    @Override
    public int hashCode() {
        return customerId.hashCode();
    }
    @Override
    public String toString(){
        return "Customer [customerID="+customerId+" name="+name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isNoOfOrdersMade() {
        return noOfOrdersMade;
    }

    public void setNoOfOrdersMade(boolean noOfOrdersMade) {
        this.noOfOrdersMade = noOfOrdersMade;
    }
}
