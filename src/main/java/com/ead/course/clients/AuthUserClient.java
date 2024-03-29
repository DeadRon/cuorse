package com.ead.course.clients;

import com.ead.course.dto.CourseUserDTO;
import com.ead.course.dto.ResponsePageDTO;
import com.ead.course.dto.UserDTO;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;

@Log4j2
@Component
public class AuthUserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilsService utilsService;

    @Value("${ead.api.url.authuser}")
    String REQUEST_URI_AUTHUSER;

    public Page<UserDTO> getAllUsersByCourse(UUID courseId, Pageable pageable){
        List<UserDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;
        String url = REQUEST_URI_AUTHUSER + utilsService.createdURLGetAllUsersByCourse(courseId, pageable);

        log.debug("Request URL: {}", url);
        log.debug("Request URL: {}", url);

        try {
            /*ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>
            informa ao RestTemplate o tipo de objeto que deve ser desserializado na resposta.
            . Representa dados de uma paginação*/
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType = new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};

            /*retorna um objeto ResponseEntity
            url: A URL do endpoint da API a ser chamada.
            HttpMethod.GET: O método HTTP para esta chamada, que é GET.
            null: O objeto HttpEntity que representa o corpo da requisição e cabeçalhos HTTP.
            Neste caso, é passado null, pois a chamada GET geralmente não possui corpo.
            responseType: Um objeto ParameterizedTypeReference que define o tipo esperado na resposta.
            Neste caso, espera-se um objeto ResponsePageDTO<CourseDTO>*/
            result = restTemplate.exchange(url, GET, null, responseType);
            searchResult = result.getBody().getContent();

            log.debug("Response Number of Elements: {}", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses {}", e);
        }
        log.info("Ending request /users courseId {}", courseId);
        return result.getBody();
    }

    public ResponseEntity<UserDTO> getOneUserById(UUID userId){
        String url = REQUEST_URI_AUTHUSER + "/users/" + userId;
        return restTemplate.exchange(url, GET, null, UserDTO.class);
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URI_AUTHUSER + "/users/" + userId + "/courses/subscription";
        var courseUserDTO = new CourseUserDTO();
        courseUserDTO.setUserId(userId);
        courseUserDTO.setCourseId(courseId);
        restTemplate.postForObject(url, courseUserDTO, String.class);
    }

    public void deleteCourseInAuthUser(UUID courseId) {
        String url = REQUEST_URI_AUTHUSER + "/users/courses" + courseId;
        restTemplate.exchange(url, DELETE, null, String.class);
    }
}