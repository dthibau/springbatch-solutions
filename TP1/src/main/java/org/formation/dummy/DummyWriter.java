package org.formation.dummy;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class DummyWriter implements ItemWriter<DummyRecord> {

	@Override
	public void write(List<? extends DummyRecord> items) throws Exception {
		
		System.out.println(items);
		
	}

}
