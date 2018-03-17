package Helper;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Injector {
	private ArrayList<Variable> variables = new ArrayList<Variable>();
	private String markerCharacter;
	private boolean forceRequired;

	public Injector(boolean forceRequired) {
		this.setForceRequired(forceRequired);
	}

	public void setMarkerCharacter(String markerCharacter){
		this.markerCharacter=markerCharacter;
	}
	
	public String getMarkerCharacter(){
		return this.markerCharacter;
	}
	
	public Result inject(String string){
		String originalString = string;
		Result result = new Result();
		for (Variable variable : variables) {
			String variableMarked = markerCharacter+variable.getName()+markerCharacter;
			if(variable.isRequired()){
				if(!string.contains(variableMarked)){
					if(forceRequired){
						System.err.println("Required Variable "+variable.getName()+" not found in the provided string. Aborting");
						System.exit(-1);
					}else{
						result.addEvent(new Event("Required Variable "+variable.getName()+" not found in the provided string"));						
					}
				}
			}
			string = string.replace(variableMarked, variable.getValue());
		}
		result.setText(string);
		
		Pattern p = Pattern.compile(markerCharacter+"[0-9a-zA-Z_-]+"+markerCharacter);
		Matcher m = p.matcher(string);
		if(m.find()){
			if(forceRequired){
				System.err.println("Found undeclared variable "+m.group()+" in the provided file");				
				System.exit(-1);	
			}else{
				result.addEvent(new Event("Found undeclared variable "+m.group()+" in the input file"));						
			}
		}
		result.addEvent(new Event("Completed. Original size "+originalString.length()+", current size "+string.length()));
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

	public boolean isForceRequired() {
		return forceRequired;
	}

	public void setForceRequired(boolean forceRequired) {
		this.forceRequired = forceRequired;
	}
}
