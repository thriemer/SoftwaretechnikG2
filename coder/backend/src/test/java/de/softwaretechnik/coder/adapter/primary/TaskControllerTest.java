package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.adapter.secondary.UserRepository;
import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.application.SolutionService;
import de.softwaretechnik.coder.domain.Task;
import de.softwaretechnik.coder.domain.TestResult;
import de.softwaretechnik.coder.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository repository;

    @MockBean
    DBAbstraction dbAbstraction;

    @MockBean
    SolutionService solutionService;

    @BeforeEach
    void setupTasks() {
        when(solutionService.getAllTasks()).thenReturn(new Task[]{new Task("taskName", "taskDesc", "psvm")});
    }

    @Test
    void listAllTasks_noLoginProvided_redirectedToLoginPage() throws Exception {
        mockMvc.perform(get("/api/task/listAll"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void listAllTasks_withExistingUser_atLeastOneTaskReturned() throws Exception {
        //arrange
        mockUserDatabase();
        //act, assert
        mockMvc.perform(get("/api/task/listAll")
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void submitTask_noLoginProvided_redirectedToLoginPage() throws Exception {
        mockMvc.perform(get("/api/task/submit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void sumbitTask_withExistingUser_evaluationReturned() throws Exception {
        //arrange
        String codedSolution = "psvm(){sout(myFancyCode);}";
        mockUserDatabase();

        when(solutionService.testResults(codedSolution,"taskName")).thenReturn(new TestResult[]{new TestResult(true,"fine")});

        //act, assert
        mockMvc.perform(post("/api/task/submit?taskName=taskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(codedSolution)
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"correct\":true,\"message\":\"fine\"}]"));
    }


    private void mockUserDatabase() {
        when(repository.findByUserName("user")).thenReturn(Optional.of(User.builder()
                .userName("username")
                .password(passwordEncoder.encode("securePassword"))
                .build()));
    }

}