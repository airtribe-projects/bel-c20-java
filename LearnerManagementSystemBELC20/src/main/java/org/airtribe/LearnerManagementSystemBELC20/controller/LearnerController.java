package org.airtribe.LearnerManagementSystemBELC20.controller;

import java.util.List;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.airtribe.LearnerManagementSystemBELC20.service.LearnerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LearnerController {

  @Autowired
  private LearnerManagementService _learnerManagementService;

  // RESOURCE NAME _> learners
  // VERBS -> GET, POST, PUT, DELETE
  // Input -> Body -> {"learnerName":"pawan","learnerEmail":"test", "learnerPhone":"1234"}
  // Output -> 201 -> body -> Learner -> {"learnerId":1,"learnerName":"pawan","learnerEmail":"test", "learnerPhone":"1234"}

  @PostMapping("/learners")
  public Learner createLearner(@RequestBody Learner learner) {
    return _learnerManagementService.createLearner(learner);
  }

  @GetMapping("/learners")
  public List<Learner> getAllLearners() {
    return _learnerManagementService.getAllLearner();
  }

  @GetMapping("/learners/{learnerId}")
  public Learner getLearnerById(@PathVariable Long learnerId) {
    return _learnerManagementService.findById(learnerId);
  }

  @GetMapping("/fetchLearners/{learnerName}")
  public List<Learner> getLearnerByName(@PathVariable String learnerName) {
    return _learnerManagementService.findByLearnerName(learnerName);
  }

}
