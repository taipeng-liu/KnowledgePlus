package com.example.knowledgeplus.SendNotificationPack;

// Token is the unique ID for each device, which will help firebase to send notification to a particular device
public class Token {
    private String token;
    public Token(String token) {this.token = token; }
    public Token() {

    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
