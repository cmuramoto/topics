package com.nc.topics.spell.frontend;

import javax.enterprise.context.ApplicationScoped;

import com.nc.topics.spell.service.FastIndex;

@ApplicationScoped
public class ProcessingService {

	public OutputObject<String> process(InputObject input) {
		var result = FastIndex.hex(input.getValue());
		var out = OutputObject.<String> builder().result(result).build();
		return out;
	}
}
