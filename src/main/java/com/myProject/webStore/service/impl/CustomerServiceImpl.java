package com.myProject.webStore.service.impl;

import com.myProject.webStore.domain.Customer;
import com.myProject.webStore.domain.repository.CustomerRepository;
import com.myProject.webStore.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }
}
