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
        Topic topic = topicMapper.getTopicById(id);
//        topic.setReplys(topicMapper.getReplysByTopicId(id));
        return topic;
    }

    public List<Topic> getAllTopics(String region) {
        List<Topic> topics = topicMapper.getAllTopics(region);
//        for (int i = 0; i < topics.size(); i++) {
//            Topic topic = topics.get(i);
//            topic.setReplys(topicMapper.getReplysByTopicId(topic.getId()));
//        }
        return topics;
    }

    public List<Topic> getTopicsByPage(int start, int size, String region) {
        List<Topic> topics = topicMapper.getTopicsByPage(start, size, region);
//        for (int i = 0; i < topics.size(); i++) {
//            Topic topic = topics.get(i);
//            topic.setReplys(topicMapper.getReplysByTopicId(topic.getId()));
//        }
        return topics;
    }

    public int getTopicPages(int size, String region) {
        return topicMapper.getTopicPages(size, region);
    }

    public void save(Topic topic) {
        topicMapper.save(topic);
    }
}
