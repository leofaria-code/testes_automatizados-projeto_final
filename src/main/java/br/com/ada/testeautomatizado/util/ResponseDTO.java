package br.com.ada.testeautomatizado.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
//@Getter @Setter
//@NoArgsConstructor @RequiredArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDTO<T> {
    String message;
    T detail;
}



