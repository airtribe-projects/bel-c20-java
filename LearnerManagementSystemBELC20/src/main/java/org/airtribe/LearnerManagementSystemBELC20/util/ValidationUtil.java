package org.airtribe.LearnerManagementSystemBELC20.util;

public class ValidationUtil {

  public static boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(emailRegex);
  }
}
