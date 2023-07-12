package com.org.gunbbang.controller;

import com.org.gunbbang.controller.DTO.request.BookMarkRequestDTO;
import com.org.gunbbang.controller.DTO.response.BookMarkResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookMarkController {

    @PostMapping("/bookMark/{bakeryId}")
    public void bookMark(@RequestBody final BookMarkRequestDTO request, @PathVariable("bakeryId") Long bakeryId) {
        return;
    }
}
