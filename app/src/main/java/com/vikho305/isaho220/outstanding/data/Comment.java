package com.vikho305.isaho220.outstanding.data;

import com.google.gson.annotations.SerializedName;

public class Comment {
    private String id;
    private String text;
    @SerializedName(value = "date_created")
    private String dateCreated;
    private Post post;
    private User author;

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public Post getPost() {
        return post;
    }

    public User getAuthor() {
        return author;
    }
}
