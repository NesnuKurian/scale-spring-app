package com.surinder.customer;

import com.surinder.exception.DuplicateResourceException;
import com.surinder.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        "customer with id [%s] not found".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        String email = customerRegistrationRequest.email();
        if(customerDao.exitsPersonWithEmail(email)){
            throw new DuplicateResourceException("Customer with email already exists");
        }
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age());

        customerDao.insertCustomer(customer);
    }

    public void updateCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        ///customerDao.updateCustomer();
    }

    public void deleteCustomerById(Integer customerId) {
         if (!customerDao.exitsPersonWithId(customerId)){
             throw new ResourceNotFound("Customer with id does not exist");
        }
         customerDao.deleteCustomerById(customerId);
    }
}
