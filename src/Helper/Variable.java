package Helper;

public class Variable {
	private String name;
	private String value;
	private boolean required;
	
	public Variable(String name, String value, boolean requried) {
		this.setName(name);
		this.setValue(value);
		this.setRequired(requried);
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String toString(){
		return ("{\""+name+"\" : \""+value.replace("\r", "/r").replace("\n", "/n").replace("\\N", "/N").replace("\\R", "/R")+"\"}"+(required?" - Required ":""));
	}
}
