package org.airtribe.LearnerManagementSystemBELC20.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.airtribe.LearnerManagementSystemBELC20.entity.Cohort;
import org.airtribe.LearnerManagementSystemBELC20.entity.CohortDTO;
import org.airtribe.LearnerManagementSystemBELC20.entity.Course;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.airtribe.LearnerManagementSystemBELC20.entity.LearnerDTO;
import org.airtribe.LearnerManagementSystemBELC20.exception.CohortNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.exception.LearnerNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.repository.CohortRepository;
import org.airtribe.LearnerManagementSystemBELC20.repository.CourseRepository;
import org.airtribe.LearnerManagementSystemBELC20.repository.LearnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LearnerManagementService {

  private static final Logger log =  LoggerFactory.getLogger(LearnerManagementService.class);

  @Autowired
  private LearnerRepository _learnerRepository;

  @Autowired
  private CohortRepository _cohortRepository;

  @Autowired
  private CourseRepository _courseRepository;

  public Learner createLearner(Learner learner) {
    return _learnerRepository.save(learner);
  }

  public List<Learner> getAllLearner() {
    return _learnerRepository.findAll();
  }

  // handle the exception
  // try catch
  // throw the exception to the layer above
//  @Cacheable(value="learners", key="#learnerId")
  public Learner findById(Long learnerId) throws LearnerNotFoundException {
    log.info("---- Cache Miss: firing database operation for learnerId=" + learnerId);
    // happy case
    // unhappy case
    Optional<Learner> learnerOptional = _learnerRepository.findById(learnerId);
    if (!learnerOptional.isPresent()) {
        throw new LearnerNotFoundException("Learner not found with id " + learnerId);
    }
    return learnerOptional.get();
  }

  // Refresh the cached entry so a subsequent GET reflects the update instead of serving a stale copy.
  @CachePut(value = "learners", key = "#learnerId")
  public Learner updateLearner(Long learnerId, Learner learner) throws LearnerNotFoundException {
    Optional<Learner> learnerOptional = _learnerRepository.findById(learnerId);
    if (!learnerOptional.isPresent()) {
      throw new LearnerNotFoundException("Learner not found with id " + learnerId);
    }
    // Update the managed entity so existing associations (e.g. cohorts) and the path id are preserved,
    // rather than blindly persisting the request body.
    Learner existingLearner = learnerOptional.get();
    existingLearner.setLearnerName(learner.getLearnerName());
    existingLearner.setLearnerEmail(learner.getLearnerEmail());
    existingLearner.setLearnerPhone(learner.getLearnerPhone());
    return _learnerRepository.save(existingLearner);
  }

  // Evict the cached entry so a deleted learner is not served from cache after removal.
  @CacheEvict(value = "learners", key = "#learnerId")
  public void deleteLearner(Long learnerId) throws LearnerNotFoundException {
    if (!_learnerRepository.existsById(learnerId)) {
      throw new LearnerNotFoundException("Learner not found with id " + learnerId);
    }
    _learnerRepository.deleteById(learnerId);
  }

  public List<Learner> findByLearnerName(String learnerName) {
    return _learnerRepository.findByLearnerName(learnerName);
  }

  public Learner findLearnerByNameAndEmail(String learnerName, String learnerEmail) {
    return _learnerRepository.findByLearnerNameAndLearnerEmail(learnerName, learnerEmail);
  }

  public Learner findByLearnerEmail(String learnerEmail) {
    return _learnerRepository.findByLearnerEmail(learnerEmail).get();
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

  public Course createCourse(Course course) {
    return _courseRepository.save(course);
  }

  public Cohort assignLearnersToCohort(Long cohortId, List<Long> learnerIds) throws CohortNotFoundException {
    Optional<Cohort> cohortOptional = _cohortRepository.findById(cohortId);
    if (!cohortOptional.isPresent()) {
      throw new CohortNotFoundException("Cohort with id " + cohortId + " not found");
    }

    Cohort fetchedCohort = cohortOptional.get();
    List<Learner> existingLearners = fetchedCohort.getLearners();
    List<Learner> learnersToAssign = new ArrayList<>();
    for (Long learnerId : learnerIds) {
      Optional<Learner> learnerOptional = _learnerRepository.findById(learnerId);
      if (learnerOptional.isPresent()) {
        for (Learner existingLearner : existingLearners) {
          if (existingLearner.getLearnerId().equals(learnerId)) {
            // Learner is already assigned to the cohort, skip to the next learner
            continue;
          }
        }
        learnersToAssign.add(learnerOptional.get());
      }
    }

    fetchedCohort.getLearners().addAll(learnersToAssign);
    return _cohortRepository.save(fetchedCohort);

  }

  @Transactional
  public Cohort createAndAssignLearnersToCohorts(Long cohortId, List<Learner> learners) throws CohortNotFoundException {
    Optional<Cohort> cohortOptional = _cohortRepository.findById(cohortId);
    if (!cohortOptional.isPresent()) {
      throw new CohortNotFoundException("Cohort with id " + cohortId + " not found");
    }

    Cohort cohort = cohortOptional.get();
//    List<Learner> managedLearners = new ArrayList<>();
//    for (Learner learner : learners) {
//      Optional<Learner> learnerOptional = _learnerRepository.findByLearnerEmail(learner.getLearnerEmail());
//      if (learnerOptional.isPresent()) {
//        managedLearners.add(learner);
//      } else {
//        _learnerRepository.save(learner);
//        managedLearners.add(learner);
//      }
//    }

    cohort.getLearners().addAll(learners);
    return _cohortRepository.save(cohort);

  }

  public Page<Cohort> fetchPaginatedAndSortedCohorts(int pageNumber, int pageSize, String sortBy, String sortDir) {
    Sort.Direction direction;
    if (sortDir.equals("asc")) {
      direction = Sort.Direction.ASC;
    } else {
      direction = Sort.Direction.DESC;
    }
    Sort sort = Sort.by(direction, sortBy);
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
    return _cohortRepository.findAll(pageRequest);
  }

}
