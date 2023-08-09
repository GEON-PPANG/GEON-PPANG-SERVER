package com.org.gunbbang.controller.DTO;

import javax.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileDTO {
  @NotNull private MultipartFile file;
}
