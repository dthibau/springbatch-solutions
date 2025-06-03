package org.formation.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JobsController {

	@Autowired
	JobExplorer jobExplorer;

	@Autowired
	JobRegistry jobRegistry;

	@Autowired
	JobOperator jobOperator;
	
	@RequestMapping("/")
	public String getAllJobs(Model model) {
		List<JobDto> list = new ArrayList<>();

		jobExplorer.getJobNames().stream().forEach(job -> {
			JobDto jobDto = new JobDto();
			jobDto.setName(job);
			JobInstance instance = jobExplorer.getLastJobInstance(job);
			jobDto.setLastJobInstance(instance);
			jobDto.setLastExecution(jobExplorer.getLastJobExecution(instance));
			list.add(jobDto);
		});
		model.addAttribute("jobs", list);

		
		return "jobs";
	}
}
