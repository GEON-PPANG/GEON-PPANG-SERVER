-- bread_type

INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (1, '글루텐프리', true, false, false, false);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (2, '비건빵', false, true, false, false);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (3, '넛프리', false, false, true, false);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (4, '슈가프리', false, false, false, true);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (5, '글루텐프리/비건빵', true, true, false, false);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (6, '글루텐프리/넛프리', true, false, true, false);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (7, '글루텐프리/슈가프리', true, false, false, true);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (8, '비건빵/넛프리', false, true, true, false);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (9, '비건빵/슈가프리', false, true, false, true);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (10, '넛프리/슈가프리', false, false, true, true);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (11, '글루텐프리/비건빵/넛프리', true, true, true, false);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (12, '글루텐프리/비건빵/슈가프리', true, true, false, true);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (13, '글루텐프리/넛프리/슈가프리', true, false, true, true);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (14, '비건빵/넛프리/슈가프리', false, true, true, true);
INSERT INTO bread_type (bread_type_id, bread_type_name, is_gluten_free, is_vegan, is_nut_free, is_sugar_free)
    VALUES (15, '글루텐프리/비건빵/넛프리/저당무설탕', true, true, true, true);

-- nutrient_type

INSERT INTO nutrient_type (nutrient_type_id, nutrient_type_name, is_ingredient_open, is_nutrient_open, is_not_open)
    VALUES (1, '영양성분 공개', true, false, false);
INSERT INTO nutrient_type (nutrient_type_id, nutrient_type_name, is_ingredient_open, is_nutrient_open, is_not_open)
    VALUES (2, '원재료 공개', false, true, false);
INSERT INTO nutrient_type (nutrient_type_id, nutrient_type_name, is_ingredient_open, is_nutrient_open, is_not_open)
    VALUES (3, '비공개', false, false, true);
INSERT INTO nutrient_type (nutrient_type_id, nutrient_type_name, is_ingredient_open, is_nutrient_open, is_not_open)
    VALUES (4, '영양성분 공개/원재료 공개', true, true, false);
INSERT INTO nutrient_type (nutrient_type_id, nutrient_type_name, is_ingredient_open, is_nutrient_open, is_not_open)
    VALUES (5, '영양성분 공개/비공개', true, false, true);
INSERT INTO nutrient_type (nutrient_type_id, nutrient_type_name, is_ingredient_open, is_nutrient_open, is_not_open)
    VALUES (6, '원재료 공개/비공개', false, true, true);
INSERT INTO nutrient_type (nutrient_type_id, nutrient_type_name, is_ingredient_open, is_nutrient_open, is_not_open)
    VALUES (7, '영양성분 공개/원재료 공개/비공개', true, true, true);

-- member
INSERT INTO member (email, password, platform_type, main_purpose, nickname, role, bread_type_id, nutrient_type_id, created_at, updated_at)
    VALUES ('example@naver.com', 'djfkskd!', 'NONE', 'DIET', '닉네임1', 'USER', 1, 3, null, null);
INSERT INTO member (email, password, platform_type, main_purpose, nickname, role, bread_type_id, nutrient_type_id, created_at, updated_at)
    VALUES ('example@han.com', 'djfkskd!', 'NONE', 'DIET', '닉네임2', 'USER', 1, 3, null, null); -- 빵유형 + 주목적 일치
INSERT INTO member (email, password, platform_type, main_purpose, nickname, role, bread_type_id, nutrient_type_id, created_at, updated_at)
    VALUES ('example@hanfff.com', 'djfkskd!', 'NONE', 'DIET', '닉네임3', 'USER', 1, 3, null, null); -- 빵유형 + 주목적 일치
INSERT INTO member (email, password, platform_type, main_purpose, nickname, role, bread_type_id, nutrient_type_id, created_at, updated_at)
    VALUES ('example@gggg.com', 'djfkskd!', 'NONE', 'HEALTH', '닉네임4', 'USER', 1, 3, null, null); -- 빵유형만 일치
INSERT INTO member (email, password, platform_type, main_purpose, nickname, role, bread_type_id, nutrient_type_id, created_at, updated_at)
    VALUES ('example@korea.com', 'djfkskd!', 'NONE', 'DIET', '닉네임5', 'USER', 7, 1, null, null); -- 주목적만 일치

-- category
INSERT INTO category (category_id, category_name)
    VALUES (1, '하드빵류');
INSERT INTO category (category_id, category_name)
    VALUES (2, '디저트류');
INSERT INTO category (category_id, category_name)
    VALUES (3, '브런치류');

-- bakery
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (1,1,2,'1152-5','펄스브레드샵','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
    '경기도','일요일, 월요일','풍산역','https://www.idus.com/w/artist/1f6a0a08-7292-403d-8185-316f8d704d58/profile',true,false,true,'화~토 11:00 ~ 19:00',
            '010-1111-1111','일산역','고양시','정발산동', 1, 9, 0, 0, 0, 9);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (2,1,2,'1152-5','펄스브레드샵2','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역','https://www.idus.com/w/artist/1f6a0a08-7292-403d-8185-316f8d704d58/profile',true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 2, 9, 0, 5, 1, 3);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (3, 1,3,'11-5','건대초코빵','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역','https://www.idus.com/w/artist/1f6a0a08-7292-403d-8185-316f8d704d58/profile',true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 3, 5, 1, 1, 1, 2);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (4,1,1,'52-5','비건비건','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역','https://www.idus.com/w/artist/1f6a0a08-7292-403d-8185-316f8d704d58/profile',true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 4, 1, 0, 1, 0, 0);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (5,7,1,'52-5','저당빵만판다','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역','https://www.idus.com/w/artist/1f6a0a08-7292-403d-8185-316f8d704d58/profile',true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 5, 14, 5, 4, 3, 2);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (6,3,6,'5211-5','졸려','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역','https://www.idus.com/w/artist/1f6a0a08-7292-403d-8185-316f8d704d58/profile',true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 6, 7, 0, 7, 0, 0);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (7,1,6,'51-5','졸빵집','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역',null,true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 7, 8, 1, 7, 0, 0);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (8,1,6,'51-5','졸빵집','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역',null,true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 7, 8, 1, 7, 0, 0);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (9,3,6,'51-5','졸빵집','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역',null,true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 7, 8, 1, 7, 0, 0);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (10,5,6,'51-5','졸빵집','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역',null,true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 7, 8, 1, 7, 0, 0);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (11,5,6,'51-5','빵빵빵','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역',null,true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 7, 8, 1, 7, 0, 0);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (12,6,6,'51-5','흠냐링빵집','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역',null,true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 7, 8, 1, 7, 0, 0);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (13,7,6,'51-5','맛있는빵집','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역',null,true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 7, 8, 1, 7, 0, 0);
