package com.ysh.model;

import java.sql.Timestamp;

public class Logs {
    private int id;
    private String requestURI;
    private String queryString;
    private String method;
    private String status;
    private String message;
    private String referer;
    private Timestamp createDateTime;
    private String thread;
    private String userName;
    private String remoteAddr;
    private String remotePort;
    private String userAgent;
    private String acceptLanguage;
    private String sessionId;
    private String contentType;
    private long consumeTime;
    private String source;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public Timestamp getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Timestamp createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(long consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "\nLogs{" +
                "id=" + id +
                ", requestURI='" + requestURI + '\'' +
                ", queryString='" + queryString + '\'' +
                ", method='" + method + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", referer='" + referer + '\'' +
                ", createDateTime=" + createDateTime +
                ", thread='" + thread + '\'' +
                ", userName='" + userName + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", remotePort='" + remotePort + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", acceptLanguage='" + acceptLanguage + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", contentType='" + contentType + '\'' +
                ", consumeTime=" + consumeTime +
                ", source='" + source + '\'' +
                '}';
    }
}
