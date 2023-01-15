package com.ead.course.repository;

import com.ead.course.model.ModuleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

    @EntityGraph(attributePaths = {"course"})
    ModuleModel findByTitle(String title);

}
