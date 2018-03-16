package Helper;
import java.util.ArrayList;

public class Injector {
	private ArrayList<Variable> variables = new ArrayList<Variable>();
	private String markerCharacter;
	

	public void setMarkerCharacter(String markerCharacter){
		this.markerCharacter=markerCharacter;
	}
	
	public Result inject(String string){
		String originalString = string;
		Result result = new Result();
		for (Variable variable : variables) {
			String variableMarked = markerCharacter+variable.getName()+markerCharacter;
			if(variable.isRequired()){
				if(!string.contains(variableMarked)){
					result.addEvent(new Event("Required Variable "+variable.getName()+" not found in the provided string"));
				}
			}
			string = string.replace(variableMarked, variable.getValue());
		}
		result.addEvent(new Event("Completed. Original size "+originalString.length()+", current size "+originalString.length()));
		result.setText(string);
		return result;
	}
	
	public void addVariable(String name, String value, boolean required){
		variables.add(new Variable(name, value, required));
	}
	
	public void addVariableObject(Variable variable){
		variables.add(variable);
	}
	
	public ArrayList<Variable> getVariables() {
		return variables;
	}
	public void setVariables(ArrayList<Variable> variables) {
		this.variables = variables;
	}
}
