package com.vikho305.isaho220.outstanding.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {

    private String id, text;
    @SerializedName(value = "date_created")
    private String dateCreated;
    private Post post;
    private User author;

    protected Comment(Parcel in) {
        id = in.readString();
        text = in.readString();
        dateCreated = in.readString();
        post = in.readParcelable(Post.class.getClassLoader());
        author = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
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

    public String getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
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
        dest.writeString(text);
        dest.writeString(dateCreated);
        dest.writeParcelable(post, flags);
        dest.writeParcelable(author, flags);
    }
}
