package com.mindera.mindswap.education_manager.controller;

import com.mindera.mindswap.education_manager.dto.CreateCourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateStudentDTO;
import com.mindera.mindswap.education_manager.dto.StudentDTO;
import com.mindera.mindswap.education_manager.service.StudentService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)

class StudentControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        studentService.deleteAllStudents();
    }

    @Autowired
    private StudentService studentService;


    @Test
    void getAllStudents() {
        // Create a student
        CreateStudentDTO student = new CreateStudentDTO("John", "Doe", "john.doe@example.com");
        studentService.createStudent(student);

        List<StudentDTO> students = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/students")
                .then().statusCode(200)
                .extract().body()
                .jsonPath().getList(".", StudentDTO.class);

        assertEquals(students.size(), studentService.findAllStudents().size());
    }

    @Test
    void getStudentById() {
        // Create a student
        CreateStudentDTO student = new CreateStudentDTO("John", "Doe", "john.doe@example.com");
        StudentDTO createdStudent = studentService.createStudent(student);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/students/" + createdStudent.getId())
                .then().statusCode(200)
                .extract().body()
                .as(StudentDTO.class);

        assertEquals(createdStudent.getFirstName(), student.getFirstName());
        assertEquals(createdStudent.getLastName(), student.getLastName());
        assertEquals(createdStudent.getEmail(), student.getEmail());
    }

    private List<CreateCourseDTO> createCourses() {
        CreateCourseDTO course1 = new CreateCourseDTO("Mathematics", "Study of numbers and patterns.");
        CreateCourseDTO course2 = new CreateCourseDTO("Science", "Study of matter and energy and their interactions.");

        List<CreateCourseDTO> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        return courses;
    }
}
