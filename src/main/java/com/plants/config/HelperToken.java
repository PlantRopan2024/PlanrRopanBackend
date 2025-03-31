package com.plants.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.plants.Dao.CustomerDao;
import com.plants.Dao.MobileApiDao;
import com.plants.entities.AgentMain;
import com.plants.entities.CustomerMain;

@Component
public class HelperToken {
	
	@Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MobileApiDao mobileApiDao;

    @Autowired
    private CustomerDao customerDao;
    
    public AgentMain validateAgentToken(String token) {
	    String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
	    String mobileNumber = jwtUtil.extractUsername(jwtToken);
	    AgentMain agent = mobileApiDao.findMobileNumberValidateToken(mobileNumber);
	    return (agent != null && jwtToken.equals(agent.getToken())) ? agent : null;
	}

	public CustomerMain validateCustomerToken(String token) {
	    String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
	    String mobileNumber = jwtUtil.extractUsername(jwtToken);
	    CustomerMain customer = customerDao.findMobileNumber(mobileNumber);
	    return (customer != null && jwtToken.equals(customer.getToken())) ? customer : null;
	}
}
