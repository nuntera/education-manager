package com.mindera.mindswap.education_manager.service;

import com.mindera.mindswap.education_manager.converter.CourseConverter;
import com.mindera.mindswap.education_manager.dto.CourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateCourseDTO;
import com.mindera.mindswap.education_manager.dto.UpdateCourseDTO;
import com.mindera.mindswap.education_manager.exception.ResourceNotFoundException;
import com.mindera.mindswap.education_manager.model.Course;
import com.mindera.mindswap.education_manager.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class handling business logic for Course operations.
 * 
 * This service provides methods for:
 * - Retrieving courses (single or all)
 * - Creating new courses
 * - Updating existing courses
 * - Deleting courses
 * 
 * All operations are transactional and include proper error handling.
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * Constructs a new CourseService with required dependencies.
     * 
     * @param courseRepository Repository for Course entity operations
     */
    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves all courses from the database.
     * 
     * @return List of CourseDTO objects representing all courses
     */
    public List<CourseDTO> findAllCourses() {
        return courseRepository.findAll().stream()
                .map(CourseConverter::toDto)
                .toList();
    }

    /**
     * Retrieves a specific course by its ID.
     * 
     * @param id The ID of the course to retrieve
     * @return CourseDTO representing the found course
     * @throws ResourceNotFoundException if no course is found with the given ID
     */
    public CourseDTO findCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return CourseConverter.toDto(course);
    }

    /**
     * Creates a new course.
     * 
     * @param createCourseDTO DTO containing the course information
     * @return CourseDTO representing the created course
     */
    @Transactional
    public CourseDTO createCourse(CreateCourseDTO createCourseDTO) {
        Course course = CourseConverter.createToEntity(createCourseDTO);
        Course savedCourse = courseRepository.save(course);
        return CourseConverter.toDto(savedCourse);
    }

    /**
     * Updates an existing course.
     * 
     * @param id The ID of the course to update
     * @param updateCourseDTO DTO containing the updated course information
     * @return CourseDTO representing the updated course
     * @throws ResourceNotFoundException if no course is found with the given ID
     */
    @Transactional
    public CourseDTO updateCourse(Long id, UpdateCourseDTO updateCourseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        if (updateCourseDTO.getName() != null) {
            course.setName(updateCourseDTO.getName());
        }
        if (updateCourseDTO.getDescription() != null) {
            course.setDescription(updateCourseDTO.getDescription());
        }

        Course updatedCourse = courseRepository.save(course);
        return CourseConverter.toDto(updatedCourse);
    }

    /**
     * Deletes a course by its ID.
     * 
     * @param id The ID of the course to delete
     * @throws ResourceNotFoundException if no course is found with the given ID
     */
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}
