package com.example.mapapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OverpassResponse {

    @SerializedName("elements")
    private List<Element> elements;

    public List<Element> getElements() {
        return elements;
    }

    public static class Element {
        @SerializedName("lat")
        private double latitude;

        @SerializedName("lon")
        private double longitude;

        @SerializedName("tags")
        private Tags tags;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public Tags getTags() {
            return tags;
        }

        public static class Tags {
            @SerializedName("name")
            private String name;
            @SerializedName("shop")
            private String shopType;

            public String getShopType() {
                return shopType;
            }

            public String getName() {
                return name; // Getter metodu ekledik
            }

        }
    }
}
