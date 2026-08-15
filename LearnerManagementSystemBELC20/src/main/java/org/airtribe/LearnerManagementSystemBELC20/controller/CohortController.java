package org.airtribe.LearnerManagementSystemBELC20.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.airtribe.LearnerManagementSystemBELC20.entity.Cohort;
import org.airtribe.LearnerManagementSystemBELC20.entity.CohortLearnerMappingResponse;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.airtribe.LearnerManagementSystemBELC20.entity.LearnerListBody;
import org.airtribe.LearnerManagementSystemBELC20.exception.CohortNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.exception.LearnerNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.service.LearnerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  /**
   * Maps the given (existing) learner ids to a cohort.
   * - 404 if the cohort doesn't exist.
   * - 400 if the learnerIds list is missing/empty.
   * - Learner ids already mapped to this cohort are skipped rather than duplicated.
   * - Learner ids that don't correspond to any learner are not fatal: they are reported back in
   *   the response body under notFoundLearnerIds while the valid learners are still mapped.
   */
  @PostMapping("/cohorts/{cohortId}/learners")
  public ResponseEntity<CohortLearnerMappingResponse> mapLearnersToCohorts(@PathVariable("cohortId") Long cohortId,
      @RequestBody LearnerListBody learnerListBody) throws CohortNotFoundException {
    CohortLearnerMappingResponse response =
        _learnerManagementService.mapLearnersToCohort(cohortId, learnerListBody.getLearnerIds());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/cohorts")
  public List<Cohort> getCohorts() {
    return _learnerManagementService.fetchAllCohorts();
  }

  /**
   * Demonstrates cascading: accepts brand-new learners (no learnerId) and maps them to the cohort in
   * one call. We only save the cohort - Hibernate cascades the persist onto each new Learner because
   * Cohort.learners is configured with cascade = CascadeType.ALL.
   * - 404 if the cohort doesn't exist.
   * - 400 if the learners list is missing/empty, or if any learner fails bean validation
   *   (blank name/email/phone, malformed email, etc.).
   */
  @PostMapping("/cohorts/{cohortId}/learners/cascade-demo")
  public ResponseEntity<CohortLearnerMappingResponse> createAndCascadeMapLearnersToCohort(
      @PathVariable("cohortId") Long cohortId, @RequestBody @Valid List<Learner> learners)
      throws CohortNotFoundException {
    CohortLearnerMappingResponse response =
        _learnerManagementService.createAndCascadeMapLearnersToCohort(cohortId, learners);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

}
