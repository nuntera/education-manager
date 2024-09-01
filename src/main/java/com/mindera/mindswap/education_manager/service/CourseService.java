package com.mindera.mindswap.education_manager.service;

import com.mindera.mindswap.education_manager.converter.CourseConverter;
import com.mindera.mindswap.education_manager.dto.CourseDTO;
import com.mindera.mindswap.education_manager.dto.CreateCourseDTO;
import com.mindera.mindswap.education_manager.model.Course;
import com.mindera.mindswap.education_manager.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseService {

    CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDTO> findAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(CourseConverter::toDto).toList();
    }

    public CourseDTO findCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        return CourseConverter.toDto(course);
    }

    public CourseDTO createCourse(CreateCourseDTO createCourseDTO) {
        Course course = CourseConverter.createToEntity(createCourseDTO);
        course = courseRepository.save(course);
        return CourseConverter.toDto(course);
    }

    public CourseDTO updateCourse(Long id, CreateCourseDTO createCourseDTO) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        course.setName(createCourseDTO.getName());
        course.setDescription(createCourseDTO.getDescription());
        course = courseRepository.save(course);
        return CourseConverter.toDto(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public void deleteAllCourses() {
        courseRepository.deleteAll();
    }
}
