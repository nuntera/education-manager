package com.mindera.mindswap.education_manager.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class StudentCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student studentId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course courseId;
}
