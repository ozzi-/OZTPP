package Helper;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

public class Parse {
	public static CommandLine commandLineArgs(String[] args) {
		Options options = new Options();
        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);
 
        Option inputJson = new Option("v", "variables", true, "variables json file path");
        inputJson.setRequired(true);
        options.addOption(inputJson);
        
        Option encoding = new Option("e", "encoding", true, "set encoding used to read files");
        encoding.setRequired(false);
        options.addOption(encoding);
        
        Option output = new Option("o", "output", true, "write processed text to file path");
        output.setRequired(false);
        options.addOption(output);
        
        Option forceRequired = new Option("forcerequired", "fail when required variable is not found (default: warning only)");
        forceRequired.setRequired(false);
        options.addOption(forceRequired);
        
        Option log = new Option("l", "log", true, "write log to file path");
        log.setRequired(false);
        options.addOption(log);
                
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("OZTPP.jar", options);
            System.exit(1);
        }
		return cmd;
	}
	
	public static void variablesFile(String varsString, Injector injector) {
		JsonArray jsonArray = null;		
		try{
			JsonValue jv = Json.parse(varsString);
			JsonObject jo = jv.asObject();
			if(jo.get("marker")!=null){
				injector.setMarkerCharacter(jo.get("marker").asString());
			}
			if(jo.get("recursionlevel")!=null) {
				injector.setRecursionLevel(jo.get("recursionlevel").asInt());
			}
			jsonArray = jo.get("variables").asArray();			
		}catch (Exception e){
			System.err.println("Error parsing variables json");
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		int i = 0;
		for (JsonValue jsonValue : jsonArray) {
			i++;
			JsonObject currentJsonObject = jsonValue.asObject();
			boolean required = false;
			String name=null;
			String value=null;
			for (Member member : currentJsonObject) {
				  String jsonObjName = member.getName();
				  JsonValue jsonObjValue = member.getValue();
				  if(jsonObjName.equals("required")){
						required = currentJsonObject.get("required")!=null?currentJsonObject.get("required").asBoolean():false;
				  }else if(jsonObjName.equals("name")) {
					  name = jsonObjValue.asString();
				  }else if(jsonObjName.equals("value")) {
					  value = jsonObjValue.asString();
				  }else if(jsonObjName.equals("file")) {
					  value = File.readFile(jsonObjValue.asString(), "UTF-8");
				  }
			}
			if(name==null || value==null) {
				System.err.println("Missing name or value for variable #"+i);
				System.exit(-1);
			}
			injector.addVariable(name,value, required);						  
		}
		checkForInvalidVariableNames(injector);
	}

	private static void checkForInvalidVariableNames(Injector injector) {
		for (Variable variable : injector.getVariables()) {
			Pattern p = Pattern.compile("^[0-9a-zA-Z_-]+$");
			Matcher m = p.matcher(variable.getName());
			if(!m.find()){
				System.err.println("Variable "+variable.getName()+" contains invalid characters. Only a-z A-Z 0-9 _ and - are allowed.");
				System.exit(-1);
			}
		}
	}
}
