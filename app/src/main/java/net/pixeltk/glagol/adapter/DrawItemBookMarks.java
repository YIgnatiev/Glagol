package net.pixeltk.glagol.adapter;

/**
 * Created by yaroslav on 21.06.16.
 */
public class DrawItemBookMarks {

    private String name_book;
    private String name_author;
    private String price;
    private String img_url;
    private String id;



    public DrawItemBookMarks(String name_book, String name_author, String price, String img_url, String id){

        this.name_book = name_book;
        this.name_author = name_author;
        this.price = price;
        this.img_url = img_url;
        this.id = id;

    }

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

    public String getId() {
        return id;
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


}
