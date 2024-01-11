package com.org.gunbbang.service

import com.org.gunbbang.PlatformType
import com.org.gunbbang.entity.Member
import com.org.gunbbang.repository.BakeryBreadTypeRepository
import com.org.gunbbang.repository.BakeryCategoryRepository
import com.org.gunbbang.repository.BakeryRepository
import com.org.gunbbang.repository.BookMarkRepository
import com.org.gunbbang.repository.BreadTypeRepository
import com.org.gunbbang.repository.CategoryRepository
import com.org.gunbbang.repository.MemberBreadTypeRepository
import com.org.gunbbang.repository.MemberNutrientTypeRepository
import com.org.gunbbang.repository.MemberRepository
import com.org.gunbbang.repository.MenuRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class BakeryServiceTest extends Specification {
    def mockCategoryRepo = Mock(CategoryRepository.class)
    def mockBookMarkRepo = Mock(BookMarkRepository.class)
    def mockMemberRepo = Mock(MemberRepository.class)
    def mockBakeryRepo = Mock(BakeryRepository.class)
    def mockMenuRepo = Mock(MenuRepository.class)
    def mockBreadTypeRepo = Mock(BreadTypeRepository.class)
    def mockBakeryCategoryRepo = Mock(BakeryCategoryRepository.class)
    def mockMemberBreadTypeRepo = Mock(MemberBreadTypeRepository.class)
    def mockMemberNutrientTypeRepo = Mock(MemberNutrientTypeRepository.class)
    def mockBakeryBreadTypeRepo = Mock(BakeryBreadTypeRepository.class)
    BakeryService bakeryService

    void setup() {
        bakeryService = new BakeryService(
                mockCategoryRepo,
                mockBookMarkRepo,
                mockMemberRepo,
                mockBakeryRepo,
                mockMenuRepo,
                mockBreadTypeRepo,
                mockBakeryCategoryRepo,
                mockMemberBreadTypeRepo,
                mockMemberNutrientTypeRepo,
                mockBakeryBreadTypeRepo)
    }

    void cleanup() {
    }


    def "필터 선택을 하지 않은 유저의 경우 랜덤 빵집 10개가 조회되어야 한다"() {
        given:
        def foundMember = new Member().builder()
                .memberId(1L)
                .email("test@naver.com")
                .password("1234")
                .platformType(PlatformType.NONE)
                .build()

        mockMemberRepo.findById(1L) >> foundMember

        when:
        def result = bakeryService.getBestBakeries()

        then:
        1 * bakeryService.getBestBakeries()


    }

    def "필터 선택을 한 유저의 경우 베스트 빵집 조건에 맞는 빵집 10개가 조회되어야 한다"() {
    }

    def "필터 선택을 했지만 베스트 빵집 조건에 맞는 빵집이 10개보다 적은 경우"() {
    }

    def "필터 선택을 했지만 베스트 빵집 조건에 맞는 빵집과 동일한 빵유형을 판매하는 빵집이 10개보다 적은 경우"() {
    }

}
