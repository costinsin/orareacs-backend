package com.bluesprint.orareacs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String group;
    private String role;
}
