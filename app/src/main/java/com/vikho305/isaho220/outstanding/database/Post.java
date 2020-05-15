package com.vikho305.isaho220.outstanding.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable {

    public static final String TEXT_TYPE = "text", IMAGE_TYPE = "image";
    public static final String VIDEO_TYPE = "video", AUDIO_TYPE = "audio";

    private String id;
    private String title;
    private String text;
    private String media;
    @SerializedName(value = "content_type")
    private String contentType;
    private double latitude, longitude;
    @SerializedName(value = "date_created")
    private String dateCreated;
    private User author;

    public Post(String contentType, double latitude, double longitude) {
        this.contentType = contentType;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Post(Parcel in) {
        id = in.readString();
        title = in.readString();
        text = in.readString();
        media = in.readString();
        contentType = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        dateCreated = in.readString();
        author = in.readParcelable(User.class.getClassLoader());
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(media);
        dest.writeString(contentType);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(dateCreated);
        dest.writeParcelable(author, flags);
    }
}
