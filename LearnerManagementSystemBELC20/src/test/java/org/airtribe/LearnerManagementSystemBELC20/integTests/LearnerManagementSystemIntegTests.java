package org.airtribe.LearnerManagementSystemBELC20.integTests;

import java.util.ArrayList;
import java.util.List;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.airtribe.LearnerManagementSystemBELC20.entity.LearnerDTO;
import org.airtribe.LearnerManagementSystemBELC20.repository.CohortRepository;
import org.airtribe.LearnerManagementSystemBELC20.repository.LearnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class LearnerManagementSystemIntegTests {

  @Autowired
  private MockMvc _mockMvc;

  @Autowired
  private LearnerRepository _learnerRepository;

  @Autowired
  private CohortRepository _cohortRepository;


  @BeforeEach
  public void setup() {
    // Any setup code if needed
    _learnerRepository.deleteAll();
  }

  @AfterEach
  public void tearDown() {
    // Any cleanup code if needed
    _learnerRepository.deleteAll();
  }

  @Test
  public void testCreateLearner_Succesfully() throws Exception {
    Learner learner = new Learner("test", "test@gmail.com", "1234");

    _mockMvc.perform(MockMvcRequestBuilders.post("/learners")
            .contentType("application/json")
            .content("{\"learnerName\":\"test\",\"learnerEmail\":\"test@gmail.com\", \"learnerPhone\":\"1234\"}"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.learnerName").value("test"))
        .andExpect(jsonPath("$.learnerEmail").value("test@gmail.com"))
        .andExpect(jsonPath("$.learnerPhone").value("1234"));
  }

  @Test
  public void testFetchAllLearnersSuccessfully() throws Exception {
    _mockMvc.perform(MockMvcRequestBuilders.get("/learners").contentType("application/json"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].learnerName").value("test"));

  }

}
