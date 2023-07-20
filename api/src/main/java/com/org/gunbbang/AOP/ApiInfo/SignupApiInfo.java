package com.org.gunbbang.AOP.ApiInfo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

@Getter
public class SignupApiInfo extends RequestApiInfo {
    private String platformType = null;
    private String mainPurpose = null;

    private Boolean isGlutenFree = null;
    private Boolean isVegan = null;
    private Boolean isNutFree = null;
    private Boolean isSugarFree = null;

    private Boolean isNutrientOpen = null;
    private Boolean isIngredientOpen = null;
    private Boolean isNotOpen = null;

    public SignupApiInfo(
            JoinPoint joinPoint,
            Class clazz,
            ObjectMapper objectMapper
    ) {
        super(joinPoint, clazz, objectMapper);
        setTypeFields(joinPoint);
        setMainPurpose(joinPoint);
    }

    private void setTypeFields(JoinPoint joinPoint) {
        MemberSignUpRequestDTO requestDTO = getMemberSignupRequestDTO(joinPoint);

        try {
            if (requestDTO != null) {
                this.isGlutenFree = requestDTO.getBreadType().getIsGlutenFree();
                this.isVegan = requestDTO.getBreadType().getIsVegan();
                this.isNutFree = requestDTO.getBreadType().getIsNutFree();
                this.isSugarFree = requestDTO.getBreadType().getIsSugarFree();

                this.isIngredientOpen = requestDTO.getNutrientType().getIsIngredientOpen();
                this.isNutrientOpen = requestDTO.getNutrientType().getIsNutrientOpen();
                this.isNotOpen = requestDTO.getNutrientType().getIsNotOpen();
            }
        } catch (Exception e) {
            System.out.println(" extract 시 에러 발생");
            e.printStackTrace();
        }
    }

    private MemberSignUpRequestDTO getMemberSignupRequestDTO(JoinPoint joinPoint) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i].equals("request")) {
                    // TODO: 여기 NPE 날 수 있을지도??? requestBody가 null일 때 어떻게 될 지 검토 필요
                    Object requestBody = joinPoint.getArgs()[i];
                    return (MemberSignUpRequestDTO) requestBody;
                }
            }

            return null;
        } catch (Exception e) {
            System.out.println("MemberSignUpRequestDTO extract 시 에러 발생");
            e.printStackTrace();
            return null;
        }
    }

    private void setMainPurpose(JoinPoint joinPoint) {
        MemberSignUpRequestDTO requestDTO = getMemberSignupRequestDTO(joinPoint);

        try {
            if (requestDTO != null) {
                this.mainPurpose = requestDTO.getMainPurpose();
            }
        } catch (Exception e) {
            System.out.println("mainPurpose extract 시 에러 발생");
            e.printStackTrace();
        }
    }
}
