package com.ices.ethereumevent.domain;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransferSingleEventReturnValues implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String operator;

    public String from;

    public String to;

    public BigInteger id;

    public BigInteger value;
}
