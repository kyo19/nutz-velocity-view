package org.nutzview.model;


public class ModelProcesser {

	private static ModelProcesser mprocess = new ModelProcesser();
	
	private ModelProcesser(){}
	
	public static ModelProcesser getInstance(){
		return mprocess;
	}

	public IModel process(Object obj) {
		IModel model = null;
		if (obj == null) {
			return new VelocityModel();
		}
		if (obj instanceof String) {
			model = new VelocityModel();
			model.setTitleName(obj.toString());
		} else if (obj instanceof IModel) {
			model = (IModel) obj;
		}
		return model;
	}
}
