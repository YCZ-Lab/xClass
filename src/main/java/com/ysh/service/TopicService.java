package com.ysh.service;

import com.ysh.mapper.TopicMapper;
import com.ysh.model.Topic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    final TopicMapper topicMapper;

    public TopicService(TopicMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    public Topic getTopicById(int id) {
        return topicMapper.getTopicById(id);
    }

    public List<Topic> getAllTopics(String region) {
        return topicMapper.getAllTopics(region);
    }

    public List<Topic> getTopicsByPage(int start, int size, String region) {
        return topicMapper.getTopicsByPage(start, size, region);
    }

    public int getTopicPages(int size, String region) {
        return topicMapper.getTopicPages(size, region);
    }

    public void save(Topic topic) {
        topicMapper.save(topic);
    }
}
