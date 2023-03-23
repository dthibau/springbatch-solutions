package org.formation.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JobsController {

	@Autowired
	JobExplorer jobExplorer;
	
	@RequestMapping("/lastJobs")
	public String getLastJobs(Model model) {
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
		
		return "lastJobs";
	}
	
	@RequestMapping("/executions")
	public String getLastJobs(@RequestParam String jobName, Model model) {
		List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(jobName, 0, 10);
		List<JobExecution> result = new ArrayList<>();
		for ( JobInstance jobInstance : jobInstances) {
			result.addAll(jobExplorer.getJobExecutions(jobInstance));
		}
		model.addAttribute("executions",result);
		return "executions";
	}
}
