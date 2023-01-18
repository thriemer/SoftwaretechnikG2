package de.softwaretechnik.coder.application.login;

import de.softwaretechnik.coder.adapter.secondary.UserRepository;
import de.softwaretechnik.coder.adapter.secondary.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("!prod")
@AllArgsConstructor
public class DefaultUserCreator {

    private final UserRepository userRepository;
    private final UserService userService;

    private final TaskRepository taskRepository;

    @PostConstruct
    void initUsers(){
        if(userRepository.findByUserName("user").isEmpty()){
            userService.createUser("user","password");
        }
        if(userRepository.findByUserName("admin").isEmpty()){
            userService.createUser("admin","password");
        }
    }

    @PostConstruct
    void initTasks(){
        if(taskRepository.findByName("addTwoNumbers").isEmpty()){
            //todo
        }
    }

}
