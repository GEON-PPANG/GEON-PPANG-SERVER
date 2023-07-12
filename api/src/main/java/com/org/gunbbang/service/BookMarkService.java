package com.org.gunbbang.service;

import com.org.gunbbang.controller.DTO.response.BookMarkResponseDTO;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookmarkRepository bookmarkRepository;

//    public BookMarkResponseDTO bookMark() {
//        return BookMarkResponseDTO.builder();
//    }

}
