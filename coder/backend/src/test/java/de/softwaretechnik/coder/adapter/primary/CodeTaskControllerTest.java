package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.evaluation.SolutionService;
import de.softwaretechnik.coder.application.evaluation.SolutionSubmitService;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.CodeTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CodeTaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SolutionSubmitService solutionSubmitService;

    @MockBean
    SolutionService solutionService;

    @Test
    void listAllTasks_noLoginProvided_redirectedToLoginPage() throws Exception {
        mockMvc.perform(get("/api/task/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
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

        when(solutionSubmitService.submitSolution("user", codedSolution, "taskName")).thenReturn(new CodeEvaluation[]{new CodeEvaluation(true, "fine")});

        //act, assert
        mockMvc.perform(post("/api/task/submit?taskName=taskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(codedSolution)
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"correct\":true,\"message\":\"fine\"}]"));
    }

    @Test
    void getTaskByName_withExistingUser_taskReturned() throws Exception {
        //arrange
        var codeTask = new CodeTask("MyTask", "This is a task 1","" ,"","","psvm");
        var expected = new SolutionService.TaskAndSubmittedSolution(codeTask, null, null);
        when(solutionService.getTaskAndSolution("MyTask", "user")).thenReturn(expected);
        //act, assert
        mockMvc.perform(get("/api/task/MyTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}