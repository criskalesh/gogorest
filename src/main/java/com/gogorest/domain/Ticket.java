package com.gogorest.domain;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="ticket")
public class Ticket implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
    @Column(name="ticket_id")
	private Long ticketid;
	
	@Column(name="ticket_name")
	private String ticketName;
	
	@Column(name="ticket_code")
	private String ticketCode;
	
	public Long getTicketid() {
		return ticketid;
	}

	public void setTicketid(Long ticketid) {
		this.ticketid = ticketid;
	}

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	
	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public Ticket(String ticketName, String ticketCode) {
		this.ticketName = ticketName;
		this.ticketCode = ticketCode;
	}
	
	public Ticket(){
	}

	@Override
	public String toString() {
		return "Ticket [ticketid=" + ticketid + ", ticketName=" + ticketName + ", ticketCode=" + ticketCode + "]";
	}
	
	
}
