package com.vikho305.isaho220.outstanding.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    private String id;
    private String username, email;
    private String description;
    private String picture;
    private double hue, saturation, lightness;
    private double latitude, longitude;
    @SerializedName(value = "date_created")
    private String dateCreated;

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        description = in.readString();
        picture = in.readString();
        hue = in.readDouble();
        saturation = in.readDouble();
        lightness = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        dateCreated = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(description);
        dest.writeString(picture);
        dest.writeDouble(hue);
        dest.writeDouble(saturation);
        dest.writeDouble(lightness);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(dateCreated);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public double getHue() {
        return hue;
    }
    public void setHue(double hue) {
        this.hue = hue;
    }

    public double getSaturation() {
        return saturation;
    }
    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    public double getLightness() {
        return lightness;
    }
    public void setLightness(double lightness) {
        this.lightness = lightness;
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
}
