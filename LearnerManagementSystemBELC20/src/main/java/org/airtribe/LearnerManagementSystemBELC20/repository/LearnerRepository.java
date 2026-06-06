package org.airtribe.LearnerManagementSystemBELC20.repository;

import java.util.List;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface LearnerRepository extends JpaRepository<Learner, Long> {

  public List<Learner> findByLearnerName(String learnerName);

  @Query("SELECT l FROM Learner l WHERE l.learnerName = ?1")
  public List<Learner> findMyWayOfFetchingLearners(String learnerName);
}
