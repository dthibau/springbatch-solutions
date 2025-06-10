package org.formation.io;

import org.formation.model.DummyRecord;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@JobScope
@Component
public class DummyReader implements ItemReader<DummyRecord> {

	private int i =0;
	private int nbIterations=100;
	

	@Override
	public DummyRecord read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if ( i < nbIterations ) {
			i++;
			return new DummyRecord();
		}
		
		return null;
	}

	

}
