package net.pixeltk.glagol.adapter;

/**
 * Created by root on 05.10.16.
 */

public class ItemData {

    private String book_name;
    private String author_name;
    private int imageUrl;

    public ItemData(String book_name, String author_name, int imageUrl){

        this.book_name = book_name;
        this.author_name = author_name;
        this.imageUrl = imageUrl;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getAuthor_name()
    {
        return author_name;
    }

    public int getImageUrl() {
        return imageUrl;
    }
}