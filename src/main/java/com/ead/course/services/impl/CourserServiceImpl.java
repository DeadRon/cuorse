package com.ead.course.services.impl;

import com.ead.course.model.CourseModel;
import com.ead.course.model.CourseUserModel;
import com.ead.course.model.LessonModel;
import com.ead.course.model.ModuleModel;
import com.ead.course.repository.CourseRepository;
import com.ead.course.repository.CourseUserRepository;
import com.ead.course.repository.LessonRepository;
import com.ead.course.repository.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourserServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    CourseUserRepository courseUserRepository;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if(!moduleModelList.isEmpty()){
            for (ModuleModel module: moduleModelList) {
                List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if(!lessonModels.isEmpty()){
                    lessonRepository.deleteAll(lessonModels);
                }
            }
            moduleRepository.deleteAll(moduleModelList);
        }
        //exclui todos os usuários do curso antes de excluir o curso
        List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserInCourse(courseModel.getCourseId());
        if(!courseUserModelList.isEmpty()){
            courseUserRepository.deleteAll(courseUserModelList);
        }
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel cuorseModel) {
        return courseRepository.save(cuorseModel);
    }

    @Override
    public Optional<CourseModel> findBy(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> courseModelSpecification, Pageable pageable) {
        return courseRepository.findAll(courseModelSpecification, pageable);
    }

}
