package Helper;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Injector {
	private ArrayList<Variable> variables = new ArrayList<Variable>();
	private String markerCharacter = "%";
	private boolean forceRequired;
	private int recursionLevel = 10;
	private ArrayList<String> requiredCheckedList = new ArrayList<String>();

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
		String preString;
		Result result = new Result();
		int currentRecursionLevel = 0;
		do {
			preString = string;
			string = injectInternal(string, result);
			if(currentRecursionLevel++ > recursionLevel) {
				System.out.println("recursion too deep -> "+recursionLevel+" - exiting");
				System.exit(-1);
			}
		} while (!preString.equals(string));
		checkUndeclaredVariables(string, result);
		result.addEvent(new Event("Completed. Original size "+originalString.length()+", current size "+string.length()));
		return result;
	}

	private String injectInternal(String string, Result result) {
		for (Variable variable : variables) {
			String variableMarked = markerCharacter+variable.getName()+markerCharacter;
			if(variable.isRequired()){
				if(!string.contains(variableMarked) && !requiredCheckedList.contains(variable.getName())){
					if(forceRequired){
						System.err.println("Required Variable "+variable.getName()+" not found in the provided string. Aborting");
						System.exit(-1);
					}else{
						result.addEvent(new Event("Required Variable "+variable.getName()+" not found in the provided string"));
						requiredCheckedList.add(variable.getName());
					}
				}else {
					requiredCheckedList.add(variable.getName());
				}
			}
			string = replace(string, variable, variableMarked);
		}
		result.setText(string);
		return string;
	}

	private String replace(String string, Variable variable, String variableMarked) {
		do {
			int start = string.indexOf(variableMarked);
			int end = start + variableMarked.length();
			if(start!=-1) {
				String value = injectIndentation(string, variable, start);
				string = string.substring(0,start)+value+string.substring(end);
			}	
		} while (string.indexOf(variableMarked)!=-1);
		return string;
	}

	private String injectIndentation(String string, Variable variable, int start) {
		String indentUnclean = "";
		String value = variable.getValue();
		int indentStart = string.substring(0,start).lastIndexOf("\n");
		if(indentStart!=-1) {
			indentUnclean = string.substring(indentStart,start);
			indentUnclean=indentUnclean.replaceAll("[^\\s]", "");
			String[] lines = value.split("\n");
			value = "";
			int c = 0;
			for (String line : lines) {
				if(c++==0) {
					value+=line;
				}else {	
					value+=indentUnclean+line;
				}	
			}
		}
		return value;
	}

	private void checkUndeclaredVariables(String string, Result result) {
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

	public void setRecursionLevel(int level) {
		this.recursionLevel = level;
	}
	
	public int getRecursionLevel() {
		return this.recursionLevel;
	}
}
