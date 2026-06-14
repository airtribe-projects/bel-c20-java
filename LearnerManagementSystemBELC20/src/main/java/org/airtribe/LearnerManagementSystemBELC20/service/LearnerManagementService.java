package org.airtribe.LearnerManagementSystemBELC20.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.airtribe.LearnerManagementSystemBELC20.entity.Cohort;
import org.airtribe.LearnerManagementSystemBELC20.entity.CohortDTO;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.airtribe.LearnerManagementSystemBELC20.entity.LearnerDTO;
import org.airtribe.LearnerManagementSystemBELC20.exception.CohortNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.exception.LearnerNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.repository.CohortRepository;
import org.airtribe.LearnerManagementSystemBELC20.repository.LearnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LearnerManagementService {

  @Autowired
  private LearnerRepository _learnerRepository;

  @Autowired
  private CohortRepository _cohortRepository;

  public Learner createLearner(Learner learner) {
    return _learnerRepository.save(learner);
  }

  public List<Learner> getAllLearner() {
    return _learnerRepository.findAll();
  }

  // handle the exception
  // try catch
  // throw the exception to the layer above
  public Learner findById(Long learnerId) throws LearnerNotFoundException {
    // happy case
    // unhappy case
    Optional<Learner> learnerOptional = _learnerRepository.findById(learnerId);
    if (!learnerOptional.isPresent()) {
        throw new LearnerNotFoundException("Learner not found with id " + learnerId);
    }
    return learnerOptional.get();
  }

  public List<Learner> findByLearnerName(String learnerName) {
    return _learnerRepository.findByLearnerName(learnerName);
  }

  public Learner findLearnerByNameAndEmail(String learnerName, String learnerEmail) {
    return _learnerRepository.findByLearnerNameAndLearnerEmail(learnerName, learnerEmail);
  }

  public Learner findByLearnerEmail(String learnerEmail) {
    return _learnerRepository.findByLearnerEmail(learnerEmail);
  }

  public List<Learner> executeBusinessLogic(String learnerName, String learnerEmail) {
    if (learnerName == null && learnerEmail == null) {
      return getAllLearner();
    }

    if (learnerName != null && learnerEmail != null) {
      return List.of(findLearnerByNameAndEmail(learnerName, learnerEmail));
    }

    if (learnerName != null) {
      return findByLearnerName(learnerName);
    }


    return List.of(findByLearnerEmail(learnerEmail));
  }

  public Cohort createCohort(Cohort cohort) {
    return _cohortRepository.save(cohort);
  }

  public Cohort assignLearnerToCohort(Long learnerId, Long cohortId)
      throws CohortNotFoundException, LearnerNotFoundException {
    Optional<Cohort> cohortOptional = _cohortRepository.findById(cohortId);

    if (!cohortOptional.isPresent()) {
      throw new CohortNotFoundException("Cohort with id " + cohortId + " not found");
    }

    Cohort fetchedCohort = cohortOptional.get();

    Optional<Learner> learnerOptional = _learnerRepository.findById(learnerId);

    if (!learnerOptional.isPresent()) {
      throw  new LearnerNotFoundException("Learner with id " + learnerId + " not found");
    }

    Learner fetchedLearner = learnerOptional.get();

    fetchedCohort.getLearners().add(fetchedLearner);

    return _cohortRepository.save(fetchedCohort);
  }

  public List<Cohort> fetchAllCohorts() {
    return _cohortRepository.findAll();
  }

  public List<LearnerDTO> convertLearnersToDTOs(List<Learner> learners) {
    List<LearnerDTO> learnerDTOS  = new ArrayList<>();
    for (Learner learner : learners) {
      LearnerDTO learnerDTO = new LearnerDTO();
      learnerDTO.setLearnerId(learner.getLearnerId());
      learnerDTO.setLearnerName(learner.getLearnerName());
      learnerDTO.setLearnerEmail(learner.getLearnerEmail());
      learnerDTO.setLearnerPhone(learner.getLearnerPhone());
      List<CohortDTO> cohortDTOS = new ArrayList<>();
      for (Cohort cohort : learner.getCohorts()) {
        CohortDTO cohortDTO = new CohortDTO();
        cohortDTO.setCohortId(cohort.getCohortId());
        cohortDTO.setCohortName(cohort.getCohortName());
        cohortDTO.setCohortDescription(cohort.getCohortDescription());
        cohortDTOS.add(cohortDTO);
      }
      learnerDTO.setCohorts(cohortDTOS);
      learnerDTOS.add(learnerDTO);
    }
    return learnerDTOS;
  }
}
