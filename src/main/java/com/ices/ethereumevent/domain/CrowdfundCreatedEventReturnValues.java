package com.ices.ethereumevent.domain;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CrowdfundCreatedEventReturnValues implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigInteger crowdfundId;
	
	private String operator;
	
	private String receiver;
	
	private BigInteger period;
	
	private BigInteger targetValue;
	
	private BigInteger retriveType;
	
	private String description;
	
	

}
