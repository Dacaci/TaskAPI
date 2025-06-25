package com.example.taskapi.controller;

import com.example.taskapi.model.User;
import com.example.taskapi.model.Project;
import com.example.taskapi.model.Task;
import com.example.taskapi.repository.UserRepository;
import com.example.taskapi.repository.ProjectRepository;
import com.example.taskapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    // POST /users : créer un utilisateur
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // GET /users/{id} : afficher un utilisateur
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /users/{id}/projects : projets créés par l'utilisateur
    @GetMapping("/{id}/projects")
    public List<Project> getUserProjects(@PathVariable Long id) {
        return projectRepository.findAll().stream()
                .filter(p -> p.getCreator() != null && p.getCreator().getId().equals(id))
                .toList();
    }

    // GET /users/{id}/tasks : tâches assignées à l'utilisateur
    @GetMapping("/{id}/tasks")
    public List<Task> getUserTasks(@PathVariable Long id) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getAssignee() != null && t.getAssignee().getId().equals(id))
                .toList();
    }
}
