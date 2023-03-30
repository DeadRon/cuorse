package com.ead.course.controllers;

import com.ead.course.clients.CourseClient;
import com.ead.course.dto.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@Log4j2
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private CourseClient courseClient;

    @GetMapping("/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId", direction = ASC) Pageable pageable,
                                                             @PathVariable(value = "courseId") UUID courseId){
        return status(OK).body(courseClient.getAllUsersByCourse(courseId, pageable));
    }

}