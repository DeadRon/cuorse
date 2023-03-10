package com.ead.course.specification;

import model.CourseModel;
import model.LessonModel;
import model.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {}

    @And(@Spec(path = "title", spec = Like.class))
    public interface ModuleSpec extends Specification<ModuleModel> {}

    @And(@Spec(path = "title", spec = Like.class))
    public interface LessonSpec extends Specification<LessonModel> {}

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId){
        return (root, query, cb) -> {
            query.distinct(true);
            Root<ModuleModel> module = root;
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(module, coursesModules));
        };
    }

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId){
        return (root, query, cb) -> {
            query.distinct(true);
            Root<LessonModel> lesson = root;
            Root<ModuleModel> course = query.from(ModuleModel.class);
            Expression<Collection<LessonModel>> moduleLessons = course.get("lessons");
            return cb.and(cb.equal(course.get("courseId"), moduleId), cb.isMember(lesson, moduleLessons));
        };
    }

}
