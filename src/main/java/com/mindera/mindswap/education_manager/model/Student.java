package com.mindera.mindswap.education_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

/**
 * Entity class representing a student in the education management system.
 * 
 * This class maps to the 'students' table in the database and contains
 * information about students enrolled in various courses.
 * 
 * Key features:
 * - Unique identifier
 * - Student personal information (first name, last name)
 * - Contact information (email)
 * - Bidirectional relationship with courses through StudentCourse
 */
@Data
@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    /**
     * Unique identifier for the student.
     * Auto-generated using database sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Student's first name.
     * Cannot be null and has a maximum length of 50 characters.
     */
    @Column(nullable = false)
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    /**
     * Student's last name.
     * Cannot be null and has a maximum length of 50 characters.
     */
    @Column(nullable = false)
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    /**
     * Student's email address.
     * Cannot be null and has a maximum length of 100 characters.
     */
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * List of course enrollments for this student.
     * Managed through the StudentCourse join entity.
     * CascadeType.ALL ensures all operations (including delete) are cascaded to
     * enrollments.
     * orphanRemoval=true ensures that enrollments are deleted when removed from the
     * collection.
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentCourse> courses = new ArrayList<>();

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                // Avoid printing the entire collection to prevent recursion
                ", courses=" + (courses != null ? courses.size() : 0) + " courses" +
                '}';
    }
}
