package com.plants.Service;

import java.util.List;
import java.util.Objects;

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
	
	public String offersAgentVsCusApproved(String pk) {
		Offers getoffers = this.offerDao.findById(pk);
		if(Objects.nonNull(getoffers)) {
			getoffers.setNewActive(true);
			this.offerDao.save(getoffers);
		}
		return "Active Offer";
	}
	
	public String offersAgentVsCusDisApprovedAgent(String pk) {
		Offers getoffers = this.offerDao.findById(pk);
		if(Objects.nonNull(getoffers)) {
			getoffers.setNewActive(false);
			this.offerDao.save(getoffers);
		}
		return "De Activeted Offer";
	}
	
	public List<Offers> getAllOffersAgentMobApi() {		
		return offerDao.getAllActiveOfferAgentMobApi();
	}
	
	public List<Offers> getAllOffersCusMobApi() {		
		return offerDao.getAllActiveOfferCusMobApi();
	}
	public List<Offers> getAllOffersAgent() {		
		return offerDao.getAllActiveOfferAgent();
	}
	
	public List<Offers> getAllOffersCus() {		
		return offerDao.getAllActiveOfferCus();
	}
	
	public Offers getIdOffers(String pk) {
		Offers getoffers = this.offerDao.findById(pk);
		return getoffers;
	}

}
