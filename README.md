# OZTPP
```
usage: OZTPP.jar
 -e,--encoding <arg>    set encoding used to read files
 -i,--input <arg>       input file path
 -v,--variables <arg>   variables json file path
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
