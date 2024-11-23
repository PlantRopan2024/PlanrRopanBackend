package com.plants.customer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plants.Dao.CustomerDao;
import com.plants.entities.CustomerMain;

@Service
public class CustomerService {
	@Autowired
	private CustomerDao customerDao;
 public CustomerMain saveCustomerProfile(CustomerMain customerMain) {
	return  customerDao.save(customerMain);
 }
}
