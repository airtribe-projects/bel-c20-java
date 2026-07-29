package org.airtribe.LearnerManagementSystemBELC20.repository;

import org.airtribe.LearnerManagementSystemBELC20.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
