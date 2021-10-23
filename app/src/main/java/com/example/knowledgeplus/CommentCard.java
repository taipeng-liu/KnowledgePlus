package com.example.knowledgeplus;

public class CommentCard {
    String id;
    String uid;
    String username;
    String aid;
    String text;
    String date;

    public CommentCard() {

    }

    public CommentCard(String id, String uid, String username, String aid, String text, String date) {
        this.id = id;
        this.uid = uid;
        this.username = username;
        this.text = text;
        this.date = date;
        this.aid = aid;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getAid() {
        return aid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static CommentCard newInstance(String id, String uid, String username, String aid, String text, String date) {
        return new CommentCard(id, uid, username, aid, text, date);
    }
}