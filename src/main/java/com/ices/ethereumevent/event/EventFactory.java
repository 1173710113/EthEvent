package com.ices.ethereumevent.event;

import java.util.List;
import java.util.stream.Collectors;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

public class EventFactory {

	public static Event get(String name, List<ParameterType> parameterTypes) {
		
		List<TypeReference<?>> typeReferences = parameterTypes.stream().map(x -> getTypeReference(x)).collect(Collectors.toList());
		return new Event(name, typeReferences);
	}
	
	
	public static TypeReference<?> getTypeReference(ParameterType type) {
		switch (type.getType()) {
		case "ADDRESS":
			return new TypeReference<Address>(type.isIndexed()) {};
		case "UINT256":
			return new TypeReference<Uint256>(type.isIndexed()) {};
		case "UINT8":
			return new TypeReference<Uint8>(type.isIndexed()) {};
		case "STRING":
			return new TypeReference<Utf8String>(type.isIndexed()) {};
		}
		return null;
	}
}
