package org.formation.io;

import org.formation.model.DummyRecord;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class DummyWriter implements ItemWriter<DummyRecord> {


	@Override
	public void write(Chunk<? extends DummyRecord> chunk) throws Exception {
		System.out.println(chunk.getItems());

	}
}
