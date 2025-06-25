package com.example.taskapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;
}
