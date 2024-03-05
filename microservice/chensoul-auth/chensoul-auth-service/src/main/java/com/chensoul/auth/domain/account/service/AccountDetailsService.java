package com.chensoul.auth.domain.account.service;

import com.chensoul.auth.domain.account.Account;
import com.chensoul.auth.domain.account.mapper.AccountMapper;
import com.chensoul.auth.domain.account.model.AccountDetails;
import com.chensoul.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("请输入用户名");
        }

        final Account account = this.accountMapper.getByAccount(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误，请重新输入");
        }
        final AccountDetails accountDetails = new AccountDetails();
        BeanUtils.copyProperties(account, accountDetails);

        //查询权限
        return accountDetails;
    }

}
