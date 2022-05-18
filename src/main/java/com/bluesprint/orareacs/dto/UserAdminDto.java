package com.bluesprint.orareacs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAdminDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String group;
}
