package com.ead.course.services.impl;

import com.ead.course.repository.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseUserImpl implements CourseUserService {

    @Autowired
    private CourseUserRepository courseUserRepository;

}
