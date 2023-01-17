package com.ead.course.services.impl;

import com.ead.course.model.LessonModel;
import com.ead.course.model.ModuleModel;
import com.ead.course.repository.LessonRepository;
import com.ead.course.repository.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(ModuleModel module) {
        List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoCourse(module.getModuleId());
        if(!lessonModels.isEmpty()){
            lessonRepository.deleteAll(lessonModels);
        }
        moduleRepository.delete(module);
    }
}
