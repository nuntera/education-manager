package com.mindera.mindswap.education_manager.converter;

import com.mindera.mindswap.education_manager.dto.CourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateCourseDTO;
import com.mindera.mindswap.education_manager.model.Course;

/**
 * Utility class for converting between Course entities and DTOs.
 * 
 * This class provides static methods to convert:
 * - Course entity to CourseDTO
 * - CreateCourseDTO to Course entity
 * 
 * The class cannot be instantiated as it only contains static utility methods.
 */
public class CourseConverter {

    /**
     * Private constructor to prevent instantiation of utility class.
     * 
     * @throws IllegalStateException if an attempt is made to instantiate the class
     */
    private CourseConverter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a Course entity to a CourseDTO.
     * 
     * @param course The Course entity to convert
     * @return CourseDTO containing the course data, or null if input is null
     */
    public static CourseDTO toDto(Course course) {
        if (course == null) {
            return null;
        }
        
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        return dto;
    }

    /**
     * Converts a CreateCourseDTO to a Course entity.
     * 
     * @param dto The CreateCourseDTO containing the course data
     * @return Course entity with the data from the DTO
     * @throws IllegalArgumentException if the input DTO is null
     */
    public static Course createToEntity(CreateCourseDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CreateCourseDTO cannot be null");
        }

        Course course = new Course();
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        return course;
    }
}
