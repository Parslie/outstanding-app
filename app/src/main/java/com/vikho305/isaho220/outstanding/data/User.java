package com.vikho305.isaho220.outstanding.data;

import com.google.gson.annotations.SerializedName;

public class User {
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

    public boolean isSelf() {
        return isSelf;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public boolean isPendingFollowing() {
        return isPendingFollowing;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public double getPrimaryHue() {
        return primaryHue;
    }

    public double getPrimarySaturation() {
        return primarySaturation;
    }

    public double getPrimaryLightness() {
        return primaryLightness;
    }

    public double getSecondaryHue() {
        return secondaryHue;
    }

    public double getSecondarySaturation() {
        return secondarySaturation;
    }

    public double getSecondaryLightness() {
        return secondaryLightness;
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

    public int getFollowerCount() {
        return followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getPendingFollowerCount() {
        return pendingFollowerCount;
    }

    public int getPendingFollowingCount() {
        return pendingFollowingCount;
    }
}
