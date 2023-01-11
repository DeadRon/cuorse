package com.ead.course.services.impl;

import com.ead.course.repository.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements CourseService {

    @Autowired
    ModuleRepository moduleRepository;

}
