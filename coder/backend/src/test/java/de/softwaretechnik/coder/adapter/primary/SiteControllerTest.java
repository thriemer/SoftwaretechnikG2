package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.evaluation.SolutionService;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SiteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    @MockBean
    SolutionService solutionService;

    @Test
    void testShowHomePage() throws Exception {
        //arrange
        CodeTask[] codeTasks = new CodeTask[]{
                new CodeTask("Task 1", "This is a task 1", "", "", "", "psvm"),
                new CodeTask("Task 2", "This is a task 2", "", "", "", "psvm")};
        when(taskService.getAllTasks()).thenReturn(codeTasks);
        when(solutionService.getTaskAndSolution("Task 1","user")).thenReturn(new SolutionService.TaskAndSubmittedSolution(codeTasks[0],null,null));
        when(solutionService.getTaskAndSolution("Task 2","user")).thenReturn(new SolutionService.TaskAndSubmittedSolution(codeTasks[1],null,null));

        //act, assert
        mockMvc.perform(get("/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("userName", "user"))
                .andExpect(model().hasNoErrors());
    }


}
