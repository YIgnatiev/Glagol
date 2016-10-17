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

    public void setName_authors(String name_authors) {
        this.name_authors = name_authors;
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

    public void setReaders(String readers) {
        this.readers = readers;
    }
    @SerializedName("categorys") String categorys;
    public String getCategorys(){
        return categorys;
    }

    public void setCategorys(String categorys) {
        this.categorys = categorys;
    }
    @SerializedName("publisher") String publisher;
    public String getPublisher(){
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @SerializedName("duration") String duration;
    public String getDuration(){
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public void setDescription(String description) {
        this.description = description;
    }



}