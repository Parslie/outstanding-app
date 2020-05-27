package com.vikho305.isaho220.outstanding.database;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Profile implements Parcelable {

    private String id;
    private String description;
    private String picture;
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

    protected Profile(Parcel in) {
        id = in.readString();
        description = in.readString();
        picture = in.readString();
        primaryHue = in.readDouble();
        primarySaturation = in.readDouble();
        primaryLightness = in.readDouble();
        secondaryHue = in.readDouble();
        secondarySaturation = in.readDouble();
        secondaryLightness = in.readDouble();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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

    public int getPrimaryColor() {
        return Color.HSVToColor(new float[] {
                360 * (float) primaryHue,
                (float) primarySaturation,
                (float) primaryLightness
        });
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

    public int getSecondaryColor() {
        return Color.HSVToColor(new float[] {
                360 * (float) secondaryHue,
                (float) secondarySaturation,
                (float) secondaryLightness
        });
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(picture);
        dest.writeDouble(primaryHue);
        dest.writeDouble(primarySaturation);
        dest.writeDouble(primaryLightness);
        dest.writeDouble(secondaryHue);
        dest.writeDouble(secondarySaturation);
        dest.writeDouble(secondaryLightness);
    }
}
