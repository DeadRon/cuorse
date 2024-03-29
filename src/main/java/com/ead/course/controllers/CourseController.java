package com.ead.course.controllers;

import com.ead.course.dto.CourseDTO;
import com.ead.course.model.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specification.SpecificationTemplate;
import com.ead.course.validation.CourseValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.status;

@Log4j2
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    CourseValidator courseValidator;

    @PostMapping
    public ResponseEntity<Object> saveCuorse(@RequestBody CourseDTO courseDTO, Errors errors){
        log.debug("POST saveCourse courseDto received {} ", courseDTO.toString());
        courseValidator.validate(courseDTO, errors);
        if(errors.hasErrors()){
            return status(BAD_REQUEST).body(errors.getAllErrors());
        }
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel = courseService.save(courseModel);
        log.debug("POST saveCourse courseId saved {} ", courseModel.getCourseId());
        log.info("Course saved successfully courseId: {} ", courseModel.getCourseId());
        return status(HttpStatus.CREATED).body(courseModel);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCuorse(@PathVariable(value = "courseId") UUID courseId){
        log.debug("DELETE deleteCourse courseId received {} ", courseId);
        Optional<CourseModel> cuorseModelOptional = courseService.findBy(courseId);
        if(!cuorseModelOptional.isPresent()){
            return status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        courseService.delete(cuorseModelOptional.get());
        log.debug("DELETE deleteCourse courseId deleted {} ", courseId);
        log.info("Course deleted successfully courseId {} ", courseId);
        return status(HttpStatus.OK).body("Course deleted successfully");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCuorse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseDTO courseDto){
        log.debug("PUT updateCourse courseDto received {} ", courseDto.toString());
        Optional<CourseModel> cuorseModelOptional = courseService.findBy(courseId);
        if(!cuorseModelOptional.isPresent()){
            return status(HttpStatus.NOT_FOUND).body("Cuorse Not Found");
        }
        CourseModel courseModel = cuorseModelOptional.get();
        courseModel.setName(courseDto.getName());
        courseModel.setDescription(courseDto.getDescription());
        courseModel.setImageURL(courseDto.getImageUrl());
        courseModel.setCourseStatus(courseDto.getCourseStatus());
        courseModel.setCourseLevel(courseModel.getCourseLevel());
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseService.save(courseModel);
        log.debug("PUT updateCourse courseId saved {} ", courseModel.getCourseId());
        log.info("Course updated successfully courseId {} ", courseModel.getCourseId());
        return status(HttpStatus.OK).body(courseService.save(courseModel));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec courseSpec,
                                                           @PageableDefault(page = 0, size = 10, sort = "courseId", direction = ASC) Pageable pageable,
                                                           @RequestParam(required = false) UUID userId){
        if (userId != null){
            return status(HttpStatus.OK).body(courseService.findAll(SpecificationTemplate.courseUserId(userId).and(courseSpec), pageable));
        } else {
            return status(HttpStatus.OK).body(courseService.findAll(courseSpec, pageable));
        }

    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourseById(@PathVariable(value = "courseId") UUID courseId){

        Optional<CourseModel> courseModelOptional = courseService.findBy(courseId);
        if(courseModelOptional.isEmpty()){
            return status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        return status(HttpStatus.OK).body(courseModelOptional);
    }

}
