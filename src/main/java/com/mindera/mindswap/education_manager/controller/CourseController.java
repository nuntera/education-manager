package com.mindera.mindswap.education_manager.controller;

import com.mindera.mindswap.education_manager.dto.CourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateCourseDTO;
import com.mindera.mindswap.education_manager.dto.UpdateCourseDTO;
import com.mindera.mindswap.education_manager.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing course operations.
 * 
 * This controller provides endpoints for:
 * - Creating new courses
 * - Retrieving course information (single or all)
 * - Updating existing courses
 * - Deleting courses
 * 
 * All endpoints are under the base path '/api/v1/courses'
 */
@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Courses", description = "Course management endpoints")
public class CourseController {

    private final CourseService courseService;

    /**
     * Constructs a new CourseController with required dependencies.
     * 
     * @param courseService Service for handling course business logic
     */
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Creates a new course.
     * 
     * @param createCourseDTO DTO containing the course information
     * @return ResponseEntity containing the created CourseDTO
     */
    @Operation(summary = "Create a new course")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO createCourse(@Valid @RequestBody CreateCourseDTO createCourseDTO) {
        return courseService.createCourse(createCourseDTO);
    }

    /**
     * Retrieves all courses.
     * 
     * @return ResponseEntity containing a list of all CourseDTOs
     */
    @Operation(summary = "Get all courses")
    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.findAllCourses();
    }

    /**
     * Retrieves a specific course by ID.
     * 
     * @param id The ID of the course to retrieve
     * @return ResponseEntity containing the found CourseDTO
     */
    @Operation(summary = "Get course by ID")
    @GetMapping("/{id}")
    public CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.findCourseById(id);
    }

    /**
     * Updates an existing course.
     * 
     * @param id              The ID of the course to update
     * @param updateCourseDTO DTO containing the updated course information
     * @return ResponseEntity containing the updated CourseDTO
     */
    @Operation(summary = "Update course")
    @PutMapping("/{id}")
    public CourseDTO updateCourse(@PathVariable Long id, @Valid @RequestBody UpdateCourseDTO updateCourseDTO) {
        return courseService.updateCourse(id, updateCourseDTO);
    }

    /**
     * Deletes a course.
     * 
     * @param id The ID of the course to delete
     * @return ResponseEntity with no content
     */
    @Operation(summary = "Delete course")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}
