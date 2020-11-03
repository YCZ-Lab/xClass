package com.ysh.model;

import java.sql.Timestamp;

public class Reply {
    private int id;
    private String userName;
    private int topicID;
    private Timestamp createDateTime;
    private String content;

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

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public Timestamp getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Timestamp createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "\nreply{" +
                "id=" + id +
                ", userName=" + userName +
                ", topicID=" + topicID +
                ", createDateTime=" + createDateTime +
                ", content='" + content + '\'' +
                '}';
    }
}
