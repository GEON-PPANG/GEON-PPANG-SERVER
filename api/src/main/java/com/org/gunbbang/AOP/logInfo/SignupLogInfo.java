package com.org.gunbbang.AOP.logInfo;

import lombok.Getter;

@Getter
public class SignupLogInfo extends LogInfo {
    private Boolean isGlutenFree;
    private Boolean isVegan;
    private Boolean isNutFree;
    private Boolean isSugarFree;

    private Boolean isNutrientOpen;
    private Boolean isIngredientOpen;
    private Boolean isNotOpen;

    public SignupLogInfo (
            String url, String name, String method, String header, String parameters, String body, String ipAddress,
            Boolean isGlutenFree, Boolean isVegan, Boolean isNutFree, Boolean isSugarFree,
            Boolean isNutrientOpen, Boolean isIngredientOpen, Boolean isNotOpen
    ) {
        super(url, name, method, header, parameters, body, ipAddress);
        this.isGlutenFree = isGlutenFree;
        this.isVegan = isVegan;
        this.isNutFree = isNutFree;
        this.isSugarFree = isSugarFree;
        this.isNutrientOpen = isNutrientOpen;
        this.isIngredientOpen = isIngredientOpen;
        this.isNotOpen = isNotOpen;
    }
}
