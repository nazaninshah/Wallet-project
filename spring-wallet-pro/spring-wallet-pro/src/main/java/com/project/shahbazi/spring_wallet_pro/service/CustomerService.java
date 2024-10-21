package com.project.shahbazi.spring_wallet_pro.service;

import com.project.shahbazi.spring_wallet_pro.entity.CustomerEntity;
import com.project.shahbazi.spring_wallet_pro.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<CustomerEntity> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public CustomerEntity createCustomer(CustomerEntity customer) {
        return customerRepository.save(customer);
    }

    public CustomerEntity updateCustomer(Long id, CustomerEntity customerDetails) {
        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            CustomerEntity customer = optionalCustomer.get();
            customer.setName(customerDetails.getName());
            customer.setSurname(customerDetails.getSurname());
            customer.setEmail(customerDetails.getEmail());
            customer.setPhoneNumber(customerDetails.getPhoneNumber());
            customer.setBirthdate(customerDetails.getBirthdate());
            customer.setGender(customerDetails.getGender());
            customer.setMilitaryServiceStatus(customerDetails.getMilitaryServiceStatus());
            return customerRepository.save(customer);
        } else {
            throw new RuntimeException("Customer not found with id: " + id);
        }
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
