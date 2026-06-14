package org.airtribe.LearnerManagementSystemBELC20.controller;

import org.airtribe.LearnerManagementSystemBELC20.exception.CohortNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.exception.LearnerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class LearnerManagementSystemGlobalHandler {

  @ExceptionHandler(LearnerNotFoundException.class)
  public ResponseEntity handleLearnerNotFoundException(LearnerNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(CohortNotFoundException.class)
  public ResponseEntity handleCohortNotFoundException(CohortNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }
}
