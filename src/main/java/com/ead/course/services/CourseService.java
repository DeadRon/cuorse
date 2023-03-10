package com.ead.course.services;

import model.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseModel cuorseModel);

    Optional<CourseModel> findBy(UUID cuorseId);

    Page<CourseModel> findAll(Specification<CourseModel> courseModelSpecification, Pageable pageable);
}
