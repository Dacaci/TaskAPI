package com.example.taskapi.controller;

import com.example.taskapi.model.Project;
import com.example.taskapi.model.Task;
import com.example.taskapi.model.User;
import com.example.taskapi.repository.ProjectRepository;
import com.example.taskapi.repository.TaskRepository;
import com.example.taskapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    // POST /projects : créer un projet (avec l'ID du créateur)
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequest request) {
        Optional<User> creator = userRepository.findById(request.creatorId);
        if (creator.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Project project = new Project();
        project.setName(request.name);
        project.setCreator(creator.get());
        return ResponseEntity.ok(projectRepository.save(project));
    }

    // GET /projects/{id} : détails d'un projet avec ses tâches
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetails> getProject(@PathVariable Long id) {
        Optional<Project> projectOpt = projectRepository.findById(id);
        if (projectOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Project project = projectOpt.get();
        List<Task> tasks = taskRepository.findAll().stream()
                .filter(t -> t.getProject() != null && t.getProject().getId().equals(id))
                .toList();
        ProjectDetails details = new ProjectDetails(project, tasks);
        return ResponseEntity.ok(details);
    }

    // GET /projects/{id}/tasks : lister les tâches d'un projet
    @GetMapping("/{id}/tasks")
    public List<Task> getProjectTasks(@PathVariable Long id) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getProject() != null && t.getProject().getId().equals(id))
                .toList();
    }

    // DTO pour la création de projet
    public static class ProjectRequest {
        public String name;
        public Long creatorId;
    }

    // DTO pour les détails d'un projet
    public static class ProjectDetails {
        public Project project;
        public List<Task> tasks;
        public ProjectDetails(Project project, List<Task> tasks) {
            this.project = project;
            this.tasks = tasks;
        }
    }
}
