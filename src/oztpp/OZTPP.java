package oztpp;
import org.apache.commons.cli.CommandLine;

import Helper.File;
import Helper.Injector;
import Helper.Parse;
import Helper.Result;

public class OZTPP {
	public static void main(String[] args) {
		
        CommandLine cmd = Parse.commandLineArgs(args);
        String textPath = cmd.getOptionValue("input");
        String varsPath = cmd.getOptionValue("variables");
        String charset = cmd.getOptionValue("encoding","UTF-8");
        String output = cmd.getOptionValue("output",null);
        boolean forceRequired = cmd.hasOption( "forcerequired" )?true:false;
        String logPath = cmd.getOptionValue("log",null);
        
		String text="";
		String varsString="";
		
		text = File.readFile(textPath, charset);
		varsString = File.readFile(varsPath, charset);

		Injector injector = new Injector(forceRequired);
		Parse.variablesFile(varsString, injector);
		
		Result res = injector.inject(text);
		
		if(output!=null){
			File.toFile(output, res.getText());
			System.out.println(res.eventsToString());
		}else{
			System.out.println(res.getText());			
		}

		if(logPath!=null){
			File.toFile(logPath, res.eventsToString());
		}
	}
}
