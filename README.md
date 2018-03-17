# OZTPP
```
usage: OZTPP.jar
 -i,--input <arg>       Required: input file path
 -v,--variables <arg>   Required: variables json file path
 -e,--encoding <arg>    Set encoding used to read files (default: UTF-8)
 -forcerequired         Fail when required variable is not found (default: warning only)
 -l,--log <arg>         Write log to file path (default: printed to console when -o is set)
 -o,--output <arg>      Write processed input file to file path
```

Turns this:
```
if(getVariable().equals("%example%")){
	System.out.println("%wow%");
}
```

With the help of this:
```
[
	{
		"marker": "%"
	},
	{
		"example": "such text",
		"required": true
	},
	{
		"wow": "even \r\n more texts"
	}
]
```

Into this:
```
if(getVariable().equals("such text")){
	System.out.println("even 
 more texts");
}
```
