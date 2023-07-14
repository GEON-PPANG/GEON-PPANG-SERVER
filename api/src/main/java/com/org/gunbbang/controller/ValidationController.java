package com.org.gunbbang.controller;

import com.org.gunbbang.controller.DTO.request.ValidateEmailRequestDTO;
import com.org.gunbbang.controller.DTO.request.ValidateNicknameRequestDTO;
import com.org.gunbbang.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class ValidationController {

    private final MemberService memberService;

    @PostMapping("/validate/nickname")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void checkDuplicatedNickname(@RequestBody @Valid ValidateNicknameRequestDTO request) {
        System.out.println("닉네임 ###" + request.getNickname().strip() + "###");
        memberService.checkDuplicatedNickname(request.getNickname().strip());
    }

    @PostMapping("/validate/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void checkDuplicatedEmail (@RequestBody @Valid ValidateEmailRequestDTO request) {
        System.out.println("이메일 ###" + request.getEmail().strip() + "###");
        memberService.checkDuplicatedEmail(request.getEmail().strip());
    }

}
