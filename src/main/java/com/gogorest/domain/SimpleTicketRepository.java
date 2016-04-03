package com.gogorest.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.tree.Fqn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = "ticketcache")
public class SimpleTicketRepository{
	
	@Autowired 
	TicketRepository ticketRepository;
	private static final Logger logger = LoggerFactory.getLogger(SimpleTicketRepository.class);
	
	@Autowired
	private DefaultCacheManager infinispanCacheManager;	

	@Cacheable(value = "ticketRestCache")
	public Ticket findByTicketId(Long id){
		logger.info("---> Loading ticket with ticketName '" + id + "'");
		Ticket ticket = ticketRepository.findOne(id);
		return ticket;
	}
	
	public Ticket findfromCache(String code){
		logger.info("---> Loading ticket with ticketName ### '" + " code - " + code);
		Ticket ticket = null;
		StringBuffer cachePathBuffer = new StringBuffer("TKT");	
		cachePathBuffer.append("/").append(code);
		logger.info("---> For KEY ### '" + cachePathBuffer.toString());
		Fqn cacheNodeFqn = Fqn.fromString(cachePathBuffer.toString()); 
		HashMap cacheData = (HashMap) infinispanCacheManager.getCache("ticketcache").get(cacheNodeFqn);
		logger.info("---> Map Data ### " + cacheData);		
		if(null != cacheData){
			ticket = (Ticket) cacheData.get("RESPONSE_DATA");
		}
		return ticket;
	}
	
	public List findAllfromCache(){		
		List list = new ArrayList<>();
		list.addAll(infinispanCacheManager.getCache("ticketcache").values());		
		return list;
	}
	
	public void loadCache(){
		logger.info("---> Loading CACHE ### ");		
		List<Ticket> ticket = ticketRepository.findAll();
		for (Ticket ticket2 : ticket) {
			StringBuffer cachePathBuffer = new StringBuffer("TKT");	
			cachePathBuffer.append("/").append(ticket2.getTicketCode());
			logger.info("---> Loading KEY ### " + cachePathBuffer.toString());	
			Fqn cacheNodeFqn = Fqn.fromString(cachePathBuffer.toString()); 
			Map cacheData = new HashMap();		
			cacheData.put("RESPONSE_DATA",ticket2);
			infinispanCacheManager.getCache("ticketcache").put(cacheNodeFqn, cacheData);
		}
	}
	
	public void savetToCache(String ticketName){
		logger.info("---> Loading CACHE ### ");	
			Ticket ticket = new Ticket(ticketName,"TKT"+UUID.randomUUID().toString().toUpperCase().substring(0, 7));
			ticket.setTicketid(new Long(getRandomNumberInRange()));
			StringBuffer cachePathBuffer = new StringBuffer("TKT");	
			cachePathBuffer.append("/").append(ticket.getTicketCode());
			logger.info("---> Loading KEY ### " + cachePathBuffer.toString());	
			Fqn cacheNodeFqn = Fqn.fromString(cachePathBuffer.toString()); 
			Map cacheData = new HashMap();		
			cacheData.put("RESPONSE_DATA",ticket);
			infinispanCacheManager.getCache("ticketcache").put(cacheNodeFqn, cacheData);
	}
	
	// Don't do this at home
    private void simulateSlowService() {
        try {
            long time = 5000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private int getRandomNumberInRange() {    	
		Random r = new Random();
		return r.ints(1000, (2000 + 1)).limit(1).findFirst().getAsInt();
		
	}
}
