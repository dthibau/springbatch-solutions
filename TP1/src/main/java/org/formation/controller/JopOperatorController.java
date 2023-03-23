package org.formation.controller;

import java.util.Set;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JopOperatorController {

	@Autowired
	JobOperator jobOperator;
	
	@GetMapping("/jobs")
	public String listAvailableJobs(Model model) {
		model.addAttribute("jobNames", jobOperator.getJobNames());
		
		return "jobs";
	}
	
	@GetMapping("/operator/start")
	public String startJob(@RequestParam String jobName) throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException, JobParametersNotFoundException, JobRestartException, JobExecutionAlreadyRunningException, JobInstanceAlreadyCompleteException, UnexpectedJobExecutionException {
		Long jobExecutionId = jobOperator.startNextInstance(jobName);
		
		return "redirect:/executions?jobName="+jobName;
	}
}
