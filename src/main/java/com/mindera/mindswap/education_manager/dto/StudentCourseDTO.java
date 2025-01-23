package com.mindera.mindswap.education_manager.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing student course enrollment information.
 * 
 * This class is used to transfer enrollment data between the service layer
 * and the client. It includes both student and course information to provide
 * a complete view of the enrollment relationship.
 * 
 * Properties:
 * - id: Unique identifier of the enrollment
 * - student: Student involved in the enrollment
 * - course: Course in which the student is enrolled
 */
@Data
public class StudentCourseDTO {
    /**
     * Unique identifier of the enrollment.
     */
    private Long id;

    /**
     * Student involved in the enrollment.
     * Contains basic student information through StudentDTO.
     */
    private StudentDTO student;

    /**
     * Course in which the student is enrolled.
     * Contains basic course information through CourseDTO.
     */
    private CourseDTO course;
}
