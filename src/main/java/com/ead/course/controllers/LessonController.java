package com.ead.course.controllers;

import com.ead.course.controllers.dto.LessonDTO;
import com.ead.course.model.LessonModel;
import com.ead.course.model.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    LessonService lessonService;

    @PostMapping("modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable("moduleId")UUID moduleId,
                                             @RequestBody @Valid LessonDTO lessonDTO){
        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if (!moduleModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found");
        }

        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDTO, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModelOptional.get());
        lessonModel.setVideoUrl(lessonDTO.getVideoUrl());

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }

    @DeleteMapping("modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable("moduleId")UUID moduleId,
                                               @PathVariable("lessonId")UUID lessonId){

        Optional<LessonModel> lessonModel  = lessonService.findLessonIntoCourse(moduleId, lessonId);
        if (!lessonModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found");
        }

        lessonService.delete(lessonModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    @PutMapping("modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable("moduleId")UUID moduleId,
                                               @PathVariable("lessonId")UUID lessonId,
                                               @RequestBody @Valid LessonDTO lessonDTO){

        Optional<LessonModel> lessonModelOptional  = lessonService.findLessonIntoCourse(moduleId, lessonId);
        if (!lessonModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found");
        }
        var lessonModel = lessonModelOptional.get();

        lessonModel.setTitle("Bem vindo - nova versão");
        lessonModel.setDescription("Bem vindo a nova versão do Curso");
        lessonModel.setVideoUrl("http://localhost:8082/13253246524564");
        lessonService.save(lessonModel);

        return ResponseEntity.status(HttpStatus.OK).body(lessonModel);
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<List<LessonModel>> findAllLessonsByModule(@PathVariable("moduleId")UUID moduleId){
        List<LessonModel> lessonModels = lessonService.findAllLessonsIntoModule(moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(lessonModels);
    }

    @GetMapping("modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> findAllLessonsByModule(@PathVariable("moduleId")UUID moduleId,
                                                                    @PathVariable("lessonId")UUID lessonId){
        Optional<LessonModel> lessonModelOptional  = lessonService.findLessonIntoCourse(moduleId, lessonId);
        if (!lessonModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional.get());
    }

}