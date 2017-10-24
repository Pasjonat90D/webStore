package com.myProject.webStore.domain.repository.impl;

import com.myProject.webStore.domain.Customer;
import com.myProject.webStore.domain.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {

    private List<Customer> listOfCustomers = new ArrayList<>();

    public InMemoryCustomerRepository(){
        Customer adam1 = new Customer("1","Adam1","AddressAdam1");
        adam1.setNoOfOrdersMade(true);

        Customer adam2 = new Customer("2","Adam2","AddressAdam2");
        adam2.setNoOfOrdersMade(false);

        Customer tom = new Customer("3","Tom","AddressTom");
        tom.setNoOfOrdersMade(true);

        Customer john = new Customer("4","John","AddressJohn");
        john.setNoOfOrdersMade(false);

        listOfCustomers.add(adam1);
        listOfCustomers.add(adam2);
        listOfCustomers.add(tom);
        listOfCustomers.add(john);
    }

    @Override
    public List<Customer> getAllCustomers() {
          return listOfCustomers;
    }
}
