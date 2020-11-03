package com.ysh.mapper;

import com.ysh.model.Reply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ReplyMapper {

    @Select("select id, userName, topicID, createDateTime, content from reply where id=${id}")
    Reply getReplyById(int id);

    @Select("select id, userName, topicID, createDateTime, content from reply where topicID=${topicId} order by id desc")
    List<Reply> getReplysByTopicId(int topicId);

    @Select("select id, userName, topicID, createDateTime, content from reply order by id desc")
    List<Reply> getAllReplys();

    @Select("select id, userName, topicID, createDateTime, content from reply where topicID=${topicId} order by id desc limit #{start}, #{size}")
    List<Reply> getReplysByTopicIdPage(int topicId, int start, int size);

    @Select("select ceil(count(*)/${size}) from reply where topicID=${topicId}")
    int getReplyPagesByTopicId(int topicId, int size);

    @Insert("insert into reply(userName, topicID, createDateTime, content) values(#{userName},#{topicID}, #{createDateTime},#{content})")
    void save(Reply reply);

    @Update("update topic set replyNums=replyNums+1 where id=#{topicId}")
    void updateReplyNums(int topicId);
}
