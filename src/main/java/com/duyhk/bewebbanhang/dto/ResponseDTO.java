package com.duyhk.bewebbanhang.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {
    private T data;
    private String message;
    private Integer status;
    private Integer totalPages;
    private Long totalElements;
}
