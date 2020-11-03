package com.ysh.service;

import com.ysh.mapper.ReplyMapper;
import com.ysh.model.Reply;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {
    final ReplyMapper replyMapper;

    public ReplyService(ReplyMapper replyMapper) {
        this.replyMapper = replyMapper;
    }

    public Reply getReplyById(int id) {
        return replyMapper.getReplyById(id);
    }

    public List<Reply> getReplyByTopicId(int id) {
        return replyMapper.getReplysByTopicId(id);
    }

    public List<Reply> getAllReplys() {
        return replyMapper.getAllReplys();
    }

    public List<Reply> getReplysByTopicIdPage(int topicId, int start, int size) {
        return replyMapper.getReplysByTopicIdPage(topicId, start, size);
    }

    public int getReplyPagesByTopicId(int topicId, int size) {
        return replyMapper.getReplyPagesByTopicId(topicId, size);
    }

    public void save(Reply reply) {
        replyMapper.save(reply);
    }

    public void updateReplyNums(int topicId) {
        replyMapper.updateReplyNums(topicId);
    }
}
