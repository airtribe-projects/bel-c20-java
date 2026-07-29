package org.airtribe.LearnerManagementSystemBELC20.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.airtribe.LearnerManagementSystemBELC20.entity.LearnerDTO;
import org.airtribe.LearnerManagementSystemBELC20.exception.LearnerNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.service.LearnerManagementService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
  public Learner createLearner(@Valid @RequestBody Learner learner) {
    if (learner.getLearnerName() == null || learner.getLearnerEmail() == null || learner.getLearnerPhone() == null) {
      throw new IllegalArgumentException("All fields are required");
    }
    return _learnerManagementService.createLearner(learner);
  }

  @GetMapping("/learners/{learnerId}")
  public Learner getLearnerById(@PathVariable Long learnerId) throws LearnerNotFoundException {
      return _learnerManagementService.findById(learnerId);
  }


  @GetMapping("/learners")
  public List<LearnerDTO> fetchLearnersByName(@RequestParam(value = "learnerName", required = false) String learnerName,
      @RequestParam(value = "learnerEmail", required = false) String learnerEmail) {
   List<Learner> learners =  _learnerManagementService.executeBusinessLogic(learnerName, learnerEmail);
   return _learnerManagementService.convertLearnersToDTOs(learners);
  }

  // Full update of an existing learner. 200 -> body -> updated Learner, 404 if the id does not exist.
  @PutMapping("/learners/{learnerId}")
  public Learner updateLearner(@PathVariable Long learnerId, @Valid @RequestBody Learner learner)
      throws LearnerNotFoundException {
    return _learnerManagementService.updateLearner(learnerId, learner);
  }

  // Delete an existing learner. 204 No Content on success, 404 if the id does not exist.
  @DeleteMapping("/learners/{learnerId}")
  public ResponseEntity<Void> deleteLearner(@PathVariable Long learnerId) throws LearnerNotFoundException {
    _learnerManagementService.deleteLearner(learnerId);
    return ResponseEntity.noContent().build();
  }



}

// "/learners?learnerName=test"
// "/learners?learnerEmail=test@gmail.com"
// "/learners?learnerName=test&learnerEmail=test@gmail.com"
// "/learners"