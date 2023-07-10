package com.org.gunbbang.util.Security;

import com.org.gunbbang.login.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Security;
import java.util.Optional;

public class SecurityUtil {
    public static Long getLoginMemberId() {
        CustomUserDetails userInfo
                = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userInfo.getMemberId();
    }
}
