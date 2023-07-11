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
    VALUES (3, '넛프리', false, false, true);
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
    VALUES ('example@naver.com', 'djfkskd!', 'NONE', '다이어트', '닉네임', 'USER', 1, 3, null, null);

-- category
INSERT INTO category (category_id, category_name)
    VALUES (1, '하드빵류');
INSERT INTO category (category_id, category_name)
    VALUES (2, '디저트류');
INSERT INTO category (category_id, category_name)
    VALUES (3, '브런치류');

-- bakery
INSERT INTO bakery (bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town)
    VALUES (1,2,'1152-5','펄스브레드샵','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
    '경기도','일요일, 월요일','풍산역','https://www.idus.com/w/artist/1f6a0a08-7292-403d-8185-316f8d704d58/profile',true,false,true,'화~토 11:00 ~ 19:00',
            '010-1111-1111','일산역','고양시','정발산동');
INSERT INTO bakery (bread_type_id, nutrient_type_id, address_rest, bakery_name,bakery_picture, city, closed_day,
                    first_near_station, homepage, is_haccp,is_nongmo, is_vegan, opening_hours, phone_number, second_near_station, state, town)
VALUES (1,2,'1152-5','펄스브레드샵2','https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220526_91%2F1653554529250qdOYp_JPEG%2F0E35EAC3-F936-41C7-BEE4-645B83AED8B1.jpeg',
        '경기도','일요일, 월요일','풍산역','https://www.idus.com/w/artist/1f6a0a08-7292-403d-8185-316f8d704d58/profile',true,false,true,'화~토 11:00 ~ 19:00',
        '010-1111-1111','일산역','고양시','정발산동');

-- bakery_category
INSERT INTO bakery_category (bakery_id,category_id) values (1,2);
INSERT INTO bakery_category (bakery_id,category_id) values (2,2);

-- menu
INSERT INTO menu (menu_name,menu_price,bakery_id) values ('소금빵',3000,1);
INSERT INTO menu (menu_name,menu_price,bakery_id) values ('소금빵1',3000,1);
INSERT INTO menu (menu_name,menu_price,bakery_id) values ('소금빵2',3000,1);