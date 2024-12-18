package com.mwsfot.framework.security.token;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author MinChang
 * @description 自定义扩展认证Token
 * @date 2024/12/11 11:20
 */
public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private Long tenantId;

    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials,
                                                     Long tenantId) {
        super(principal, credentials);
        this.tenantId = tenantId;
    }

    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials,
                                                     Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
