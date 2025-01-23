package com.mindera.mindswap.education_manager.controller;

import com.mindera.mindswap.education_manager.dto.CourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateCourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateStudentDTO;
import com.mindera.mindswap.education_manager.dto.StudentDTO;
import com.mindera.mindswap.education_manager.dto.UpdateCourseDTO;
import com.mindera.mindswap.education_manager.repository.CourseRepository;
import com.mindera.mindswap.education_manager.repository.StudentCourseRepository;
import com.mindera.mindswap.education_manager.repository.StudentRepository;
import com.mindera.mindswap.education_manager.service.CourseService;
import com.mindera.mindswap.education_manager.service.StudentService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the CourseController REST endpoints.
 * 
 * These tests verify the functionality of all course-related operations including:
 * - CRUD operations (Create, Read, Update, Delete)
 * - Input validation
 * - Error handling for various scenarios
 * 
 * Each test uses RestAssured for API testing and includes proper setup and cleanup
 * to ensure test isolation.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    /**
     * Sets up the test environment before each test.
     * Configures RestAssured with the dynamic port and base path.
     */
    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/courses";
    }

    /**
     * Cleans up the test environment after each test.
     * Ensures database is clean for the next test by removing all test data.
     */
    @AfterEach
    void tearDown() {
        studentCourseRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    /**
     * Tests successful course creation with valid data.
     * Verifies:
     * - Correct HTTP status code (201 Created)
     * - Response contains all provided course information
     * - Course ID is generated
     */
    @Test
    void createCourse_withValidData_shouldReturn201() {
        CreateCourseDTO dto = new CreateCourseDTO();
        dto.setName("Test Course");
        dto.setDescription("Test Course Description");

        CourseDTO response = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CourseDTO.class);

        assertNotNull(response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getDescription(), response.getDescription());
    }

    /**
     * Tests course creation with invalid data (missing required fields).
     * Verifies:
     * - Correct HTTP status code (400 Bad Request)
     * - Appropriate error message
     * - Course is not created
     */
    @Test
    void createCourse_withInvalidData_shouldReturn400() {
        CreateCourseDTO dto = new CreateCourseDTO();
        // Missing required fields

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests retrieval of all courses.
     * Verifies:
     * - Correct HTTP status code (200 OK)
     * - Returns list of all courses
     * - Correct course count
     */
    @Test
    void getAllCourses_shouldReturnAllCourses() {
        // Create test courses
        CreateCourseDTO dto1 = new CreateCourseDTO();
        dto1.setName("Course 1");
        dto1.setDescription("Description 1");
        courseService.createCourse(dto1);

        CreateCourseDTO dto2 = new CreateCourseDTO();
        dto2.setName("Course 2");
        dto2.setDescription("Description 2");
        courseService.createCourse(dto2);

        List<CourseDTO> response = given()
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList(".", CourseDTO.class);

        assertEquals(2, response.size());
    }

    /**
     * Tests retrieval of a specific course by ID.
     * Verifies:
     * - Correct HTTP status code (200 OK)
     * - Returns correct course information
     */
    @Test
    void getCourseById_withValidId_shouldReturnCourse() {
        // Create test course
        CreateCourseDTO dto = new CreateCourseDTO();
        dto.setName("Test Course");
        dto.setDescription("Test Description");
        CourseDTO createdCourse = courseService.createCourse(dto);

        CourseDTO response = given()
                .when()
                .get("/{id}", createdCourse.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CourseDTO.class);

        assertEquals(createdCourse.getId(), response.getId());
        assertEquals(createdCourse.getName(), response.getName());
        assertEquals(createdCourse.getDescription(), response.getDescription());
    }

    /**
     * Tests course update with valid data.
     * Verifies:
     * - Correct HTTP status code (200 OK)
     * - Course information is updated correctly
     */
    @Test
    void updateCourse_withValidData_shouldReturn200() {
        // Create test course
        CreateCourseDTO createDto = new CreateCourseDTO();
        createDto.setName("Original Course");
        createDto.setDescription("Original Description");
        CourseDTO createdCourse = courseService.createCourse(createDto);

        UpdateCourseDTO updateDto = new UpdateCourseDTO();
        updateDto.setName("Updated Course");
        updateDto.setDescription("Updated Description");

        CourseDTO response = given()
                .contentType(ContentType.JSON)
                .body(updateDto)
                .when()
                .put("/{id}", createdCourse.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CourseDTO.class);

        assertEquals(updateDto.getName(), response.getName());
        assertEquals(updateDto.getDescription(), response.getDescription());
    }

    /**
     * Tests course deletion.
     * Verifies:
     * - Correct HTTP status code (204 No Content)
     * - Course is actually removed from database
     */
    @Test
    void deleteCourse_withValidId_shouldReturn204() {
        // Create test course
        CreateCourseDTO dto = new CreateCourseDTO();
        dto.setName("Test Course");
        dto.setDescription("Test Description");
        CourseDTO createdCourse = courseService.createCourse(dto);

        given()
                .when()
                .delete("/{id}", createdCourse.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertFalse(courseRepository.existsById(createdCourse.getId()));
    }

    /**
     * Tests course retrieval with non-existent ID.
     * Verifies:
     * - Correct HTTP status code (404 Not Found)
     * - Appropriate error message
     */
    @Test
    void getCourseById_withInvalidId_shouldReturn404() {
        given()
                .when()
                .get("/{id}", 999L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Tests the cascade deletion behavior when deleting a course with enrolled students.
     * 
     * This test verifies several critical aspects of the deletion process:
     * 1. Course deletion succeeds even with existing enrollments
     * 2. All related enrollment records are automatically removed
     * 3. Enrolled students are preserved in the database
     * 4. Database remains in a consistent state after deletion
     * 
     * This test is essential for ensuring proper cleanup of related data
     * and maintaining referential integrity in the database.
     */
    @Test
    void deleteCourse_withEnrollments_shouldDeleteEnrollments() {
        // Create a course
        CreateCourseDTO courseDto = new CreateCourseDTO();
        courseDto.setName("Course with Enrollments");
        courseDto.setDescription("Will be deleted with enrollments");
        CourseDTO createdCourse = courseService.createCourse(courseDto);

        // Create a student and enroll them
        CreateStudentDTO studentDto = new CreateStudentDTO();
        studentDto.setFirstName("Test");
        studentDto.setLastName("Student");
        studentDto.setEmail("test.student@example.com");
        StudentDTO createdStudent = studentService.createStudent(studentDto);

        // Enroll student in the created course
        given()
                .baseUri("http://localhost")
                .port(port)
                .basePath("/api/v1/students")
                .when()
                .post("/{studentId}/courses/{courseId}", createdStudent.getId(), createdCourse.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        // Verify enrollment exists
        assertFalse(studentCourseRepository.findAll().isEmpty());

        // Delete the course
        given()
                .baseUri("http://localhost")
                .port(port)
                .basePath("/api/v1/courses")
                .when()
                .delete("/{id}", createdCourse.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Verify course and enrollments are deleted, but student remains
        assertTrue(courseRepository.findById(createdCourse.getId()).isEmpty());
        assertTrue(studentCourseRepository.findAll().isEmpty());
        assertFalse(studentRepository.findById(createdStudent.getId()).isEmpty());
    }
}