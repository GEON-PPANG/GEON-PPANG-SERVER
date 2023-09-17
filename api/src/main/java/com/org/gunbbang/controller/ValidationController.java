package com.org.gunbbang.controller;

import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.request.ValidateEmailRequestDTO;
import com.org.gunbbang.controller.DTO.request.ValidateNicknameRequestDTO;
import com.org.gunbbang.controller.DTO.response.ValidationResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.MemberService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("validation")
public class ValidationController {

  private final MemberService memberService;

  @PostMapping("/nickname")
  public ApiResponse<ValidationResponseDTO> checkDuplicatedNickname(
      @RequestBody @Valid final ValidateNicknameRequestDTO request) {
    return ApiResponse.success(
        SuccessType.VALIDATE_DUPLICATED_NICKNAME_SUCCESS,
        memberService.checkDuplicatedNickname(request.getNickname().strip()));
  }

  @PostMapping("/email")
  public ApiResponse<ValidationResponseDTO> checkDuplicatedEmail(
      @RequestBody @Valid final ValidateEmailRequestDTO request) {
    return ApiResponse.success(
        SuccessType.VALIDATE_DUPLICATED_EMAIL_SUCCESS,
        memberService.checkDuplicatedEmail(request.getEmail().strip()));
  }
}
