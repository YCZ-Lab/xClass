package com.ysh.mapper;

import com.ysh.model.Reply;
import com.ysh.model.Topic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TopicMapper {
    @Select("select id, userName, createDateTime, region, title, content, replyNums from topic where id=#{id}")
    Topic getTopicById(int id);

    @Select("select id, userName, createDateTime, region, title, content, replyNums from topic where " +
            "region=#{region} order by" +
            " id desc")
    List<Topic> getAllTopics(String region);

    @Select("select id, userName, createDateTime, region, title, content, replyNums from topic where " +
            "region=#{region} order " +
            "by id desc " +
            "limit #{start}, #{size}")
    List<Topic> getTopicsByPage(int start, int size, String region);

    @Select("select ceil(count(*)/#{size}) from topic where region=#{region}")
    int getTopicPages(int size, String region);

    @Select("select id, userName, topicID, createDateTime, region, content, replyNums from reply where " +
            "topicID=#{topicId} and region=#{region} order by id desc")
    List<Reply> getReplysByTopicId(int topicId, String region);

    @Insert("insert into topic(userName, createDateTime, region, title, content, replyNums) values(#{userName}," +
            "#{createDateTime},#{region},#{title},#{content},0)")
    void save(Topic topic);
}
