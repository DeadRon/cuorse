package com.ead.course.services;

import com.ead.course.model.CourseModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseModel cuorseModel);

    Optional<CourseModel> findBy(UUID cuorseId);

    List<CourseModel> findAll();
}
