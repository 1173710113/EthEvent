package com.ices.ethereumevent.domain;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagePO {
	
	public static final int STATE_NOT_SEND = 0;
	
	public static final int STATE_SEND = 1;
	
	public static final int STATE_SEND_FAILED = 2;
	
	public static final int STATE_COMPLETE = 3;
	
	public static final int STATE_COMPLETE_FAILED = 4;

	private int id;
	
	private String correlationId;
	
	private String payload;
	
	private int state;
	
	private Date completeTime;
	
	private Date failTime;
}
