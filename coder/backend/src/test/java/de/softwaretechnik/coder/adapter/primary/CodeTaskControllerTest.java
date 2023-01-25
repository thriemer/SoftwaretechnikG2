package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.evaluation.SolutionSubmitService;
import de.softwaretechnik.coder.application.evaluation.compiler.TemplateModifiedException;
import de.softwaretechnik.coder.adapter.secondary.UserRepository;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeTask;
import de.softwaretechnik.coder.domain.CodeEvaluation;
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
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class CodeTaskControllerTest {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository repository;

    @MockBean
    TaskService taskService;

    @MockBean
    SolutionSubmitService solutionSubmitService;

    @BeforeEach
    void setupTasks() {
        when(taskService.getAllTasks()).thenReturn(new CodeTask[]{new CodeTask("taskName", "taskDesc","","","", "psvm")});
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


    private void mockUserDatabase() {
        when(repository.findByUserName("user")).thenReturn(Optional.of(User.builder()
                .userName("username")
                .password(passwordEncoder.encode("securePassword"))
                .build()));
    }

    @Test
    void testShowHomePage() throws Exception {
        //arrange
        mockUserDatabase();
        CodeTask[] codeTasks = new CodeTask[]{new CodeTask("Task 1", "This is a task 1","" ,"","","psvm"), new CodeTask("Task 2", "This is a task 2","","","", "psvm")};
        when(taskService.getAllTasks()).thenReturn(codeTasks);
        //act, assert
        mockMvc.perform(get("/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("userName", "user"))
                .andExpect(model().attribute("tasks", codeTasks));
    }

    @Test
    public void testTemplateModifiedException() {
        Exception originalException = new Exception("original message");
        TemplateModifiedException ex = new TemplateModifiedException("Wrapper message", originalException);
        assertEquals("Wrapper message", ex.getMessage());
        assertEquals(originalException, ex.getCause());
    }


    @Test
    void testhandleTemplateModifiedException_returnsBadRequestWithErrorMessage() throws Exception {
        //arrange
        TaskController taskController = new TaskController(solutionSubmitService, taskService);
        Exception cause = new ClassNotFoundException("Class not found");
        TemplateModifiedException ex = new TemplateModifiedException("template modified", cause);
        //act
        ResponseEntity<String> response = taskController.handleTemplateModifiedException(ex);
        //assert
        assertEquals(response.getStatusCodeValue(), 400);
        assertEquals(response.getBody(), "Class not found");
    }

    @Test
    void testhandleTemplateModifiedException_WithoutCause() throws Exception {
        //arrange
        TaskController taskController = new TaskController(solutionSubmitService, taskService);
        TemplateModifiedException ex = new TemplateModifiedException("template modified", new Exception());
        //act
        ResponseEntity<String> response = taskController.handleTemplateModifiedException(ex);
        //assert
        assertEquals(response.getStatusCodeValue(), 400);
        assertEquals(response.getBody(), ex.getMessage());
    }


}