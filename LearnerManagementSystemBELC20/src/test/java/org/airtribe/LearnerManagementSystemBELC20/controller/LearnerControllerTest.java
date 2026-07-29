package org.airtribe.LearnerManagementSystemBELC20.controller;

import java.util.ArrayList;
import java.util.List;
import org.airtribe.LearnerManagementSystemBELC20.entity.Learner;
import org.airtribe.LearnerManagementSystemBELC20.entity.LearnerDTO;
import org.airtribe.LearnerManagementSystemBELC20.service.LearnerManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class LearnerControllerTest {

  @Autowired
  private MockMvc _mockMvc;

  @MockitoBean
  private LearnerManagementService _learnerManagementService;

  @Test
  public void testCreateLearner_Succesfully() throws Exception {
    Learner learner = new Learner("test", "test@gmail.com", "1234");
    when(_learnerManagementService.createLearner(any())).thenReturn(learner);

    _mockMvc.perform(MockMvcRequestBuilders.post("/learners")
        .contentType("application/json")
        .content("{\"learnerName\":\"test\",\"learnerEmail\":\"test123@gmail.com\", \"learnerPhone\":\"1234\"}"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.learnerName").value("test"))
        .andExpect(jsonPath("$.learnerEmail").value("test@gmail.com"))
        .andExpect(jsonPath("$.learnerPhone").value("1234"));
  }

  @Test
  public void testFetchAllLearnersSuccessfully() throws Exception {
    List<LearnerDTO> learnerDTOList = new ArrayList<>();
    LearnerDTO learnerDTO = new LearnerDTO(1L, "test", "test@gmail.com", "1234");
    learnerDTOList.add(learnerDTO);

    List<Learner> learners = new ArrayList<>();
    Learner learner = new Learner(1L, "test", "test@gmail.com", "1234");
    learners.add(learner);


    when(_learnerManagementService.executeBusinessLogic(null, null)).thenReturn(learners);
    when(_learnerManagementService.convertLearnersToDTOs(learners)).thenReturn(learnerDTOList);

    _mockMvc.perform(MockMvcRequestBuilders.get("/learners").contentType("application/json"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].learnerName").value("test"));

  }
}
