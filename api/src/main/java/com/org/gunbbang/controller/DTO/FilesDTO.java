package com.org.gunbbang.controller.DTO;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FilesDTO {
  @NotNull private List<MultipartFile> files;
}
