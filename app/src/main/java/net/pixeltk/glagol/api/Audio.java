package net.pixeltk.glagol.api;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by o_cure on 1/23/16.
 */
public class Audio {

    @SerializedName("id") String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("name") String name_book;
    public String getName_book(){
        return name_book;
    }

    public void setName_book(String name) {
        this.name_book = name;
    }

    @SerializedName("authors") String name_authors;
    public String getName_authors(){
        return name_authors;
    }

    @SerializedName("icon") String icon;
    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @SerializedName("price") String price;
    public String getPrice(){
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    @SerializedName("readers") String readers;
    public String getReaders(){
        return readers;
    }

    @SerializedName("categorys") String categorys;
    public String getCategorys(){
        return categorys;
    }

    @SerializedName("publisher") String publisher;
    public String getPublisher(){
        return publisher;
    }

    @SerializedName("duration") String duration;
    public String getDuration(){
        return duration;
    }

    @SerializedName("size") String size;
    public String getSize(){
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @SerializedName("description") String description;
    public String getDescription(){
        return description;
    }

    @SerializedName("word") String word;
    public String getWord(){
        return word;
    }

    @SerializedName("filename") String name_audio;

    public String getName_audio() {
        return name_audio;
    }

    @SerializedName("pathfile") String path_audio;

    public String getPath_audio() {
        return path_audio;
    }

    @SerializedName("track_number") String track_number;

    public String getTrack_number() {
        return track_number;
    }

    @SerializedName("demo") String demo;

    public String getDemo() {
        return demo;
    }

    @SerializedName("model") String model;

    public String getModel() {
        return model;
    }

    @SerializedName("model_id") String model_id;

    public String getModel_id() {
        return model_id;
    }

    @SerializedName("created_at") String created_at;

    public String getCreated_at() {
        return created_at;
    }



}