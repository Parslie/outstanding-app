package com.vikho305.isaho220.outstanding.database;

import com.google.gson.annotations.SerializedName;

public class Comment {

    private String id;
    private String text;
    @SerializedName(value = "date_created")
    private String dateCreated;
    private Post post;
    private User author;
}
