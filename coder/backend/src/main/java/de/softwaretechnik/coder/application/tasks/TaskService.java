package de.softwaretechnik.coder.application.tasks;

import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.domain.CodeTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final DBAbstraction dbAbstraction;

    public CodeTask[] getAllTasks(){
        return dbAbstraction.getAllTasks();
    }

}
