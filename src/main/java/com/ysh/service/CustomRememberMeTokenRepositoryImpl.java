package com.ysh.service;

import com.ysh.mapper.CustomRememberMeTokenRepositoryImplMapper;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomRememberMeTokenRepositoryImpl implements PersistentTokenRepository {

    final CustomRememberMeTokenRepositoryImplMapper customRememberMeTokenRepositoryImplMapper;

    public CustomRememberMeTokenRepositoryImpl(CustomRememberMeTokenRepositoryImplMapper customRememberMeTokenRepositoryImplMapper) {
        this.customRememberMeTokenRepositoryImplMapper = customRememberMeTokenRepositoryImplMapper;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        System.out.println("createNewToken: " + token);
        customRememberMeTokenRepositoryImplMapper.createNewToken(token);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        System.out.println("series=" + series + ",tokenValue=" + tokenValue + ",lastUsed=" + lastUsed + "");
        customRememberMeTokenRepositoryImplMapper.updateToken(series, tokenValue, lastUsed);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        System.out.println("seriesId: " + seriesId);
        return customRememberMeTokenRepositoryImplMapper.getTokenForSeries(seriesId);
    }

    @Override
    public void removeUserTokens(String username) {
        System.out.println("username: " + username);
        customRememberMeTokenRepositoryImplMapper.removeUserTokens(username);
    }
}
