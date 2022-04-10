package com.nc.topics.spell.frontend;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class OutputObject<T> {

	private T result;

	private String requestId;

}
