package com.chensoul.auth.domain.account.model;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.chensoul.auth.domain.role.Role;
import com.chensoul.spring.boot.mybatis.entity.CommonEntity;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
public class AccountDetails extends CommonEntity implements UserDetails {

    private static final long serialVersionUID = 2893861964284628170L;
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String phone;
    private String email;
    private List<Role> roles;

    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(this.authorities)) {
            this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        }
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
