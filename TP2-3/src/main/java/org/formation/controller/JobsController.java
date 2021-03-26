package org.formation.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JobsController {

	@Autowired
	JobExplorer jobExplorer;
	
	@RequestMapping("/")
	public String getAllJobs(Model model) {
		List<JobDto> list = new ArrayList<>();
		for ( String name : jobExplorer.getJobNames() ) {
			JobDto dto = new JobDto();
			dto.setName(name);
			JobInstance lastJobInstance = jobExplorer.getLastJobInstance(name);
			dto.setLastJobInstance(lastJobInstance);
			dto.setLastExecution(jobExplorer.getLastJobExecution(lastJobInstance));
			list.add(dto);
		}
		model.addAttribute("jobs",list);
		
		return "jobs";
	}
}
