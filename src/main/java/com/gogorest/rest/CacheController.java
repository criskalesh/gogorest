package com.gogorest.rest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gogorest.domain.SimpleTicketRepository;
import com.gogorest.domain.Ticket;
import com.gogorest.domain.TicketRepository;

@Controller
@RequestMapping(value="/caching")
public class CacheController {

	@Autowired 
	SimpleTicketRepository simpleTicketRepository;
	
	@Autowired 
	TicketRepository ticketRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(CacheController.class);
	
    @RequestMapping("/loadData")
    public String loadData(Model model) {
    	logger.info("Inside loadData...");
		try{
			Arrays.asList("CHN-MDU,DXB-LAX,DXB-BMB,DXB-CHG".split(",")).parallelStream()
			  	  .forEach(n -> {
			  		ticketRepository.save(new Ticket(n,  "TKT"+UUID.randomUUID().toString().toUpperCase().substring(0, 7)));
			  	  });
			model.addAttribute("status", "Data Load Success!!");
		}catch(Exception e){
			logger.info("Exception Occured", e);
			model.addAttribute("status", "Data Load Failure!!" + e.getMessage());
		}
        return "admincache";
    }

    @RequestMapping("/loadcache")
    public String loadcache(Model model) {
    	logger.info("Inside loadcache...");
    	try{
    		simpleTicketRepository.loadCache();
			model.addAttribute("status", "Cache Load Success!!");
		}catch(Exception e){
			logger.info("Exception Occured", e);
			model.addAttribute("status", "Cache Load Failure!!" + e.getMessage());
		}
        return "admincache";
    }
    
    @RequestMapping(value="/findAllData")
	public String findAllData(Model model){
		logger.info("Inside tickets. Finding all data");
		try{
			List list = simpleTicketRepository.findAllfromCache();
			List <Ticket>fullData = new ArrayList<>();
			if(list.size() >0)
			{
			  list.forEach(item  -> {
			  		HashMap cacheData = (HashMap) item;
			  		Ticket ticket= (Ticket) cacheData.get("RESPONSE_DATA");
			  		logger.info("---> Value  ### " +ticket.toString());
			  		fullData.add(ticket);
		  	  });
			}
			model.addAttribute("status", "Cache Data Load Success!!");
			model.addAttribute("fullData", fullData);
		}catch(Exception e){
			logger.info("Exception Occured", e);
			model.addAttribute("status", "Cache Data Find Failure!!" + e.getMessage());
		}
        return "admincache";
	}
    
    @RequestMapping(value = "/findSingleData", method = RequestMethod.POST)
	public String findSingleData(String code, Model model) {    	
    	Ticket ticket = simpleTicketRepository.findfromCache(code);
    	model.addAttribute("status", "Cache Data Loaded..");
		model.addAttribute("singleData", ticket);
		return "admincache";
	}
    
    @RequestMapping(value = "/saveTicket", method = RequestMethod.POST)
	public String saveTicketData(String name, Model model) {
    	model.addAttribute("status", "Data Save Complete..");
    	simpleTicketRepository.savetToCache(name);
		return "admincache";
	}
    
    @RequestMapping(value = "read/findSingleData", method = RequestMethod.POST)
	public String singleDataRead(String code, Model model) {
    	model.addAttribute("status", "Cache Data Loaded..");
    	Ticket ticket = simpleTicketRepository.findfromCache(code);
		model.addAttribute("singleData", ticket);
		return "viewcache";
	}
}