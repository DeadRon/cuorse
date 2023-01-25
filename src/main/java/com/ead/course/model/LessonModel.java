package com.ead.course.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)// não converta para json atributos nulos
@Entity
@Table(name = "TB_LESSONS")
public class LessonModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID lessonId;
    @Column(nullable = false, length = 150)
    private String title;
    @Column(nullable = false)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//não permite a serialização há nível de api
    @ManyToOne(optional = false, fetch = FetchType.LAZY) //optional garante que as lições (muitos) estão vincúladas a um módulo (um)
    private ModuleModel module;

}
