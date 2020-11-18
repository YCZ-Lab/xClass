package com.ysh.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;

@Mapper
public interface CustomRememberMeTokenRepositoryImplMapper {

    @Insert("insert into rememberme_token(username,series,tokenValue,last_login) values(#{username},#{series},#{tokenValue},#{date})")
    void createNewToken(PersistentRememberMeToken token);

    @Update("update rememberme_token set tokenValue=#{tokenValue},last_login=#{lastUsed} where series=#{series}")
    void updateToken(String series, String tokenValue, Date lastUsed);

    @Select("select username,series,tokenValue ,last_login from rememberme_token where series=#{seriesId}")
    PersistentRememberMeToken getTokenForSeries(String seriesId);

    @Update("delete from rememberme_token where username=#{username}")
    void removeUserTokens(String username);
}
















