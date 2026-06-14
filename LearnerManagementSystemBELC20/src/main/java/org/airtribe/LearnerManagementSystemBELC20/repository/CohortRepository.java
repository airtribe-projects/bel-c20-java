package org.airtribe.LearnerManagementSystemBELC20.repository;

import org.airtribe.LearnerManagementSystemBELC20.entity.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CohortRepository extends JpaRepository<Cohort, Long> {
}
