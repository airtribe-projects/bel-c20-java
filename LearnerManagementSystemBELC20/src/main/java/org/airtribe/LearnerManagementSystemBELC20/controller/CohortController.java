package org.airtribe.LearnerManagementSystemBELC20.controller;

import java.util.List;
import org.airtribe.LearnerManagementSystemBELC20.entity.Cohort;
import org.airtribe.LearnerManagementSystemBELC20.exception.CohortNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.exception.LearnerNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.service.LearnerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CohortController {

  @Autowired
  private LearnerManagementService _learnerManagementService;

  @PostMapping("/cohorts")
  public Cohort createCohort(@RequestBody Cohort cohort) {
    return _learnerManagementService.createCohort(cohort);
  }

  @PostMapping("/assignLearnersToCohorts")
  public Cohort assignLearnerToCohort(@RequestParam("learnerId") Long learnerId, @RequestParam("cohortId") Long cohortId)
      throws CohortNotFoundException, LearnerNotFoundException {
    return  _learnerManagementService.assignLearnerToCohort(learnerId, cohortId);
  }

  @GetMapping("/cohorts")
  public List<Cohort> getCohorts() {
    return _learnerManagementService.fetchAllCohorts();
  }

}
