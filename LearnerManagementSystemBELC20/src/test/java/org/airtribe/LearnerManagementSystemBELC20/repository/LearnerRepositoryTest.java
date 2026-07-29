package org.airtribe.LearnerManagementSystemBELC20.repository;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Stream;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LearnerRepositoryTest {

  @Autowired
  private LearnerRepository _learnerRepository;

//  @Test
//  public void saveLearnerSuccessfully() {
//    // ARRANGE
//    Learner learner = new Learner("test", "test@gmail.com", "1234");
//    // ACT
//    Learner savedLearner = _learnerRepository.save(learner);
//    // ASSERT
//    assert savedLearner.getLearnerId() != null;
//    Assertions.assertEquals("test", savedLearner.getLearnerName());
//    Assertions.assertEquals("test@gmail.com", savedLearner.getLearnerEmail());
//    Assertions.assertEquals("1234", savedLearner.getLearnerPhone());
//  }




  @ParameterizedTest(name = "[{index}] rejects {0}")
  @MethodSource("invalidLearners")
  @Rollback
  public void save_withInvalidInput_throwsConstraintViolation(
      String caseDescription, String learnerName, String learnerEmail, String learnerPhone) {
    // ARRANGE
    Learner learner = new Learner(learnerName, learnerEmail, learnerPhone);

    // ACT & ASSERT
    Assertions.assertThrows(
        ConstraintViolationException.class,
        () -> _learnerRepository.save(learner));
  }

  private static Stream<Arguments> invalidLearners() {
    // caseDescription, learnerName, learnerEmail, learnerPhone
    return Stream.of(
        Arguments.of("null name", null, "valid@gmail.com", "1234"),
        Arguments.of("empty name", "", "valid@gmail.com", "1234"),
        Arguments.of("null email", "test", null, "1234"),
        Arguments.of("empty email", "test", "", "1234"),
        Arguments.of("malformed email (no @)", "test", "not-an-email", "1234"),
        Arguments.of("null phone", "test", "valid@gmail.com", null),
        Arguments.of("empty phone", "test", "valid@gmail.com", ""));
  }

  @Test
  public void testFetchAllLearners() {
    List<Learner> learners =  _learnerRepository.findAll();
  }

}
