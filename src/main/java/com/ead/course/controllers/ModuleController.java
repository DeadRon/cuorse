package com.ead.course.controllers;

import com.ead.course.dto.ModuleDTO;
import com.ead.course.model.CourseModel;
import com.ead.course.model.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specification.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static com.ead.course.specification.SpecificationTemplate.moduleCourseId;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable("courseId")UUID courseId, @RequestBody @Valid ModuleDTO moduleDTO){
        log.debug("POST saveModule moduleDto received {} ", moduleDTO.toString());
        Optional<CourseModel> courseModelOptional = courseService.findBy(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDTO, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModelOptional.get());
        log.debug("POST saveModule moduleId saved {} ", moduleModel.getModuleId());
        log.info("Module saved successfully moduleId {} ", moduleModel.getModuleId());
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable("courseId")UUID courseId, @PathVariable("moduleId")UUID moduleId){

        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }

        moduleService.delete(moduleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully.");
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable("courseId")UUID courseId,
                                               @PathVariable("moduleId")UUID moduleId,
                                               @RequestBody @Valid ModuleDTO moduleDTO){
        log.debug("DELETE deleteModule moduleId received {} ", moduleId);
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }

        var moduleModel = moduleModelOptional.get();
        moduleModel.setDescription(moduleDTO.getDescription());
        moduleModel.setTitle(moduleDTO.getTitle());
        log.debug("DELETE deleteModule moduleId deleted {} ", moduleId);
        log.info("Module deleted successfully moduleId {} ", moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable("courseId")UUID courseId,
                                                           SpecificationTemplate.ModuleSpec moduleSpec,
                                                           @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = ASC) Pageable pageable){
        Page<ModuleModel> allModulesByCourse = moduleService.findAllByCourse(moduleCourseId(courseId).and(moduleSpec), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(allModulesByCourse);
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable("courseId")UUID courseId,
                                                          @PathVariable("moduleId")UUID moduleId){
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
    }

}