package net.pixeltk.glagol.adapter;

/**
 * Created by Yaroslav on 20.10.2016.
 */

public class BookMarksHelper {

    private String name_book;
    private String name_author;
    private String name_reader;
    private String price;
    private String img_url;
    private String current_position;
    private String seekbar_value;
    private String total_duration;
    private String now_listening;
    private String id_book;
    private String class_name;
    private String some_info;

    public String getName_book()
    {
        return name_book;
    }
    public String getName_author()
    {
        return name_author;
    }
    public String getPrice()
    {
        return price;
    }
    public String getImg_url()
    {
        return img_url;
    }

    public String getName_reader() {
        return name_reader;
    }

    public String getId_book() {
        return id_book;
    }

    public String getCurrent_position() {
        return current_position;
    }

    public String getSeekbar_value() {
        return seekbar_value;
    }

    public String getTotal_duration() {
        return total_duration;
    }

    public String getNow_listening() {
        return now_listening;
    }

    public void setName_author(String name_author) {
        this.name_author = name_author;
    }

    public void setName_book(String name_book) {
        this.name_book = name_book;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setId_book(String id_book) {
        this.id_book = id_book;
    }

    public void setName_reader(String name_reader) {
        this.name_reader = name_reader;
    }

    public void setCurrent_position(String current_position) {
        this.current_position = current_position;
    }

    public void setSeekbar_value(String seekbar_value) {
        this.seekbar_value = seekbar_value;
    }

    public void setTotal_duration(String total_duration) {
        this.total_duration = total_duration;
    }

    public void setNow_listening(String now_listening) {
        this.now_listening = now_listening;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSome_info() {
        return some_info;
    }

    public void setSome_info(String some_info) {
        this.some_info = some_info;
    }
}
