package com.mindera.mindswap.education_manager.converter;

import com.mindera.mindswap.education_manager.dto.StudentCourseDTO;
import com.mindera.mindswap.education_manager.model.StudentCourse;

/**
 * Utility class for converting between StudentCourse entities and DTOs.
 * 
 * This class provides static methods to convert StudentCourse entities
 * to their DTO representation. It uses StudentConverter and CourseConverter
 * to handle the nested conversions of Student and Course entities.
 * 
 * The class cannot be instantiated as it only contains static utility methods.
 */
public class StudentCourseConverter {

    /**
     * Private constructor to prevent instantiation of utility class.
     * 
     * @throws IllegalStateException if an attempt is made to instantiate the class
     */
    private StudentCourseConverter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a StudentCourse entity to a StudentCourseDTO.
     * Also converts the associated Student and Course entities using their
     * respective converters.
     * 
     * @param studentCourse The StudentCourse entity to convert
     * @return StudentCourseDTO containing the enrollment data, or null if input is null
     */
    public static StudentCourseDTO toDto(StudentCourse studentCourse) {
        if (studentCourse == null) {
            return null;
        }

        StudentCourseDTO dto = new StudentCourseDTO();
        dto.setId(studentCourse.getId());
        dto.setStudent(StudentConverter.toDto(studentCourse.getStudent()));
        dto.setCourse(CourseConverter.toDto(studentCourse.getCourse()));
        return dto;
    }
} 