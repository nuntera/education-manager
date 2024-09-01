package com.mindera.mindswap.education_manager.controller;

import com.mindera.mindswap.education_manager.dto.CreateStudentDTO;
import com.mindera.mindswap.education_manager.dto.StudentDTO;
import com.mindera.mindswap.education_manager.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> studentDTOS = studentService.findAllStudents();
        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        try {
            StudentDTO studentDTO = studentService.findStudentById(id);
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody CreateStudentDTO createStudentDTO) {
        StudentDTO studentDTO = studentService.createStudent(createStudentDTO);
        return new ResponseEntity<>(studentService.findStudentById(studentDTO.getId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody CreateStudentDTO createStudentDTO) {
        StudentDTO studentDTO = studentService.updateStudent(id, createStudentDTO);
        return new ResponseEntity<>(studentService.findStudentById(studentDTO.getId()), HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<StudentDTO> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
