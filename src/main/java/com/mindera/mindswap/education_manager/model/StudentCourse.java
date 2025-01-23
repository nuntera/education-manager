package com.mindera.mindswap.education_manager.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity class representing the relationship between students and courses.
 * 
 * This class maps to the 'student_courses' table in the database and serves
 * as a join table to implement the many-to-many relationship between
 * students and courses. It allows tracking student enrollments in courses.
 * 
 * Key features:
 * - Unique identifier
 * - Many-to-One relationship with Student
 * - Many-to-One relationship with Course
 * - Composite unique constraint on student and course IDs
 */
@Data
@Entity
@Table(
    name = "student_courses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id"})
    },
    indexes = {
        @Index(name = "idx_student_course", columnList = "student_id,course_id")
    }
)
public class StudentCourse {

    /**
     * Unique identifier for the student-course relationship.
     * Auto-generated using database sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The student enrolled in the course.
     * Many-to-One relationship with the Student entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /**
     * The course in which the student is enrolled.
     * Many-to-One relationship with the Course entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Override
    public String toString() {
        return "StudentCourse{" +
                "id=" + id +
                ", studentId=" + (student != null ? student.getId() : "null") +
                ", courseId=" + (course != null ? course.getId() : "null") +
                '}';
    }
}
