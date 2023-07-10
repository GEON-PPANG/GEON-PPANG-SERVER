package com.org.gunbbang.controller.DTO;


import com.org.gunbbang.common.AuthType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberSignUpResponseDTO {
    private final AuthType type;
    private final String email;
}
