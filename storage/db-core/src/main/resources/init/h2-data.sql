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
INSERT INTO member (email, password, platform_type, main_purpose, nickname, role, bread_type_id, nutrient_type_id)
    VALUES ('example@naver.com', 'djfkskd!', 'NONE', '다이어트', '닉네임', 'ROLE_USER', 1, 3);

-- category
INSERT INTO category (category_id, category_name)
    VALUES (1, '하드빵류');
INSERT INTO category (category_id, category_name)
    VALUES (2, '디저트류');
INSERT INTO category (category_id, category_name)
    VALUES (3, '브런치류');
