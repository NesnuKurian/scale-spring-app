package com.surinder.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //@RequestMapping(path = "api/v1/customer", method = RequestMethod.GET)
    @GetMapping("")
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") Integer customerId){

        return customerService.getCustomer(customerId);
    }

    @PostMapping("addCustomer")
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerService.addCustomer(customerRegistrationRequest);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable ("customerId") Integer customerId){
        customerService.deleteCustomerById(customerId);
    }

    public void updateCustomer(CustomerRegistrationRequest customerRegistrationRequest){

    }
}
