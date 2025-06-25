package com.example.taskapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assignee_id", referencedColumnName = "id")
    private User assignee;
}
