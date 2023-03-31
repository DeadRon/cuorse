package com.ead.course.services.impl;

import com.ead.course.model.CourseModel;
import com.ead.course.model.CourseUserModel;
import com.ead.course.repository.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CourseUserImpl implements CourseUserService {

    @Autowired
    private CourseUserRepository courseUserRepository;

    @Override
    public boolean existByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return courseUserRepository.save(courseUserModel);
    }

}