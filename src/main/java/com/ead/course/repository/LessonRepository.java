package com.ead.course.repository;

import com.ead.course.model.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {
}
