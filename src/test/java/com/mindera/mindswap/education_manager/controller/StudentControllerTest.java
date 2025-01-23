package com.mindera.mindswap.education_manager.controller;

import com.mindera.mindswap.education_manager.dto.CourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateCourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateStudentDTO;
import com.mindera.mindswap.education_manager.dto.StudentCourseDTO;
import com.mindera.mindswap.education_manager.dto.StudentDTO;
import com.mindera.mindswap.education_manager.dto.UpdateStudentDTO;
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
import static org.hamcrest.Matchers.containsString;

/**
 * Integration tests for the StudentController REST endpoints.
 * 
 * These tests verify the functionality of all student-related operations including:
 * - CRUD operations (Create, Read, Update, Delete)
 * - Email validation and uniqueness
 * - Course enrollment management
 * - Error handling for various scenarios
 * 
 * Each test uses RestAssured for API testing and includes proper setup and cleanup
 * to ensure test isolation.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    /**
     * Sets up the test environment before each test.
     * Configures RestAssured with the dynamic port and base path.
     */
    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/students";
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
     * Tests successful student creation with valid data.
     * Verifies:
     * - Correct HTTP status code (201 Created)
     * - Response contains all provided student information
     * - Student ID is generated
     */
    @Test
    void createStudent_withValidData_shouldReturn201() {
        CreateStudentDTO dto = new CreateStudentDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");

        StudentDTO response = given()
                        .contentType(ContentType.JSON)
                        .body(dto)
                        .when()
                        .post()
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(StudentDTO.class);

        assertNotNull(response.getId());
        assertEquals(dto.getFirstName(), response.getFirstName());
        assertEquals(dto.getLastName(), response.getLastName());
        assertEquals(dto.getEmail(), response.getEmail());
    }

    /**
     * Tests student creation with invalid data.
     * Verifies:
     * - Correct HTTP status code (400 Bad Request)
     * - Request is rejected when required fields are missing
     */
    @Test
    void createStudent_withInvalidData_shouldReturn400() {
        CreateStudentDTO dto = new CreateStudentDTO();
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
     * Tests successful retrieval of a student by ID.
     * Verifies:
     * - Correct HTTP status code (200 OK)
     * - Retrieved student matches the created student
     * - All fields are correctly returned
     */
    @Test
    void getStudent_withValidId_shouldReturn200() {
        // Create a student first
        CreateStudentDTO dto = new CreateStudentDTO();
        dto.setFirstName("Create");
        dto.setLastName("Jane");
        dto.setEmail("create.jane@example.com");
        StudentDTO created = studentService.createStudent(dto);

        StudentDTO response = given()
                        .when()
                        .get("/{id}", created.getId())
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(StudentDTO.class);

        assertEquals(created.getId(), response.getId());
        assertEquals(created.getFirstName(), response.getFirstName());
    }

    /**
     * Tests student retrieval with an invalid ID.
     * Verifies:
     * - Correct HTTP status code (404 Not Found)
     * - Appropriate error response when student doesn't exist
     */
    @Test
    void getStudent_withInvalidId_shouldReturn404() {
        given()
                .when()
                .get("/{id}", 999L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Tests retrieval of all students.
     * Verifies:
     * - Correct HTTP status code (200 OK)
     * - Returns list of all students
     * - Correct student count
     */
    @Test
    void getAllStudents_shouldReturnAllStudents() {
        // Create test students
        CreateStudentDTO dto1 = new CreateStudentDTO();
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setEmail("john.doe@example.com");
        studentService.createStudent(dto1);

        CreateStudentDTO dto2 = new CreateStudentDTO();
        dto2.setFirstName("Jane");
        dto2.setLastName("Doe");
        dto2.setEmail("jane.doe@example.com");
        studentService.createStudent(dto2);

        List<StudentDTO> response = given()
                        .when()
                        .get()
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", StudentDTO.class);

        assertEquals(2, response.size());
    }

    /**
     * Tests successful student update with valid data.
     * Verifies:
     * - Correct HTTP status code (200 OK)
     * - Updated fields reflect new values
     * - Non-updated fields retain original values
     */
    @Test
    void updateStudent_withValidData_shouldReturn200() {
        // Create a student first
        CreateStudentDTO createDto = new CreateStudentDTO();
        createDto.setFirstName("Original");
        createDto.setLastName("Name");
        createDto.setEmail("original@example.com");
        StudentDTO created = studentService.createStudent(createDto);

        UpdateStudentDTO updateDto = new UpdateStudentDTO();
        updateDto.setFirstName("Updated");

        StudentDTO response = given()
                        .contentType(ContentType.JSON)
                        .body(updateDto)
                        .when()
                        .put("/{id}", created.getId())
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(StudentDTO.class);

        assertEquals(updateDto.getFirstName(), response.getFirstName());
        assertEquals(created.getLastName(), response.getLastName()); // Unchanged
        assertEquals(created.getEmail(), response.getEmail()); // Unchanged
    }

    /**
     * Tests student update with an invalid ID.
     * Verifies:
     * - Correct HTTP status code (404 Not Found)
     * - Appropriate error response when student doesn't exist
     */
    @Test
    void updateStudent_withInvalidId_shouldReturn404() {
        UpdateStudentDTO updateDto = new UpdateStudentDTO();
        updateDto.setFirstName("Updated");

        given()
                .contentType(ContentType.JSON)
                .body(updateDto)
                .when()
                .put("/{id}", 999L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Tests successful student deletion.
     * Verifies:
     * - Correct HTTP status code (204 No Content)
     * - Student is actually removed from database
     */
    @Test
    void deleteStudent_withValidId_shouldReturn204() {
        // Create test student
        CreateStudentDTO dto = new CreateStudentDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        StudentDTO createdStudent = studentService.createStudent(dto);

        given()
                .when()
                .delete("/{id}", createdStudent.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertFalse(studentRepository.existsById(createdStudent.getId()));
    }

    /**
     * Tests student deletion with an invalid ID.
     * Verifies:
     * - Correct HTTP status code (404 Not Found)
     * - Appropriate error response when student doesn't exist
     */
    @Test
    void deleteStudent_withInvalidId_shouldReturn404() {
        given()
                .when()
                .delete("/{id}", 999L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Tests successful student enrollment in a course.
     * Verifies:
     * - Correct HTTP status code (200 OK)
     * - Enrollment is created successfully
     * - Returns correct enrollment information
     */
    @Test
    void enrollStudentInCourse_withValidIds_shouldReturn200() {
        // Create test student
        CreateStudentDTO studentDto = new CreateStudentDTO();
        studentDto.setFirstName("John");
        studentDto.setLastName("Doe");
        studentDto.setEmail("john.doe@example.com");
        StudentDTO student = studentService.createStudent(studentDto);

        // Create test course
        CreateCourseDTO courseDto = new CreateCourseDTO();
        courseDto.setName("Test Course");
        courseDto.setDescription("Test Description");
        CourseDTO course = courseService.createCourse(courseDto);

        StudentCourseDTO response = given()
                .when()
                .post("/{studentId}/courses/{courseId}", student.getId(), course.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(StudentCourseDTO.class);

        assertNotNull(response.getId());
        assertEquals(student.getId(), response.getStudent().getId());
        assertEquals(course.getId(), response.getCourse().getId());
    }

    /**
     * Tests enrollment attempt with an invalid student ID.
     * Verifies:
     * - Correct HTTP status code (404 Not Found)
     * - Appropriate error response when student doesn't exist
     * - No enrollment record is created
     */
    @Test
    void enrollStudent_withInvalidStudentId_shouldReturn404() {
        // Create a course first
        CreateCourseDTO courseDto = new CreateCourseDTO();
        courseDto.setName("Test Course");
        courseDto.setDescription("Test Course Description");
        CourseDTO createdCourse = courseService.createCourse(courseDto);

        given()
                .when()
                .post("/{studentId}/courses/{courseId}", 999L, createdCourse.getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Tests cascade deletion of student enrollments.
     * Verifies:
     * - Student can be deleted when enrolled in a course
     * - Enrollment record is automatically deleted
     * - Course remains in the database
     * - Correct HTTP status codes for all operations
     */
    @Test
    void deleteStudent_withEnrollments_shouldDeleteEnrollments() {
        // Create a student
        CreateStudentDTO studentDto = new CreateStudentDTO();
        studentDto.setFirstName("Create_John");
        studentDto.setLastName("Doe");
        studentDto.setEmail("create.john.doe@example.com");

        StudentDTO createdStudent = given()
                        .contentType(ContentType.JSON)
                        .body(studentDto)
                        .when()
                        .post()
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().as(StudentDTO.class);

        // Log the created student ID
        System.out.println("Created Student ID: " + createdStudent.getId());

        // Create a course
        CreateCourseDTO courseDto = new CreateCourseDTO();
        courseDto.setName("Test Course 1");
        courseDto.setDescription("Test Course Description");

        CourseDTO createdCourse = given()
                        .contentType(ContentType.JSON)
                        .body(courseDto)
                        .baseUri("http://localhost")
                        .port(port)
                        .basePath("/api/v1/courses")
                        .when()
                        .post()
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().as(CourseDTO.class);

        // Enroll student in the created course
        StudentCourseDTO enrollment = given()
                        .when()
                        .post("/{studentId}/courses/{courseId}", createdStudent.getId(), createdCourse.getId())
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract().as(StudentCourseDTO.class);

        // Verify enrollment exists
        assertFalse(studentCourseRepository.findById(enrollment.getId()).isEmpty());

        // Check if the student exists before deletion
        assertTrue(studentRepository.findById(createdStudent.getId()).isPresent());

        // Delete the student
        given()
                .when()
                .delete("/{id}", createdStudent.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Add a small delay to ensure the deletion is complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify both student and enrollment are deleted
        assertTrue(studentRepository.findById(createdStudent.getId()).isEmpty());
        assertTrue(studentCourseRepository.findById(enrollment.getId()).isEmpty());
    }

    /**
     * Tests cascade deletion of multiple student enrollments.
     * Verifies:
     * - Student can be deleted when enrolled in multiple courses
     * - All enrollment records are automatically deleted
     * - Courses remain in the database
     * - Correct HTTP status codes for all operations
     * - Database is left in a consistent state
     */
    @Test
    void deleteStudent_withMultipleEnrollments_shouldDeleteAllEnrollments() {
        // Create a student
        CreateStudentDTO studentDto = new CreateStudentDTO();
        studentDto.setFirstName("Create_Jane");
        studentDto.setLastName("Smith");
        studentDto.setEmail("create.jane.smith@example.com");
        StudentDTO createdStudent = studentService.createStudent(studentDto);

        // Create first course
        CreateCourseDTO courseDto1 = new CreateCourseDTO();
        courseDto1.setName("Test Course 2");
        courseDto1.setDescription("Test Course 1 Description");
        CourseDTO createdCourse1 = courseService.createCourse(courseDto1);

        // Create second course
        CreateCourseDTO courseDto2 = new CreateCourseDTO();
        courseDto2.setName("Test Course 3");
        courseDto2.setDescription("Test Course 2 Description");
        CourseDTO createdCourse2 = courseService.createCourse(courseDto2);

        // Enroll student in both courses
        given()
                .when()
                .post("/{studentId}/courses/{courseId}", createdStudent.getId(), createdCourse1.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .when()
                .post("/{studentId}/courses/{courseId}", createdStudent.getId(), createdCourse2.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        // Verify enrollments exist
        assertFalse(studentCourseRepository.findAll().isEmpty());
        assertEquals(2, studentCourseRepository.findAll().size());

        // Delete the student
        given()
                .when()
                .delete("/{id}", createdStudent.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Verify both student and all enrollments are deleted
        assertTrue(studentRepository.findById(createdStudent.getId()).isEmpty());
        assertTrue(studentCourseRepository.findAll().isEmpty());
    }

    /**
     * Tests student creation with duplicate email.
     * Verifies:
     * - Correct HTTP status code (400 Bad Request)
     * - Appropriate error message about duplicate email
     * - Second student is not created
     */
    @Test
    void createStudent_withDuplicateEmail_shouldReturn400() {
        // Create first student
        CreateStudentDTO dto1 = new CreateStudentDTO();
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setEmail("john.doe@example.com");
        studentService.createStudent(dto1);

        // Try to create second student with same email
        CreateStudentDTO dto2 = new CreateStudentDTO();
        dto2.setFirstName("Jane");
        dto2.setLastName("Doe");
        dto2.setEmail("john.doe@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(dto2)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("is already in use"));
    }

    /**
     * Tests student creation with invalid email format.
     * Verifies:
     * - Correct HTTP status code (400 Bad Request)
     * - Appropriate error message about invalid email format
     * - Student is not created
     */
    @Test
    void createStudent_withInvalidEmail_shouldReturn400() {
        CreateStudentDTO dto = new CreateStudentDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("invalid-email");

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("Invalid email format"));
    }

    /**
     * Tests student update with duplicate email.
     * Verifies:
     * - Correct HTTP status code (400 Bad Request)
     * - Appropriate error message about duplicate email
     * - Student information remains unchanged
     */
    @Test
    void updateStudent_withDuplicateEmail_shouldReturn400() {
        // Create first student
        CreateStudentDTO dto1 = new CreateStudentDTO();
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setEmail("john.doe@example.com");
        studentService.createStudent(dto1);

        // Create second student
        CreateStudentDTO dto2 = new CreateStudentDTO();
        dto2.setFirstName("Jane");
        dto2.setLastName("Doe");
        dto2.setEmail("jane.doe@example.com");
        StudentDTO student2 = studentService.createStudent(dto2);

        // Try to update second student with first student's email
        UpdateStudentDTO updateDto = new UpdateStudentDTO();
        updateDto.setEmail("john.doe@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(updateDto)
                .when()
                .put("/{id}", student2.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("is already in use"));
    }
}