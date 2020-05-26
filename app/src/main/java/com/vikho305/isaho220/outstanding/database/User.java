package com.vikho305.isaho220.outstanding.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    private String id;
    private String username, email;
    private Profile profile;
    @SerializedName(value = "date_created")
    private String dateCreated;
    private double latitude, longitude;
    @SerializedName(value = "is_online")
    private boolean isOnline;
    @SerializedName(value = "follower_count")
    private int followerCount;
    @SerializedName(value = "following_count")
    private int followingCount;
    @SerializedName(value = "pending_follower_count")
    private int pendingFollowerCount;
    @SerializedName(value = "pending_following_count")
    private int pendingFollowingCount;

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        profile = in.readParcelable(Profile.class.getClassLoader());
        dateCreated = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        isOnline = in.readByte() != 0;
        followerCount = in.readInt();
        followingCount = in.readInt();
        pendingFollowerCount = in.readInt();
        pendingFollowingCount = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Profile getProfile() {
        return profile;
    }
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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

    public boolean isOnline() {
        return isOnline;
    }
    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getFollowerCount() {
        return followerCount;
    }
    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }
    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getPendingFollowerCount() {
        return pendingFollowerCount;
    }
    public void setPendingFollowerCount(int pendingFollowerCount) {
        this.pendingFollowerCount = pendingFollowerCount;
    }

    public int getPendingFollowingCount() {
        return pendingFollowingCount;
    }
    public void setPendingFollowingCount(int pendingFollowingCount) {
        this.pendingFollowingCount = pendingFollowingCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeParcelable(profile, flags);
        dest.writeString(dateCreated);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isOnline ? 1 : 0));
        dest.writeInt(followerCount);
        dest.writeInt(followingCount);
        dest.writeInt(pendingFollowerCount);
        dest.writeInt(pendingFollowingCount);
    }
}
