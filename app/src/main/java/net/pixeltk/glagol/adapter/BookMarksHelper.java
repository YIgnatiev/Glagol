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
    private String id_book;

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
}
