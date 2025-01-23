package com.mindera.mindswap.education_manager.controller;

import com.mindera.mindswap.education_manager.dto.CreateStudentDTO;
import com.mindera.mindswap.education_manager.dto.StudentCourseDTO;
import com.mindera.mindswap.education_manager.dto.StudentDTO;
import com.mindera.mindswap.education_manager.dto.UpdateStudentDTO;
import com.mindera.mindswap.education_manager.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing student operations.
 * 
 * This controller provides endpoints for:
 * - Creating new students
 * - Retrieving student information (single or all)
 * - Updating existing students
 * - Deleting students
 * - Managing course enrollments
 * 
 * All endpoints are under the base path '/api/v1/students'
 */
@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "Students Controller", description = "Student management endpoints")
public class StudentController {

        private final StudentService studentService;

        /**
         * Constructs a new StudentController with required dependencies.
         * 
         * @param studentService Service for handling student business logic
         */
        @Autowired
        public StudentController(StudentService studentService) {
                this.studentService = studentService;
        }

        /**
         * Creates a new student.
         * 
         * @param createStudentDTO DTO containing the student information
         * @return ResponseEntity containing the created StudentDTO
         */
        @Operation(summary = "Create a new student")
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public StudentDTO createStudent(@Valid @RequestBody CreateStudentDTO createStudentDTO) {
                return studentService.createStudent(createStudentDTO);
        }

        /**
         * Retrieves all students.
         * 
         * @return ResponseEntity containing a list of all StudentDTOs
         */
        @Operation(summary = "Get all students")
        @GetMapping
        public List<StudentDTO> getAllStudents() {
                return studentService.findAllStudents();
        }

        /**
         * Retrieves a specific student by ID.
         * 
         * @param id The ID of the student to retrieve
         * @return ResponseEntity containing the found StudentDTO
         */
        @Operation(summary = "Get student by ID")
        @GetMapping("/{id}")
        public StudentDTO getStudentById(@PathVariable Long id) {
                return studentService.findStudentById(id);
        }

        /**
         * Updates an existing student.
         * 
         * @param id               The ID of the student to update
         * @param updateStudentDTO DTO containing the updated student information
         * @return ResponseEntity containing the updated StudentDTO
         */
        @Operation(summary = "Update student")
        @PutMapping("/{id}")
        public StudentDTO updateStudent(@PathVariable Long id, @Valid @RequestBody UpdateStudentDTO updateStudentDTO) {
                return studentService.updateStudent(id, updateStudentDTO);
        }

        /**
         * Deletes a student.
         * 
         * @param id The ID of the student to delete
         * @return ResponseEntity with no content
         */
        @Operation(summary = "Delete student")
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteStudent(@PathVariable Long id) {
                studentService.deleteStudent(id);
        }

        /**
         * Enrolls a student in a course.
         * 
         * @param studentId The ID of the student to enroll
         * @param courseId  The ID of the course to enroll in
         * @return ResponseEntity containing the enrollment details
         */
        @Operation(summary = "Enroll student in course")
        @PostMapping("/{studentId}/courses/{courseId}")
        public StudentCourseDTO enrollStudentInCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
                return studentService.enrollStudentInCourse(studentId, courseId);
        }
}
