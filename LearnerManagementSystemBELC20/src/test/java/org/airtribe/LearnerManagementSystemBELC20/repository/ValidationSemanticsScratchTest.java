package org.airtribe.LearnerManagementSystemBELC20.repository;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Throwaway verification (no Spring / no DB) that the Learner bean-validation
 * constraints reject the "invalid" rows and accept the "valid" rows used by the
 * parameterized LearnerRepositoryTest.
 */
public class ValidationSemanticsScratchTest {

  private static Validator validator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator();
  }

  private static int violations(String name, String email, String phone) {
    return validator().validate(new Learner(name, email, phone)).size();
  }

  @Test
  public void invalidRowsProduceViolations() {
    Assertions.assertTrue(violations(null, "valid@gmail.com", "1234") > 0, "null name");
    Assertions.assertTrue(violations("", "valid@gmail.com", "1234") > 0, "empty name");
    Assertions.assertTrue(violations("test", null, "1234") > 0, "null email");
    Assertions.assertTrue(violations("test", "", "1234") > 0, "empty email");
    Assertions.assertTrue(violations("test", "not-an-email", "1234") > 0, "malformed email");
    Assertions.assertTrue(violations("test", "valid@gmail.com", null) > 0, "null phone");
    Assertions.assertTrue(violations("test", "valid@gmail.com", "") > 0, "empty phone");
  }

  @Test
  public void validRowsProduceNoViolations() {
    Assertions.assertEquals(0, violations("test", "test@gmail.com", "1234"), "minimal");
    Assertions.assertEquals(0, violations("Alice Smith", "alice.smith@example.com", "9999999999"), "full");
  }
}
