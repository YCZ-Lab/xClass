package com.ysh.model;

import java.sql.Timestamp;
import java.util.List;

public class Topic {
    private int id;
    private String userName;
    private Timestamp createDateTime;
    private String region;
    private String title;
    private String content;
    private int replyNums;
    private List<Reply> replys;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Timestamp createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<Reply> getReplys() {
        return replys;
    }

    public void setReplys(List<Reply> replys) {
        this.replys = replys;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReplyNums() {
        return replyNums;
    }

    public void setReplyNums(int replyNums) {
        this.replyNums = replyNums;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", createDateTime=" + createDateTime +
                ", region='" + region + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", replyNums=" + replyNums +
                ", replys=" + replys +
                '}';
    }
}
