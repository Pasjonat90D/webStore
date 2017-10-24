package com.myProject.webStore.domain.repository;

import com.myProject.webStore.domain.Customer;

import java.util.List;

public interface CustomerRepository {

    List<Customer> getAllCustomers();
}
