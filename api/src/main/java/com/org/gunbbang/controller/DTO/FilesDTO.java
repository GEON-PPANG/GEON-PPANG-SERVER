package com.org.gunbbang.controller.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class FilesDTO {
    @NotNull
    private List<MultipartFile> files;
}
