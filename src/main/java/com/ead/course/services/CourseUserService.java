package com.ead.course.services;

import com.ead.course.model.CourseModel;
import com.ead.course.model.CourseUserModel;

import java.util.UUID;

public interface CourseUserService {
    boolean existByCourseAndUserId(CourseModel courseModel, UUID userId);

    CourseUserModel save(CourseUserModel courseUserModel);
}
