package net.pixeltk.glagol.adapter;

public class NavDrawerItem {
	

	private String icon;
	private String title;
	private int arrow;

	
	public NavDrawerItem(){}

	public NavDrawerItem(String icon, String title, int arrow){
		this.title = title;
		this.icon = icon;
		this.arrow = arrow;
	}

	
	public String getTitle(){
		return this.title;
	}
	
	public String getIcon(){
		return this.icon;
	}

	public int getArrow(){return this.arrow;}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(String icon){
		this.icon = icon;
	}

	public void setArrow(int arrow){this.arrow = arrow;}

}
