package com.bluesprint.orareacs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String group;
    private String role;
    private List<Rule> rules;
}
