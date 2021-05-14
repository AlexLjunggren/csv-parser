## CSV-Parser ##

Parse CSV and map to a given class.

```java
Parser parser = new Parser();
List<MyClass> results = parser.parse(file, MyClass.class);
```

Annotate private member variables with @CSVColumn. Set value equal to the name of the column as viewed in Excel

```java
@CSVColumn(value = "A")
```

Defining class map

```java
public class MyClass {
	@CSVColumn(value = "A")
	private String firstName;
	
	@CSVColumn(value = "B")
	private String lastName;
	
	@CSVColumn(value = "G")
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
