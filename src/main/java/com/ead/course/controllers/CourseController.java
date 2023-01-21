package com.ead.course.controllers;

import com.ead.course.dto.CourseDTO;
import com.ead.course.model.CourseModel;
import com.ead.course.services.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCuorse(@RequestBody @Valid CourseDTO courseDTO){
        var cuorseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, cuorseModel);
        cuorseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        cuorseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(cuorseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCuorse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> cuorseModelOptional = courseService.findBy(courseId);
        if(!cuorseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        courseService.delete(cuorseModelOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCuorse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseDTO courseDTO){
        Optional<CourseModel> cuorseModelOptional = courseService.findBy(courseId);
        if(!cuorseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuorse Not Found");
        }
        CourseModel courseModel = cuorseModelOptional.get();
        courseModel.setName(courseDTO.getName());
        courseModel.setDescription(courseDTO.getDescription());
        courseModel.setImageURL(courseDTO.getImageUrl());
        courseModel.setCourseStatus(courseDTO.getCourseStatus());
        courseModel.setCourseLevel(courseModel.getCourseLevel());
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseService.save(courseModel);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }

    @GetMapping
    public ResponseEntity<List<CourseModel>> getAllCourses(){
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourseById(@PathVariable(value = "courseId") UUID courseId){

        Optional<CourseModel> courseModelOptional = courseService.findBy(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional);
    }

}
