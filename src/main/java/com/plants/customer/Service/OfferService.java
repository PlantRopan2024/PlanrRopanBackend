package com.plants.customer.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plants.Dao.OfferDao;
import com.plants.entities.Offers;

@Service
public class OfferService {
	@Autowired
	public OfferDao offerDao;
	
	public Offers saveOffer(Offers offers) {
		return offerDao.save(offers);
	}

	public List<Offers> getAllOffers() {		
		return offerDao.getAllActiveOffer();
	}

}
