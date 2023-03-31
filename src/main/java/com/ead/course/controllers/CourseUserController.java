package com.ead.course.controllers;

import com.ead.course.clients.CourseClient;
import com.ead.course.dto.SubscriptionDTO;
import com.ead.course.dto.UserDTO;
import com.ead.course.model.CourseModel;
import com.ead.course.model.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@Log4j2
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    CourseService courseService;

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private CourseUserService courseUserService;

    @GetMapping("/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId", direction = ASC) Pageable pageable,
                                                             @PathVariable(value = "courseId") UUID courseId){
        return status(OK).body(courseClient.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                               @RequestBody @Valid SubscriptionDTO subscriptionDTO){
        Optional<CourseModel> courseModelOptional = courseService.findBy(courseId);
        if(courseModelOptional.isEmpty()){
            return status(NOT_FOUND).body("Course Not Found");
        }
        if(courseUserService.existByCourseAndUserId(courseModelOptional.get(), subscriptionDTO.getUserId())){
            return status(CONFLICT).body("Error: subscription already exists!");
        }
        //verificação user
        CourseUserModel courseUserModel = courseUserService.save(courseModelOptional.get().convertToCourseUserModel(subscriptionDTO.getUserId()));

        return status(CREATED).body("Subscription creadted successfully!");
    };

}