package org.formation.bd;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FournisseurPartitioner implements Partitioner {
	
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> result = new HashMap<>();
		 

       ExecutionContext context1 = new ExecutionContext();
       context1.put("fournisseurId", 1);
       result.put("partition0" , context1);
       ExecutionContext context2 = new ExecutionContext();
       context2.put("fournisseurId", 2);
       result.put("partition1" , context2);
       ExecutionContext context3 = new ExecutionContext();
       context3.put("fournisseurId", 3);
       result.put("partition2" , context3);

        return result;
	}
}
