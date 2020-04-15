package com.vikho305.isaho220.outstanding;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String id, username;
    private String description;
    private String picture;
    private double hue, saturation, lightness;
    private double latitude, longitude;
    private String date_created; // TODO: test if "dateCreated" is possible

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        description = in.readString();
        picture = in.readString();
        hue = in.readDouble();
        saturation = in.readDouble();
        lightness = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        date_created = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(description);
        dest.writeString(picture);
        dest.writeDouble(hue);
        dest.writeDouble(saturation);
        dest.writeDouble(lightness);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(date_created);
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

    public String getDate_created() {
        return date_created;
    }
    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
