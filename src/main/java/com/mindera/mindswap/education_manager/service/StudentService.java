package com.mindera.mindswap.education_manager.service;

import com.mindera.mindswap.education_manager.converter.StudentConverter;
import com.mindera.mindswap.education_manager.converter.StudentCourseConverter;
import com.mindera.mindswap.education_manager.dto.CreateStudentDTO;
import com.mindera.mindswap.education_manager.dto.StudentCourseDTO;
import com.mindera.mindswap.education_manager.dto.StudentDTO;
import com.mindera.mindswap.education_manager.dto.UpdateStudentDTO;
import com.mindera.mindswap.education_manager.exception.EnrollmentException;
import com.mindera.mindswap.education_manager.exception.ResourceNotFoundException;
import com.mindera.mindswap.education_manager.model.Course;
import com.mindera.mindswap.education_manager.model.Student;
import com.mindera.mindswap.education_manager.model.StudentCourse;
import com.mindera.mindswap.education_manager.repository.CourseRepository;
import com.mindera.mindswap.education_manager.repository.StudentCourseRepository;
import com.mindera.mindswap.education_manager.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class handling business logic for Student operations.
 * 
 * This service provides methods for:
 * - Retrieving students (single or all)
 * - Creating new students
 * - Updating existing students
 * - Deleting students
 * - Managing course enrollments
 * 
 * All operations are transactional and include proper error handling.
 */
@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;

    /**
     * Constructs a new StudentService with required dependencies.
     * 
     * @param studentRepository Repository for Student entity operations
     * @param courseRepository Repository for Course entity operations
     * @param studentCourseRepository Repository for StudentCourse entity operations
     */
    @Autowired
    public StudentService(
        StudentRepository studentRepository, 
        CourseRepository courseRepository,
        StudentCourseRepository studentCourseRepository
    ) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentCourseRepository = studentCourseRepository;
    }

    /**
     * Retrieves all students from the database.
     * 
     * @return List of StudentDTO objects representing all students
     */
    public List<StudentDTO> findAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentConverter::toDto)
                .toList();
    }

    /**
     * Retrieves a specific student by their ID.
     * 
     * @param id The ID of the student to retrieve
     * @return StudentDTO representing the found student
     * @throws ResourceNotFoundException if no student is found with the given ID
     */
    public StudentDTO findStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return StudentConverter.toDto(student);
    }

    /**
     * Creates a new student.
     * 
     * @param createStudentDTO DTO containing the student information
     * @return StudentDTO representing the created student
     */
    @Transactional
    public StudentDTO createStudent(CreateStudentDTO createStudentDTO) {
        Student student = StudentConverter.createToEntity(createStudentDTO);
        Student savedStudent = studentRepository.save(student);
        return StudentConverter.toDto(savedStudent);
    }

    /**
     * Updates an existing student.
     * 
     * @param id The ID of the student to update
     * @param updateStudentDTO DTO containing the updated student information
     * @return StudentDTO representing the updated student
     * @throws ResourceNotFoundException if no student is found with the given ID
     */
    @Transactional
    public StudentDTO updateStudent(Long id, UpdateStudentDTO updateStudentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if (updateStudentDTO.getFirstName() != null) {
            student.setFirstName(updateStudentDTO.getFirstName());
        }
        if (updateStudentDTO.getLastName() != null) {
            student.setLastName(updateStudentDTO.getLastName());
        }
        if (updateStudentDTO.getEmail() != null) {
            student.setEmail(updateStudentDTO.getEmail());
        }

        Student updatedStudent = studentRepository.save(student);
        return StudentConverter.toDto(updatedStudent);
    }

    /**
     * Deletes a student by their ID.
     * Due to the cascade configuration in the Student entity,
     * this will also delete all associated StudentCourse records.
     * 
     * @param id The ID of the student to delete
     * @throws ResourceNotFoundException if no student is found with the given ID
     */
    @Transactional
    public void deleteStudent(Long id) {
        logger.info("Attempting to delete student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Student not found with ID: {}", id);
                    return new ResourceNotFoundException("Student not found with id: " + id);
                });

        logger.info("Student found: {}", student);

        studentRepository.delete(student);
        logger.info("Student with ID: {} has been deleted", id);
    }

    /**
     * Enrolls a student in a course.
     * 
     * @param studentId The ID of the student to enroll
     * @param courseId The ID of the course to enroll in
     * @return StudentCourseDTO representing the enrollment
     * @throws ResourceNotFoundException if either student or course is not found
     * @throws EnrollmentException if the student is already enrolled in the course
     */
    @Transactional
    public StudentCourseDTO enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Check if student is already enrolled in the course
        boolean isAlreadyEnrolled = student.getCourses().stream()
                .anyMatch(sc -> sc.getCourse().getId().equals(courseId));
        
        if (isAlreadyEnrolled) {
            throw new EnrollmentException("Student is already enrolled in this course");
        }

        StudentCourse enrollment = new StudentCourse();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        StudentCourse savedEnrollment = studentCourseRepository.save(enrollment);
        return StudentCourseConverter.toDto(savedEnrollment);
    }
}
