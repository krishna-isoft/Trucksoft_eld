package com.trucksoft.isoft.isoft_elog.driverchecklist;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemListViewBean implements Serializable{

	private String item_name;
	
	private String item_name_other;
	private String img_url;
	private String goodstatus;
	public String getItem_name_other() {
		return item_name_other;
	}

	public void setItem_name_other(String item_name_other) {
		this.item_name_other = item_name_other;
	}

	private ArrayList<String> subitem;

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public ArrayList<String> getSubitem() {
		return subitem;
	}

	public void setSubitem(ArrayList<String> subitem) {
		this.subitem = subitem;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String item_name) {
		this.img_url = item_name;
	}

	public String getGoodstatus() {
		return goodstatus;
	}

	public void setGoodstatus(String item_name) {
		this.goodstatus = item_name;
	}

}
