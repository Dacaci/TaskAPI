package com.example.taskapi.controller;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.model.Project;
import com.example.taskapi.model.User;
import com.example.taskapi.repository.TaskRepository;
import com.example.taskapi.repository.ProjectRepository;
import com.example.taskapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    // POST /tasks : créer une tâche
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request) {
        Optional<Project> projectOpt = projectRepository.findById(request.projectId);
        Optional<User> assigneeOpt = userRepository.findById(request.assigneeId);
        if (projectOpt.isEmpty() || assigneeOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Task task = new Task();
        task.setTitle(request.title);
        task.setStatus(TaskStatus.TODO);
        task.setProject(projectOpt.get());
        task.setAssignee(assigneeOpt.get());
        return ResponseEntity.ok(taskRepository.save(task));
    }

    // PATCH /tasks/{id} : modifier le statut
    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task task = taskOpt.get();
        try {
            TaskStatus newStatus = TaskStatus.valueOf(request.status);
            task.setStatus(newStatus);
            return ResponseEntity.ok(taskRepository.save(task));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DTO pour la création de tâche
    public static class TaskRequest {
        public String title;
        public Long projectId;
        public Long assigneeId;
    }

    // DTO pour la modification du statut
    public static class StatusRequest {
        public String status;
    }
}
