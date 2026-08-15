package org.airtribe.LearnerManagementSystemBELC20.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.airtribe.LearnerManagementSystemBELC20.entity.Cohort;
import org.airtribe.LearnerManagementSystemBELC20.entity.CohortDTO;
import org.airtribe.LearnerManagementSystemBELC20.entity.CohortLearnerMappingResponse;
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

  /**
   * Demonstrates CascadeType.ALL (specifically PERSIST) on {@code Cohort.learners}: takes {@link Learner}
   * payloads and attaches them to the cohort's collection, saving only the {@link Cohort}. Hibernate
   * cascades the persist operation onto each genuinely-new Learner automatically because of the cascade
   * config on the association, inserting both the learner rows and the join-table rows in one cohort save.
   * <p>
   * Before cascading, each incoming learner is matched against existing learners by {@code learnerEmail}
   * (the closest thing to a natural key here, since a client-supplied {@code learnerId} can't be trusted -
   * see {@link #mapLearnersToCohort} for the id-based flow). Learners whose email already exists are
   * <b>not</b> re-inserted; the already-managed entity is reused instead. Two sub-cases are handled and
   * reported separately:
   * <ul>
   *   <li>Learner exists but isn't yet mapped to this cohort - the existing entity is attached to the
   *       cohort's collection (a new join-table row only, no learner insert) and counted in
   *       {@code addedLearnerIds}.</li>
   *   <li>Learner exists and is already mapped to this cohort - skipped entirely (no insert, no duplicate
   *       join row) and counted in {@code alreadyMappedLearnerIds}.</li>
   * </ul>
   * Contrast with {@link #mapLearnersToCohort}, which deliberately fetches already-managed Learner
   * entities by id so cascade/dirty-checking can't accidentally mutate or duplicate existing learners.
   */
  @Transactional
  public CohortLearnerMappingResponse createAndCascadeMapLearnersToCohort(Long cohortId, List<Learner> newLearners)
      throws CohortNotFoundException {
    if (newLearners == null || newLearners.isEmpty()) {
      throw new IllegalArgumentException("learners must not be null or empty");
    }

    Cohort cohort = fetchCohortOrThrow(cohortId);
    Set<Long> alreadyMappedIds = collectMappedLearnerIds(cohort);

    List<Long> alreadyMappedLearnerIds = new ArrayList<>();
    List<Learner> newlyMappedLearners = new ArrayList<>();
    int newlyCreatedCount = 0;

    for (Learner learner : newLearners) {
      Optional<Learner> existingLearner = _learnerRepository.findByLearnerEmail(learner.getLearnerEmail());

      if (existingLearner.isPresent()) {
        // Reuse the already-managed entity instead of cascading an insert, so a learner that already
        // exists (matched by email) never gets duplicated.
        Learner managedLearner = existingLearner.get();
        if (alreadyMappedIds.add(managedLearner.getLearnerId())) {
          newlyMappedLearners.add(managedLearner);
        } else {
          // Existing learner already mapped to this cohort - nothing to do.
          alreadyMappedLearnerIds.add(managedLearner.getLearnerId());
        }
      } else {
        // Force this to be treated as brand-new (transient), so cascade=PERSIST inserts it instead of
        // Hibernate attempting to UPDATE a row using a client-supplied id. A brand-new learner can never
        // already be mapped to this cohort, so it's always added.
        learner.setLearnerId(null);
        newlyMappedLearners.add(learner);
        newlyCreatedCount++;
      }
    }

    // Saving only the cohort here is the point of this endpoint: cascade = CascadeType.ALL on
    // Cohort.learners means Hibernate persists each genuinely new Learner as a side effect of this single
    // save, while reused existing learners are merely re-attached (no insert, no update).
    Cohort savedCohort = attachLearnersAndSave(cohort, newlyMappedLearners);

    // newlyMappedLearners holds the same object references saved above, so brand-new learners now have
    // their generated id populated, and existing learners already had theirs.
    List<Long> addedLearnerIds = new ArrayList<>();
    for (Learner learner : newlyMappedLearners) {
      addedLearnerIds.add(learner.getLearnerId());
    }

    String message = newlyCreatedCount + " new learner(s) created and mapped via cascade, "
        + (addedLearnerIds.size() - newlyCreatedCount) + " existing learner(s) (matched by email) newly "
        + "mapped to the cohort, " + alreadyMappedLearnerIds.size()
        + " existing learner(s) already mapped and skipped.";

    return new CohortLearnerMappingResponse(savedCohort.getCohortId(), savedCohort.getCohortName(),
        addedLearnerIds, alreadyMappedLearnerIds, new ArrayList<>(), message);
  }

  /**
   * Maps the given learner ids to a cohort.
   * <p>
   * - Learner ids that already have a mapping to this cohort are skipped (no duplicate rows are added).
   * - Learner ids that don't correspond to any existing learner are collected and reported back in the
   *   response rather than failing the whole request, so valid learners are still mapped.
   * - Null/blank learnerIds list is rejected as a bad request via {@link IllegalArgumentException}.
   */
  @Transactional
  public CohortLearnerMappingResponse mapLearnersToCohort(Long cohortId, List<Long> learnerIds)
      throws CohortNotFoundException {
    if (learnerIds == null || learnerIds.isEmpty()) {
      throw new IllegalArgumentException("learnerIds must not be null or empty");
    }

    Cohort cohort = fetchCohortOrThrow(cohortId);
    Set<Long> existingLearnerIds = collectMappedLearnerIds(cohort);

    // Preserve order while de-duplicating and dropping nulls from the incoming request.
    Set<Long> requestedLearnerIds = new LinkedHashSet<>();
    for (Long learnerId : learnerIds) {
      if (learnerId != null) {
        requestedLearnerIds.add(learnerId);
      }
    }

    List<Long> addedLearnerIds = new ArrayList<>();
    List<Long> alreadyMappedLearnerIds = new ArrayList<>();
    List<Long> notFoundLearnerIds = new ArrayList<>();
    List<Learner> learnersToAdd = new ArrayList<>();

    for (Long learnerId : requestedLearnerIds) {
      if (existingLearnerIds.contains(learnerId)) {
        alreadyMappedLearnerIds.add(learnerId);
        continue;
      }

      Optional<Learner> learnerOptional = _learnerRepository.findById(learnerId);
      if (!learnerOptional.isPresent()) {
        notFoundLearnerIds.add(learnerId);
        continue;
      }

      learnersToAdd.add(learnerOptional.get());
      addedLearnerIds.add(learnerId);
    }

    Cohort savedCohort = attachLearnersAndSave(cohort, learnersToAdd);

    String message = buildMappingSummaryMessage(addedLearnerIds, alreadyMappedLearnerIds, notFoundLearnerIds);

    return new CohortLearnerMappingResponse(savedCohort.getCohortId(), savedCohort.getCohortName(), addedLearnerIds,
        alreadyMappedLearnerIds, notFoundLearnerIds, message);
  }

  /**
   * Attaches the given (already-resolved) learners to the cohort's collection and saves the cohort, only
   * if there's anything new to attach - avoiding a needless write when every input learner was already
   * mapped or none were provided.
   */
  private Cohort attachLearnersAndSave(Cohort cohort, List<Learner> learnersToAttach) {
    if (learnersToAttach.isEmpty()) {
      return cohort;
    }
    cohort.getLearners().addAll(learnersToAttach);
    return _cohortRepository.save(cohort);
  }

  private Cohort fetchCohortOrThrow(Long cohortId) throws CohortNotFoundException {
    Cohort cohort = _cohortRepository.findById(cohortId)
        .orElseThrow(() -> new CohortNotFoundException("Cohort with id " + cohortId + " not found"));
    if (cohort.getLearners() == null) {
      cohort.setLearners(new ArrayList<>());
    }
    return cohort;
  }

  private Set<Long> collectMappedLearnerIds(Cohort cohort) {
    Set<Long> mappedLearnerIds = new HashSet<>();
    for (Learner learner : cohort.getLearners()) {
      mappedLearnerIds.add(learner.getLearnerId());
    }
    return mappedLearnerIds;
  }

  private String buildMappingSummaryMessage(List<Long> addedLearnerIds, List<Long> alreadyMappedLearnerIds,
      List<Long> notFoundLearnerIds) {
    StringBuilder message = new StringBuilder();
    message.append(addedLearnerIds.size()).append(" learner(s) mapped to the cohort.");

    if (!alreadyMappedLearnerIds.isEmpty()) {
      message.append(" ").append(alreadyMappedLearnerIds.size())
          .append(" learner(s) were already mapped to this cohort and were skipped: ")
          .append(alreadyMappedLearnerIds).append(".");
    }

    if (!notFoundLearnerIds.isEmpty()) {
      message.append(" ").append(notFoundLearnerIds.size())
          .append(" learner id(s) could not be found: ").append(notFoundLearnerIds).append(".");
    }

    return message.toString();
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
