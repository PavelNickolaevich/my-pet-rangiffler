package com.rangiffler.model.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

public class CustomSliceImpl<T> extends SliceImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CustomSliceImpl(@JsonProperty("content") List<T> content, @JsonProperty("number") int number,
                          @JsonProperty("size") int size, @JsonProperty("totalElements") Long totalElements,
                          @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                          @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
                          @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, 1), true);
    }

    public CustomSliceImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, true);
    }

    public CustomSliceImpl(List<T> content) {
        super(content);
    }

    public CustomSliceImpl() {
        super(new ArrayList<>());
    }
}
