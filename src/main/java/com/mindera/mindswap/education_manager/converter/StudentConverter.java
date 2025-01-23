package com.mindera.mindswap.education_manager.converter;

import com.mindera.mindswap.education_manager.dto.CreateStudentDTO;
import com.mindera.mindswap.education_manager.dto.StudentDTO;
import com.mindera.mindswap.education_manager.model.Student;

/**
 * Utility class for converting between Student entities and DTOs.
 * 
 * This class provides static methods to convert:
 * - Student entity to StudentDTO
 * - CreateStudentDTO to Student entity
 * 
 * The class cannot be instantiated as it only contains static utility methods.
 */
public class StudentConverter {

    /**
     * Private constructor to prevent instantiation of utility class.
     * 
     * @throws IllegalStateException if an attempt is made to instantiate the class
     */
    private StudentConverter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a Student entity to a StudentDTO.
     * 
     * @param student The Student entity to convert
     * @return StudentDTO containing the student data, or null if input is null
     */
    public static StudentDTO toDto(Student student) {
        if (student == null) {
            return null;
        }

        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        return dto;
    }

    /**
     * Converts a CreateStudentDTO to a Student entity.
     * 
     * @param dto The CreateStudentDTO containing the student data
     * @return Student entity with the data from the DTO
     * @throws IllegalArgumentException if the input DTO is null
     */
    public static Student createToEntity(CreateStudentDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CreateStudentDTO cannot be null");
        }

        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        return student;
    }
}
