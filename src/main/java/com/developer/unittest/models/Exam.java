package com.developer.unittest.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Exam {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    private List<String> questions = new ArrayList<>();
}
