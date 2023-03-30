package com.ead.course.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

//Esta classe representa dados de uma paginação
// obtida da listagem de todos os cursos com
// base em algum filtro no micro-serviço Course
public class ResponsePageDTO<T> extends PageImpl<T> {

    //esse contrutor possui para parametros para a paginação e @JsonProperty é para informar ao Jackson que é
    //para utilizar esses atributos na desserialização
    //esse atributos são os mesmo que uma paginação costuma retornar
    //aqui é usado para ajudar em um recuros que exige paginação entre microsserviços
    /*@JsonProperty informa ao Jackson que esses atributos devem ser utilizados na desserialização
    * São os mesmos atributos que uma paginação costuma retornar*/
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResponsePageDTO(@JsonProperty("content") List<T> content,
                           @JsonProperty("number") int number,
                           @JsonProperty("size") int size,
                           @JsonProperty("totalElements") Long totalElements,
                           @JsonProperty("pageable") JsonNode pageable,
                           @JsonProperty("last") boolean last,
                           @JsonProperty("totalPages") int totalPages,
                           @JsonProperty("sort") JsonNode sort,
                           @JsonProperty("first") boolean first,
                           @JsonProperty("empty") boolean empty){
        super(content, PageRequest.of(number, size), totalElements);
    }

    public ResponsePageDTO(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public ResponsePageDTO(List<T> content) {
        super(content);
    }
}