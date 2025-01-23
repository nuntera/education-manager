package com.mindera.mindswap.education_manager.repository;

import com.mindera.mindswap.education_manager.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
}


