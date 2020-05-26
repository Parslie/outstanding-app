package com.vikho305.isaho220.outstanding.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable {

    public static final String TEXT_TYPE = "text", IMAGE_TYPE = "image";
    public static final String VIDEO_TYPE = "video", AUDIO_TYPE = "audio";

    private String id;
    private User author;
    private String title, text, media;
    @SerializedName(value = "media_type")
    private String mediaType;
    private double latitude, longitude;
    @SerializedName(value = "date_created")
    private String dateCreated;
    @SerializedName(value = "like_count")
    private int likeCount;
    @SerializedName(value = "dislike_count")
    private int dislikeCount;

    public Post(String mediaType, double latitude, double longitude) {
        this.mediaType = mediaType;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Post(Parcel in) {
        id = in.readString();
        author = in.readParcelable(User.class.getClassLoader());
        title = in.readString();
        text = in.readString();
        media = in.readString();
        mediaType = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        dateCreated = in.readString();
        likeCount = in.readInt();
        dislikeCount = in.readInt();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getMedia() {
        return media;
    }
    public void setMedia(String media) {
        this.media = media;
    }

    public String getMediaType() {
        return mediaType;
    }
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }
    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(author, flags);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(media);
        dest.writeString(mediaType);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(dateCreated);
        dest.writeInt(likeCount);
        dest.writeInt(dislikeCount);
    }
}
