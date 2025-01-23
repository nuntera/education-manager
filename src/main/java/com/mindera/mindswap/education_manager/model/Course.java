package com.mindera.mindswap.education_manager.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

/**
 * Entity class representing a course in the education management system.
 * 
 * This class maps to the 'courses' table in the database and contains
 * information about educational courses offered to students.
 * 
 * Key features:
 * - Unique identifier
 * - Course name (max 100 characters)
 * - Course description (max 1000 characters)
 * - Bidirectional relationship with students through StudentCourse
 */
@Data
@Entity
@Table(name = "courses")
public class Course {
    
    /**
     * Unique identifier for the course.
     * Auto-generated using database sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Name of the course.
     * Cannot be null and has a maximum length of 100 characters.
     */
    @Column(nullable = false, length = 100)
    private String name;
    
    /**
     * Detailed description of the course.
     * Cannot be null and has a maximum length of 1000 characters.
     */
    @Column(nullable = false, length = 1000)
    private String description;

    /**
     * List of student enrollments in this course.
     * Managed through the StudentCourse join entity.
     * Cascade operations will affect related StudentCourse entries.
     */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentCourse> students = new ArrayList<>();
}
