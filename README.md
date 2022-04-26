## CSV-Parser ##

Parse CSV and map to a given class.

```java
Parser parser = new Parser();
List<MyClass> results = parser.parse(file, MyClass.class);
```

Mark first row as header

```java
parser.firstRowIsHeader();
```

Set Delimiter

```java
parser.delimiter(',');
```

**Note:** Default is comma

Annotate private member variables with @CSVColumn. Set value equal to the name of the column as viewed in Excel

```java
@CSVColumn("A")
```

Defining class map

```java
public class MyClass {
	@CSVColumn("A")
	private String firstName;
	
	@CSVColumn("B")
	private String lastName;
	
	@CSVColumn("C")
	private int age;
	
	// Getters & Setters
}
```

## CSV Date ##

Converts value to Date 

```java
@CSVDate(pattern = "MM-dd-yyyy")
```

Data types supported
- Date

Format used by java.text.SimpleDateFormat

## Column Mapping ##

This library will automatically map CSV columns to an Object's annotated value.

Example:

| A | B | C |
|---|---|---|
|Alex|Ljunggren|40|

Will map to:

```java
public class MyClass {
	@CSVColumn("A")
	private String firstName;
	
	@CSVColumn("B")
	private String lastName;
	
	@CSVColumn("C")
	private int age;
	
	// Getters & Setters
}
```

In the event the code may need to normalize a CSV or swap values on the fly, a column mapping can be injected.

Example:

| A | B | C |
|---|---|---|
|40|Alex|Ljunggren|

In order to map to MyClass, the following will need to be injected:

```
Map<String, String> map = new HashMap<>();
// target object, CSV
map.put("A", "B");
map.put("B", "C");
map.put("C", "A");
```

And instatiated with:

```
parser.parse(file, MyClass.class).columnMap(map);
```