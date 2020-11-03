package com.ysh.service;

import com.ysh.mapper.LogsMapper;
import com.ysh.model.Logs;
import org.springframework.stereotype.Service;

@Service
public class LogsService {
    final LogsMapper logsMapper;

    public LogsService(LogsMapper logsMapper) {
        this.logsMapper = logsMapper;
    }

    public void save(Logs logs) {
        logsMapper.save(logs);
    }
}
