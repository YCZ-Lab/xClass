package com.ysh.service;

import com.ysh.mapper.RememberMeTokenRepositoryMapper;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RememberMeTokenRepositoryService implements PersistentTokenRepository {

    final RememberMeTokenRepositoryMapper rememberMeTokenRepositoryMapper;

    public RememberMeTokenRepositoryService(RememberMeTokenRepositoryMapper rememberMeTokenRepositoryMapper) {
        this.rememberMeTokenRepositoryMapper = rememberMeTokenRepositoryMapper;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        rememberMeTokenRepositoryMapper.createNewToken(token);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        rememberMeTokenRepositoryMapper.updateToken(series, tokenValue, lastUsed);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return rememberMeTokenRepositoryMapper.getTokenForSeries(seriesId);
    }

    @Override
    public void removeUserTokens(String username) {
        rememberMeTokenRepositoryMapper.removeUserTokens(username);
    }
}
