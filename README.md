# codeex_one
## Coding Exercise

Application that reads input CSV file and prints a) Salary corrections b) Long reporting lines

### Language - Java

JDK 1.8

Used, 
openjdk version "17.0.11" 2024-04-16 LTS
OpenJDK Runtime Environment Zulu17.50+19-CA (build 17.0.11+9-LTS)
OpenJDK 64-Bit Server VM Zulu17.50+19-CA (build 17.0.11+9-LTS, mixed mode, sharing)

#### To Run
Springboot Application that takes input absolute file path as the only argument.

Eg., 

java -jar salarycheck-0.0.1-SNAPSHOT.jar /tmp/input_file.csv

##### Logic

1) Fetches List of beans from input file rows
2) Creates EmpNode objects and are stored in HashMap
3) Sets Level value by iterating through the HashMap. Max iterations are Max Level times. Worst scenario is O(N*N)
4) Prints output in below example format, by iterating the HashMap

<div>
  <pre>
---------------------------------------------------------------------
Id: 5
First Name: Kalina
Last Name: Corcoran
Salary: 77771.79
Level: 7
Manager: (9) Hylden, Tierney
Subordinates: (36) Stede, Melosa | (37) O'Hagirtie, Durward | (54) Renzini, Humfrid | (60) Meggison, Anette | (62) Lighton, Junie | (67) Brigginshaw, Guilbert | (74) Dumper, Roi |
---------------------------------------------------------------------
Average Salary at level 8 is : 69686.41
Min Salary expected: 83623.69. Max Salary expected: 104529.61
Salary less by 5851.90
---------------------------------------------------------------------
Employee's level - 7, exceeds Max permissable level - 6.
---------------------------------------------------------------------
  </pre>
</div>

###### Frameworks

1) OpenCSV

###### JUnits

JUnits cover the main logic

###### Screenshots

Location: ./screenshots/

