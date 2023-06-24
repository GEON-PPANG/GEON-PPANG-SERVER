package com.org.gunbbang.controller.DTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FileDTO {
    @NotNull
    private MultipartFile file;
}
