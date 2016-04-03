package com.gogorest.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gogorest.domain.SimpleTicketRepository;
import com.gogorest.domain.Ticket;
import com.gogorest.domain.TicketRepository;
import com.gogorest.domain.User;
import com.gogorest.domain.UserRepository;

@RestController
@RequestMapping(value="/gogo")
public class TicketRestController{		
	
	private static final Logger logger = LoggerFactory.getLogger(TicketRestController.class);
	@Autowired 
	UserRepository userRepository;
	
	@Autowired 
	TicketRepository ticketRepository;
	
	@Autowired 
	SimpleTicketRepository simpleTicketRepository;
	
	@RequestMapping(value="/tickets")
	public Collection <Ticket>  tickets(){
		logger.info("Inside tickets. Finding all tickets");
		List list = simpleTicketRepository.findAllfromCache();
		List <Ticket>fullData = new ArrayList<>();
		list.parallelStream()
	  	    .forEach(item  -> {
		  		HashMap cacheData = (HashMap) item;
		  		Ticket ticket= (Ticket) cacheData.get("RESPONSE_DATA");
		  		logger.info("---> Value  ### " +ticket.toString());
		  		fullData.add(ticket);
	  	  });
		return fullData;
	}

	@RequestMapping(value="/findticket")
	public Ticket getTicketByName(@RequestParam(value="id", required=false) Long id){
		logger.info("Inside getTicketByName. Finding ");
		return simpleTicketRepository.findByTicketId(id);
	}
	
	@RequestMapping(value="/findTreeTicket")
	public Ticket getTicketByNameandId(@RequestParam(value="code", required=false) String code){
		logger.info("Inside getTicketByNameandId. Finding " + code);
		return simpleTicketRepository.findfromCache(code);
	}
	
	@RequestMapping("/users")
	public Collection <User> users(){
		logger.info("Inside users. Finding all users");
		return (Collection<User>) userRepository.findAll();		
	}
	
	@RequestMapping(value ="/loadticket")
	public void insertTickets(){
		logger.info("Inside users. Loading all tickets");
		final Integer[] sum = new Integer[1];
		sum[0] = 100;
		Arrays.asList("CHN-MDU,DXB-LAX,DXB-BMB,DXB-CHG".split(",")).parallelStream()
		  	  .forEach(n -> {
		  		sum[0] = sum[0] +1;
		  		ticketRepository.save(new Ticket(n,  "TKT"+sum[0].toString()));
		  	  });
		logger.info("Inside users. Finding all tickets");
	}
	
	@RequestMapping(value ="/loadcache")
	public void loadCache(){
		logger.info("Inside loadCache. Loading all cache");
		simpleTicketRepository.loadCache();
	}
	
	@RequestMapping(value ="/hello")
	public String healthCheck(){
		logger.info("Inside healthCheck");
		return "OK";
	}
}
	