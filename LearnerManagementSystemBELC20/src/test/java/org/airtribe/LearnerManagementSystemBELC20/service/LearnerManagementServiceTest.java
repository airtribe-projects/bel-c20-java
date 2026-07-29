package org.airtribe.LearnerManagementSystemBELC20.service;

import java.util.Optional;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.airtribe.LearnerManagementSystemBELC20.exception.LearnerNotFoundException;
import org.airtribe.LearnerManagementSystemBELC20.repository.LearnerRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LearnerManagementServiceTest {

  @InjectMocks
  private LearnerManagementService _learnerManagementService;

  @Mock
  private LearnerRepository _learnerRepository;

  @BeforeEach
  public void setupBeforeEach() {
    System.out.println("Running once before each unit test");
  }

  @AfterEach
  public void setupAfterEach() {
    System.out.println("Running once after each unit test");
  }

  @BeforeAll
  public static void setupBeforeAll() {
    System.out.println("Running once before all the unit tests");
  }

  @AfterAll
  public static void setupAfterAll() {
    System.out.println("Running once after all the unit tests");
  }

  @Test
  public void testCreateLearner_successfully() {

    /// ARRANGE
    Learner learner = new Learner("test", "test@gmail.com", "1234");
    when(_learnerRepository.save(learner)).thenReturn(learner);
    // ACT
    Learner createdLearner = _learnerManagementService.createLearner(learner);
    // ASSERT
    Assertions.assertEquals("test", createdLearner.getLearnerName());
    Assertions.assertEquals("test@gmail.com", createdLearner.getLearnerEmail());
    assert "1234".equals(createdLearner.getLearnerPhone());
  }

  @Test
  public void testFetchLearnerById_Successfully() throws LearnerNotFoundException {
    // ARRANGE ACT ASSERT
    Learner learner = new Learner(1L, "test", "test@gmail.com", "1234");
    when(_learnerRepository.findById(1L)).thenReturn(Optional.of(learner));
    // ACT
    Learner fetchedLearner = _learnerManagementService.findById(1L);
    // ASSERTION
    Assertions.assertEquals(fetchedLearner, learner);
    Assertions.assertEquals(1L, fetchedLearner.getLearnerId());
    Assertions.assertEquals("test", fetchedLearner.getLearnerName());
    Assertions.assertEquals("test@gmail.com", fetchedLearner.getLearnerEmail());
    Assertions.assertEquals("1234", fetchedLearner.getLearnerPhone());
  }

  @Test
  public void testFetchLeanerById_learnerNotFound() {
    when(_learnerRepository.findById(1L)).thenReturn(Optional.empty());
//    try {
//      _learnerManagementService.findById(1L);
//    } catch (LearnerNotFoundException exception) {
//      Assertions.assertEquals("Learner not found with id 1", exception.getMessage());
//    }
    LearnerNotFoundException exception = Assertions.assertThrows(LearnerNotFoundException.class, () -> {
      _learnerManagementService.findById(1L);
    });
    Assertions.assertEquals("Learner not found with id 1", exception.getMessage());
  }

}
