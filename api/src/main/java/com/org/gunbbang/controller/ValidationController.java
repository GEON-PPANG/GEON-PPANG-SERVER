package com.org.gunbbang.controller;

import com.org.gunbbang.controller.DTO.request.ValidateNicknameRequestDTO;
import com.org.gunbbang.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class ValidationController {

    private final MemberService memberService;

    @PostMapping("/validate/nickname")
    public void checkDuplicatedNickname(@RequestBody @Valid ValidateNicknameRequestDTO request) {
        memberService.checkDuplicatedNickname(request.getNickname());
    }

}
