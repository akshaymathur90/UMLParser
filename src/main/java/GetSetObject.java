
public class GetSetObject {
	private String getMethodName;
	private String setMethodName;
	private String privateVariableName;
	public String getGetMethodName() {
		return getMethodName;
	}
	public void setGetMethodName(String getMethodName) {
		this.getMethodName = getMethodName;
	}
	public String getSetMethodName() {
		return setMethodName;
	}
	public void setSetMethodName(String setMethodName) {
		this.setMethodName = setMethodName;
	}
	public String getPrivateVariableName() {
		return privateVariableName;
	}
	public void setPrivateVariableName(String privateVariableName) {
		this.privateVariableName = privateVariableName;
	}
	public GetSetObject(String getMethodName, String setMethodName,
			String privateVariableName) {
		super();
		this.getMethodName = getMethodName;
		this.setMethodName = setMethodName;
		this.privateVariableName = privateVariableName;
	}

}
