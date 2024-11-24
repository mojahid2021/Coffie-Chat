package com.horizonhunters.coffiechat.Fragment.ChatFragment;

public class Chat {
    private String userId;
    private String lastMessage;
    private long timestamp;

    public Chat(String userId, String lastMessage, long timestamp) {
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
