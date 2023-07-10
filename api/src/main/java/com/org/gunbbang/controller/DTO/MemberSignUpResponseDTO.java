package com.org.gunbbang.controller.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberSignUpResponseDTO {
    private final String type;
    private final String email;
}
