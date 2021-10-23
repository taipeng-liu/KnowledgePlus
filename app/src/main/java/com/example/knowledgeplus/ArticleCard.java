package com.example.knowledgeplus;

import java.io.Serializable;

public class ArticleCard implements Serializable {
    String id;
    String title;
    int nViews;
    int nComments;
    String author;
    String uid;
    String location;
    String publishDate;
    String body;
    int nImages;

    public ArticleCard() {
    }

    public ArticleCard(String id, String title, int nViews, int nComments, String author, String uid, String location, String publishDate, String body, int nImages) {
        this.id = id;
        this.title = title;
        this.nViews = nViews;
        this.nComments = nComments;
        this.author = author;
        this.uid = uid;
        this.location = location;
        this.publishDate = publishDate;
        this.body = body;
        this.nImages = nImages;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getnViews() {
        return nViews;
    }

    public int getnComments() {
        return nComments;
    }

    public String getAuthor() {
        return author;
    }

    public String getUid() {
        return uid;
    }

    public String getLocation() {
        return location;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getBody() {
        return body;
    }

    public int getnImages() {
        return nImages;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setnViews(int nViews) {
        this.nViews = nViews;
    }

    public void setnComments(int nComments) {
        this.nComments = nComments;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setnImages(int nImages) {
        this.nImages = nImages;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public static ArticleCard newInstance(String id, String title, int nViews, int nComments, String author, String uid, String location, String publishDate, String body, int nImages) {
        return new ArticleCard(id, title, nViews, nComments, author, uid, location, publishDate, body, nImages);
    }
}
