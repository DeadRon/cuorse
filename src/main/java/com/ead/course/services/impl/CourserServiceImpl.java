package com.ead.course.services.impl;

import com.ead.course.repository.CourseRepository;
import com.ead.course.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourserServiceImpl implements ModuleService {

    @Autowired
    CourseRepository courseRepository;

}
