package org.airtribe.LearnerManagementSystemBELC20.entity;

import java.util.List;


/**
 * Response payload for mapping learners to a cohort, reporting which learner ids were newly
 * mapped, which were skipped because the mapping already existed, and which learner ids could
 * not be found at all.
 */
public class CohortLearnerMappingResponse {

  private Long cohortId;

  private String cohortName;

  private List<Long> addedLearnerIds;

  private List<Long> alreadyMappedLearnerIds;

  private List<Long> notFoundLearnerIds;

  private String message;

  public CohortLearnerMappingResponse() {
  }

  public CohortLearnerMappingResponse(Long cohortId, String cohortName, List<Long> addedLearnerIds,
      List<Long> alreadyMappedLearnerIds, List<Long> notFoundLearnerIds, String message) {
    this.cohortId = cohortId;
    this.cohortName = cohortName;
    this.addedLearnerIds = addedLearnerIds;
    this.alreadyMappedLearnerIds = alreadyMappedLearnerIds;
    this.notFoundLearnerIds = notFoundLearnerIds;
    this.message = message;
  }

  public Long getCohortId() {
    return cohortId;
  }

  public void setCohortId(Long cohortId) {
    this.cohortId = cohortId;
  }

  public String getCohortName() {
    return cohortName;
  }

  public void setCohortName(String cohortName) {
    this.cohortName = cohortName;
  }

  public List<Long> getAddedLearnerIds() {
    return addedLearnerIds;
  }

  public void setAddedLearnerIds(List<Long> addedLearnerIds) {
    this.addedLearnerIds = addedLearnerIds;
  }

  public List<Long> getAlreadyMappedLearnerIds() {
    return alreadyMappedLearnerIds;
  }

  public void setAlreadyMappedLearnerIds(List<Long> alreadyMappedLearnerIds) {
    this.alreadyMappedLearnerIds = alreadyMappedLearnerIds;
  }

  public List<Long> getNotFoundLearnerIds() {
    return notFoundLearnerIds;
  }

  public void setNotFoundLearnerIds(List<Long> notFoundLearnerIds) {
    this.notFoundLearnerIds = notFoundLearnerIds;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
