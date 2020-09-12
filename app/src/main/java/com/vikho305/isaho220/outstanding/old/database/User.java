package com.vikho305.isaho220.outstanding.old.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName(value = "is_self")
    private boolean isSelf;
    @SerializedName(value = "is_following")
    private boolean isFollowing;
    @SerializedName(value = "is_pending_following")
    private boolean isPendingFollowing;
    @SerializedName(value = "is_blocking")
    private boolean isBlocking;
    private String id, username, email;
    private String picture, description;
    @SerializedName(value = "primary_hue")
    private double primaryHue;
    @SerializedName(value = "primary_saturation")
    private double primarySaturation;
    @SerializedName(value = "primary_lightness")
    private double primaryLightness;
    @SerializedName(value = "secondary_hue")
    private double secondaryHue;
    @SerializedName(value = "secondary_saturation")
    private double secondarySaturation;
    @SerializedName(value = "secondary_lightness")
    private double secondaryLightness;
    private double latitude, longitude;
    @SerializedName(value = "date_created")
    private String dateCreated;
    @SerializedName(value = "follower_count")
    private int followerCount;
    @SerializedName(value = "following_count")
    private int followingCount;
    @SerializedName(value = "pending_follower_count")
    private int pendingFollowerCount;
    @SerializedName(value = "pending_following_count")
    private int pendingFollowingCount;

    protected User(Parcel in) {
        isSelf = in.readByte() != 0;
        isFollowing = in.readByte() != 0;
        isPendingFollowing = in.readByte() != 0;
        isBlocking = in.readByte() != 0;
        id = in.readString();
        username = in.readString();
        email = in.readString();
        picture = in.readString();
        description = in.readString();
        primaryHue = in.readDouble();
        primarySaturation = in.readDouble();
        primaryLightness = in.readDouble();
        secondaryHue = in.readDouble();
        secondarySaturation = in.readDouble();
        secondaryLightness = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        dateCreated = in.readString();
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

    public boolean isSelf() {
        return isSelf;
    }
    public void setSelf(boolean self) {
        isSelf = self;
    }

    public boolean isFollowing() {
        return isFollowing;
    }
    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public boolean isPendingFollowing() {
        return isPendingFollowing;
    }
    public void setPendingFollowing(boolean pendingFollowing) {
        isPendingFollowing = pendingFollowing;
    }

    public boolean isBlocking() {
        return isBlocking;
    }
    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }

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

    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrimaryHue() {
        return primaryHue;
    }
    public void setPrimaryHue(double primaryHue) {
        this.primaryHue = primaryHue;
    }

    public double getPrimarySaturation() {
        return primarySaturation;
    }
    public void setPrimarySaturation(double primarySaturation) {
        this.primarySaturation = primarySaturation;
    }

    public double getPrimaryLightness() {
        return primaryLightness;
    }
    public void setPrimaryLightness(double primaryLightness) {
        this.primaryLightness = primaryLightness;
    }

    public double getSecondaryHue() {
        return secondaryHue;
    }
    public void setSecondaryHue(double secondaryHue) {
        this.secondaryHue = secondaryHue;
    }

    public double getSecondarySaturation() {
        return secondarySaturation;
    }
    public void setSecondarySaturation(double secondarySaturation) {
        this.secondarySaturation = secondarySaturation;
    }

    public double getSecondaryLightness() {
        return secondaryLightness;
    }
    public void setSecondaryLightness(double secondaryLightness) {
        this.secondaryLightness = secondaryLightness;
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
        dest.writeByte((byte) (isSelf ? 1 : 0));
        dest.writeByte((byte) (isFollowing ? 1 : 0));
        dest.writeByte((byte) (isPendingFollowing ? 1 : 0));
        dest.writeByte((byte) (isBlocking ? 1 : 0));
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(picture);
        dest.writeString(description);
        dest.writeDouble(primaryHue);
        dest.writeDouble(primarySaturation);
        dest.writeDouble(primaryLightness);
        dest.writeDouble(secondaryHue);
        dest.writeDouble(secondarySaturation);
        dest.writeDouble(secondaryLightness);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(dateCreated);
        dest.writeInt(followerCount);
        dest.writeInt(followingCount);
        dest.writeInt(pendingFollowerCount);
        dest.writeInt(pendingFollowingCount);
    }
}
