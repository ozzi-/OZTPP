package Helper;
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
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonObject.Member;

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
		JsonArray jsonArray = Json.parse(varsString).asArray();
		for (JsonValue jsonValue : jsonArray) {
			JsonObject currentJsonObject = jsonValue.asObject();
			if(currentJsonObject.get("marker")!=null){
				injector.setMarkerCharacter(currentJsonObject.get("marker").asString());
			}else{
				boolean required = false;
				String name=null;
				String value=null;
				for (Member member : currentJsonObject) {
					  String jsonObjName = member.getName();
					  JsonValue jsonObjValue = member.getValue();
					  if(jsonObjName.equals("required")){
							required = currentJsonObject.get("required")!=null?currentJsonObject.get("required").asBoolean():false;
					  }else{
						  name = jsonObjName;
						  value = jsonObjValue.asString();
					  }
				}
				injector.addVariable(name,value, required);						  
			}
		}
	}
}
