package org.nutzview.model;

import java.util.HashMap;
import java.util.Map;

public class VelocityModel implements IModel {

	private Map<String, Object> map = new HashMap<String, Object>();

	private String titleName = "Index页面";
	
	@Override
	public void addValue(Object Value) {
		map.put("data", Value);
	}

	@Override
	public String getTitleName() {
		return titleName;
	}

	@Override
	public Object getValue(String key) {
		return map.get(key);
	}

	
	public void put(String key, Object value) {
		map.put(key, value);
	}

	
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

}
