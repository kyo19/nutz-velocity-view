package org.nutzview.model;

public interface IModel {

	public String getTitleName();

	public void setTitleName(String titleName);

	public void put(String key, Object t);

	public void addValue(Object t);

	public Object getValue(String key);
}
