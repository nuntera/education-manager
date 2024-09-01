package com.mindera.mindswap.education_manager.controller;

import com.mindera.mindswap.education_manager.dto.CourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateCourseDTO;
import com.mindera.mindswap.education_manager.service.CourseService;
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
class CourseControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        courseService.deleteAllCourses();
    }

    // Tests for edge cases names and descriptions
    @Test
    void creteCourse_withEmptyNameAndDescription_shouldReturn400(){
        // Create course with empty name and description
        CreateCourseDTO course = new CreateCourseDTO("", "");

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(course)
                .when()
                .post("/api/v1/courses")
                .then()
                .statusCode(400);
    }

    @Test
    void creteCourse_withWhitespacesNameAndDescription_shouldReturn400() {
        // Create course with whitespaces name and description
        CreateCourseDTO course = new CreateCourseDTO("    ", "    ");

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(course)
                .when()
                .post("/api/v1/courses")
                .then()
                .statusCode(400);
    }

    @Test
    void creteCourse_withNullNameAndDescription_shouldReturn400() {
        // Create course with null name and description
        CreateCourseDTO course = new CreateCourseDTO(null, null);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(course)
                .when()
                .post("/api/v1/courses")
                .then()
                .statusCode(400);
    }

    @Test
    void crateCourse_withVeryLongName_shouldReturn400() {
        // Create course with very long name and description
        String longName = "A".repeat(51);
        String description = "Study of numbers and patterns.";
        CreateCourseDTO course = new CreateCourseDTO(longName, description);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(course)
                .when()
                .post("/api/v1/courses")
                .then()
                .statusCode(400);
    }

    @Test
    void crateCourse_withVeryLongDescription_shouldReturn400() {
        // Create course with very long description
        String name = "Mathematics";
        String longDescription = "B".repeat(1001);
        CreateCourseDTO course = new CreateCourseDTO(name, longDescription);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(course)
                .when()
                .post("/api/v1/courses")
                .then()
                .statusCode(400);
    }

    @Test
    void createMultipleCourses_shouldReturn201() {
        // Create courses
        List<CreateCourseDTO> courses = createCourses();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(courses.get(0))
                .when().post("/api/v1/courses")
                .then()
                .statusCode(201);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(courses.get(1))
                .when().post("/api/v1/courses")
                .then()
                .statusCode(201);
    }

    @Test
    void createMultipleCourses_andGetAll_shouldSucceed() {
        // Create courses
        List<CreateCourseDTO> courses = createCourses();
        courses.forEach(course -> courseService.createCourse(course));

        List<CourseDTO> courseDTOS = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/v1/courses")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList(".", CourseDTO.class);

        assertEquals(2, courses.size());
        assertEquals(courses.size(), courseDTOS.size());
        assertEquals(courses.get(0).getName(), courseDTOS.get(0).getName());
        assertEquals(courses.get(0).getDescription(), courseDTOS.get(0).getDescription());
        assertEquals(courses.get(1).getName(), courseDTOS.get(1).getName());
        assertEquals(courses.get(1).getDescription(), courseDTOS.get(1).getDescription());
    }

    @Test
    void getAllCourses_whenNoneExist_shouldReturnEmptyList() {
        List<CourseDTO> courses = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/v1/courses")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList(".", CourseDTO.class);

        assertNotNull(courses);
        assertTrue(courses.isEmpty());
    }

    @Test
    void getAllCourses_shouldReturn200() {
        // Create courses
        List<CreateCourseDTO> courses = createCourses();
        courses.forEach(course -> courseService.createCourse(course));

        List<CourseDTO> courseDTOS = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/v1/courses")
                .then().statusCode(200)
                .extract()
                .body()
                .jsonPath().getList(".", CourseDTO.class);

        assertNotNull(courseDTOS);
        assertFalse(courseDTOS.isEmpty());
        assertEquals(courseDTOS.size(), courseService.findAllCourses().size());
    }

    @Test
    void getCourseById_shouldReturn200() {
        // Create courses
        List<CreateCourseDTO> courses = createCourses();
        CourseDTO createdCourse1 = courseService.createCourse(courses.get(0));
        CourseDTO createdCourse2 = courseService.createCourse(courses.get(1));


        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/v1/courses/" + createdCourse1.getId())
                .then().statusCode(200)
                .extract()
                .body()
                .as(CourseDTO.class);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/v1/courses/" + createdCourse2.getId())
                .then().statusCode(200)
                .extract()
                .body()
                .as(CourseDTO.class);

        assertEquals(courses.get(0).getName(), createdCourse1.getName());
        assertEquals(courses.get(0).getDescription(), createdCourse1.getDescription());
        assertEquals(courses.get(1).getName(), createdCourse2.getName());
        assertEquals(courses.get(1).getDescription(), createdCourse2.getDescription());
    }

    @Test
    void updateCourse_shouldReturn200() {
        // Create a course
        CreateCourseDTO course = new CreateCourseDTO("Mathematics", "Study of numbers and patterns.");
        CourseDTO createdCourse = courseService.createCourse(course);

        // Update the course
        CreateCourseDTO updatedCourse = new CreateCourseDTO("Physics", "Study of matter and energy and their interactions.");
        CourseDTO updatedCourseDTO = courseService.updateCourse(createdCourse.getId(), updatedCourse);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updatedCourse)
                .when().put("/api/v1/courses/" + createdCourse.getId())
                .then().statusCode(200)
                .extract().body()
                .as(CourseDTO.class);

        assertEquals(updatedCourseDTO.getName(), updatedCourse.getName());
        assertEquals(updatedCourseDTO.getDescription(), updatedCourse.getDescription());
    }

    @Test
    void deleteCourse_shouldReturn204() {
        // Create a course
        CreateCourseDTO course = new CreateCourseDTO("Mathematics", "Study of numbers and patterns.");
        CourseDTO createdCourse = courseService.createCourse(course);

        // Delete the course
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/v1/courses/" + createdCourse.getId())
                .then().statusCode(204);
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
