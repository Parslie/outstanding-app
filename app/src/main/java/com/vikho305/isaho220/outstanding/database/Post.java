package com.vikho305.isaho220.outstanding.database;

import com.google.gson.annotations.SerializedName;

public class Post {

    private String id;
    private String text;
    private String media;
    @SerializedName(value = "content_type")
    private String contentType;
    private double latitude, longitude;
    @SerializedName(value = "date_created")
    private String dateCreated;
    private User author;
}
