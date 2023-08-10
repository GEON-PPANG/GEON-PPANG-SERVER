package com.org.gunbbang.controller;

import com.org.gunbbang.controller.DTO.request.ValidateEmailRequestDTO;
import com.org.gunbbang.controller.DTO.request.ValidateNicknameRequestDTO;
import com.org.gunbbang.service.MemberService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("validation")
public class ValidationController {

  private final MemberService memberService;

  @PostMapping("/nickname")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void checkDuplicatedNickname(@RequestBody @Valid ValidateNicknameRequestDTO request) {
    memberService.checkDuplicatedNickname(request.getNickname().strip());
  }

  @PostMapping("/email")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void checkDuplicatedEmail(@RequestBody @Valid ValidateEmailRequestDTO request) {
    memberService.checkDuplicatedEmail(request.getEmail().strip());
  }
}
