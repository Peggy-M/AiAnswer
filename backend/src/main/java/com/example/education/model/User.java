package com.example.education.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String email;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
} 