package com.org.gunbbang.controller.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class KeywordNameRequestDto {
    @NotNull
    String keywordName;
}
