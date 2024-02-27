package com.chensoul.auth.domain.account.mapper;

import com.chensoul.auth.domain.account.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {

    @Select("select * from account where " +
            "(username = #{account} or phone = #{account} or email = #{account})")
    Account getByAccount(@Param("account") String account);

}
