package net.pixeltk.glagol.adapter;

public class ChoiceItemFromCatalog {


	private int icon;
	private String author;
	private String book_name;
	private String name_reader;
	private String price;


	public ChoiceItemFromCatalog(int icon, String author, String book_name, String name_reader, String price){
		this.author = author;
		this.icon = icon;
		this.book_name = book_name;
		this.name_reader = name_reader;
		this.price = price;
	}

	
	public String getAuthor(){
		return this.author;
	}
	
	public int getIcon(){
		return this.icon;
	}

	public String getBook_name(){return this.book_name;}
	
	public String getName_reader(){return this.name_reader;}

	public String getPrice(){return  this.price;}

}
