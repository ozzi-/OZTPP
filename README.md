# OZTPP
OZ-Text-Pre-Processor is a small command line tool used for preprocessing of text files.

It was developed for the primary use of generating configuration files for multiple systems, which only differ for some certain strings (example: a changing IP per config file).
With OZTTP only the master template (--input) has to be adjusted instead of making the changes by hand in every single file. 

```
usage: OZTPP.jar
 -i,--input <arg>       Required: input file path
 -v,--variables <arg>   Required: variables json file path
 -e,--encoding <arg>    Set encoding used to read files (default: UTF-8)
 -forcerequired         Fail when required variable is not found (default: warning only)
 -l,--log <arg>         Write log to file path (default: printed to console when -o is set)
 -o,--output <arg>      Write processed input file to file path
```

## Example
```
java -jar OZTTP.jar -i input.txt -v variables.json -o out.txt
```

Turns this (input.txt):
```
if(getVariable().equals("%example%")){
	System.out.println("%example%");
}
while(test){
	%fileinclude%
}
```

With the help of this (variables.json):
```
{
  "marker": "%", // optional, defaults to %variablename%
  "recursionlevel": 5, // optional, defaults to 10 
  "variables": [
    {
      "name": "example",
      "value": "192.168.1.4"
    },
    {
      "name": "fileinclude",
      "file": "/home/ozzi/Desktop/Ruleset/include.txt"
    }
  ]
}

```
And this (include.txt):
```
System.out.println("im from the include file");
// indentation is kept. neat!
// also variables work here too -> %example%
```

Into this (output.txt):
```
if(getVariable().equals("192.168.1.4")){
	System.out.println("192.168.1.4");
}
while(test){
	System.out.println("im from the include file");
	// indentation is kept. neat!
	// also variables work here too -> 192.168.1.4
}
```

While the output on the CLI will contain information about any errors, missing required variables, variables that have been found in input.txt but not defined and the size prior and after the processing. 