INSERT INTO bakery (bakery_id, bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town,
                    book_mark_count, review_count, keyword_delicious_count, keyword_kind_count, keyword_special_count, keyword_zero_waste_count)
    VALUES (14,3,6,'51-5','죽빵집','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역',null,true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동', 7, 8, 1, 7, 0, 0);



-- bakery_category
INSERT INTO bakery_category (bakery_id,category_id) values (1,2);
INSERT INTO bakery_category (bakery_id,category_id) values (2,2);
INSERT INTO bakery_category (bakery_id,category_id) values (1,3);
INSERT INTO bakery_category (bakery_id,category_id) values (1,1);


-- menu
INSERT INTO menu (menu_name,menu_price,bakery_id) values ('소금빵',3000,1);
INSERT INTO menu (menu_name,menu_price,bakery_id) values ('소금빵1',3000,1);
INSERT INTO menu (menu_name,menu_price,bakery_id) values ('소금빵2',3000,1);

-- recommend
INSERT INTO recommend_keyword (keyword_name) values ('맛있어요');
INSERT INTO recommend_keyword (keyword_name) values ('친절해요');
INSERT INTO recommend_keyword (keyword_name) values ('특별한 메뉴');
INSERT INTO recommend_keyword (keyword_name) values ('제로 웨이스트');

-- book_mark
INSERT INTO book_mark (bakery_id, member_id) values (1, 2);
INSERT INTO book_mark (bakery_id, member_id) values (2, 2);
INSERT INTO book_mark (bakery_id, member_id) values (3, 2);
INSERT INTO book_mark (bakery_id, member_id) values (3, 3);
INSERT INTO book_mark (bakery_id, member_id) values (4, 3);
INSERT INTO book_mark (bakery_id, member_id) values (5, 3);
INSERT INTO book_mark (bakery_id, member_id) values (6, 4);
INSERT INTO book_mark (bakery_id, member_id) values (7, 5);

-- -- review
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (1, true, 'review_id: 1 | 제일최근 리뷰+좋아요+멤버빵유형 및 주목적 일치', 1, 2, '2023-07-13T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (2, true, 'review_id: 2 | 두번째+좋아요+멤버빵유형 및 주목적 일치', 1, 2, '2023-07-12T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (3, true, 'review_id: 3 | 세번째+좋아요+멤버빵유형 및 주목적 일치', 1, 2, '2023-07-11T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (4, true, 'review_id: 4 | 네번째+좋아요+멤버빵유형 및 주목적 일치', 4, 2, '2023-07-10T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (5, true, 'review_id: 5 | 다섯+좋아요++멤버빵유형 및 주목적 일치', 5, 3, '2023-07-09T18:00:29.68338Z', null);
--
-- -- 여기까지 best 리뷰에서 조회되어야 하는 데이터
--
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (6, true, 'review_id: 6 | 좋아요+빵유형만 일치 이거 나오면 안됨', 1, 4, '2023-07-11T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (7, true, 'review_id: 7 | 좋아요+주목적만 일치 이거 나오면 안됨', 1, 5, '2023-07-13T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (8, true, 'review_id: 8 | 좋아요+빵유형만 일치 이거 나오면 안됨', 1, 4, '2023-07-11T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (9, true, 'review_id: 9 | 좋아요+주목적만 일치 이거 나오면 안됨', 1, 5, '2023-07-13T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (10, true, 'review_id: 10 | 좋아요+빵유형만 일치 이거 나오면 안됨', 1, 4, '2023-07-11T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (11, true, 'review_id: 11 | 좋아요+주목적만 일치 이거 나오면 안됨', 1, 5, '2023-07-13T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (12, true, 'review_id: 12 | 좋아요+빵유형만 일치 이거 나오면 안됨', 1, 4, '2023-07-11T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (13, true, 'review_id: 13 | 좋아요+주목적만 일치 이거 나오면 안됨', 1, 5, '2023-07-13T18:00:29.68338Z', null);
--
-- -- 여기까지 best 리뷰가 모자를 때 나오는 랜덤 데이터
--
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (14, false, 'review_id: 14 | 싫어요임 나오면 안됨', 1, 3, '2023-07-13T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (15, false, 'review_id: 15 | 싫어요임 이거 나오면 안됨', 1, 4, '2023-07-12T18:00:29.68338Z', null);
-- INSERT INTO review (review_id, is_like, review_text, bakery_id, member_id, created_at, updated_at)
--     VALUES (16, false, 'review_id: 16 | 싫어요임 이거 나오면 안됨', 1, 4, '2023-07-12T18:00:29.68338Z', null);
--
-- -- 여기는 best+랜덤 리뷰 10개 했을때 모자라도 나오면 안되는 데이터


