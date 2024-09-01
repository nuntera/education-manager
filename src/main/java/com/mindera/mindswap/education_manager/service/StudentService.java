package com.mindera.mindswap.education_manager.service;

import com.mindera.mindswap.education_manager.converter.StudentConverter;
import com.mindera.mindswap.education_manager.dto.CreateStudentDTO;
import com.mindera.mindswap.education_manager.dto.StudentDTO;
import com.mindera.mindswap.education_manager.model.Student;
import com.mindera.mindswap.education_manager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDTO> findAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream().map(StudentConverter::toDto).toList();
    }

    public StudentDTO findStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return StudentConverter.toDto(student);
    }

    public StudentDTO createStudent(CreateStudentDTO createStudentDTO) {
        Student student = StudentConverter.createToEntity(createStudentDTO);
        student = studentRepository.save(student);
        return StudentConverter.toDto(student);
    }

    public StudentDTO updateStudent(Long id, CreateStudentDTO createStudentDTO) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        student.setFirstName(createStudentDTO.getFirstName());
        student.setLastName(createStudentDTO.getLastName());
        student.setEmail(createStudentDTO.getEmail());
        student = studentRepository.save(student);
        return StudentConverter.toDto(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public void deleteAllStudents() {
        studentRepository.deleteAll();
    }
}
