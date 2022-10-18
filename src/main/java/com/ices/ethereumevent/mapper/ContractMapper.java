package com.ices.ethereumevent.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ices.ethereumevent.domain.ContractPO;

@Mapper
public interface ContractMapper {

	
	public void insert(ContractPO contractPO);
	
	
	public ContractPO selectByAddress(String address);
}
