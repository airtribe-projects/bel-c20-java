package org.airtribe.LearnerManagementSystemBELC20.service;

import java.util.List;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.airtribe.LearnerManagementSystemBELC20.repository.LearnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LearnerManagementService {

  @Autowired
  private LearnerRepository _learnerRepository;

  public Learner createLearner(Learner learner) {
    return _learnerRepository.save(learner);
  }

  public List<Learner> getAllLearner() {
    return _learnerRepository.findAll();
  }

  public Learner findById(Long learnerId) {
    return _learnerRepository.findById(learnerId).get();
  }

  public List<Learner> findByLearnerName(String learnerName) {
    return _learnerRepository.findByLearnerName(learnerName);
  }
}
