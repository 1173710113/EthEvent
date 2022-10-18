package com.ices.ethereumevent.service.impl;

import org.redisson.Redisson;
import org.redisson.api.RBitSet;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ices.ethereumevent.domain.ContractPO;
import com.ices.ethereumevent.mapper.ContractMapper;
import com.ices.ethereumevent.util.TimeUtil;



@Service
public class DACService {

	private static final String CONTRACT_PREFIX = "contract:";

	private static final String CONTRACT_DAC_PREFIX = "contract:dac:";

	private static final String CONTRACT_LOCK_PREFIX = "contract:lock:";

	@Autowired
	Redisson redisson;

	@Autowired
	ContractMapper contractMapper;

	public void DAC(String contractAddress) {
		RBucket<ContractPO> contractBucket = redisson.getBucket(CONTRACT_PREFIX + contractAddress);
		if (contractBucket.get() != null) {
			RBitSet bitSet = redisson.getBitSet(CONTRACT_DAC_PREFIX + TimeUtil.getNowDateFormat());
			if (!bitSet.get(contractBucket.get().getId())) {
				bitSet.set(contractBucket.get().getId());
			}
		} else {

			RLock contractLock = redisson.getLock(CONTRACT_LOCK_PREFIX + contractAddress);
			contractLock.lock();

			try {
				contractBucket = redisson.getBucket(CONTRACT_PREFIX + contractAddress);
				if (contractBucket.get() != null) {
					RBitSet bitSet = redisson.getBitSet(CONTRACT_DAC_PREFIX + TimeUtil.getNowDateFormat());
					if(!bitSet.get(contractBucket.get().getId())) {
						bitSet.set(contractBucket.get().getId());
					}
				} else {
					ContractPO contractPO = contractMapper.selectByAddress(contractAddress);
					if (contractPO != null) {
						contractBucket.set(contractPO);
						contractBucket.expire(TimeUtil.randomDurationNormal());
					} else {
						contractPO = new ContractPO();
						contractPO.setAddress(contractAddress);
						contractMapper.insert(contractPO);
						contractBucket.set(contractPO);
						contractBucket.expire(TimeUtil.randomDurationNormal());
					}
					RBitSet bitSet = redisson.getBitSet(CONTRACT_DAC_PREFIX + TimeUtil.getNowDateFormat());
					bitSet.set(contractBucket.get().getId());
				}
			} finally {
				contractLock.unlock();
			}
		}
	}

}
