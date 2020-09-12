package com.vikho305.isaho220.outstanding.data;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName(value = "is_liked")
    private boolean isLiked;
    @SerializedName(value = "is_disliked")
    private boolean isDisliked;

    private String id;
    private String title, text, media;
    @SerializedName(value = "media_type")
    private String mediaType;
    private double latitude, longitude;
    @SerializedName(value = "date_created")
    private String dateCreated;
    private User author;
    @SerializedName(value = "like_count")
    private int likeCount;
    @SerializedName(value = "dislike_count")
    private int dislikeCount;

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isDisliked() {
        return isDisliked;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getMedia() {
        return media;
    }

    public String getMediaType() {
        return mediaType;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public User getAuthor() {
        return author;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }
}
