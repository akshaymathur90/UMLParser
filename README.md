# UMLParser

This project required us to develop a UML Parser which takes java source code as input and output file name, and then it generates a UML class diagram for it. The solution has been developed using JavaParser and PlantUML libraries. JavaParser is used to parse the java source code and obtain the relevant information while PlantUML generates the UML diagram. JavaParser can parse Java 1.8 source code as well. The parser generates an AST and provides visitor support. The AST records the sources code and comments. With JavaParser you can create new source and even edit the source code programmatically. It is light weight and easy to use. PlantUML is used to draw UML diagram, using a simple and human readable text description. It does not prevent you from drawing inconsistent diagram (like having two classes inheriting from each other, for example). PlantUML is a component that allows to quickly write all kinds of UML diagrams. Diagrams are defined using a simple and intuitive language. PlantUML can be integrated with almost any tool or you can simply use it from command line. The images can be generated in PNG,  SVG or LaTeX format.  To be able to generate some diagrams, you must have Graphviz software installed on your machine. PlantUML should be working with any version of Graphviz, starting with 2.26.3.

The solution has been developed in Eclipse Luna. The code logic uses JavaParser to parse each .java file and generate a compilation unit. The compilation unit is passed to the different overridden visitor methods. The visitor methods have been written for Classes, Methods, Field Variables and Constructors. Each visitor is uniquely coded to extract the relevant information from the java source code and create an intermediate output file which is understood by PlantUML and acts as the input for generating the Class Diagrams. The diagram is generated at the jar location with the specified output file name.

Using this Parser:

1.	Configure Graphviz
PlantUML only works when graphviz is correctly installed. Download and install graphviz version 2.26 or above for your Operating system from http://www.graphviz.org/Download..php. 

By default, the dot executable should be:

Windows :
	Firstly in: c:\Program Files\Graphviz*\bin\dot.exe
	Then in: c:\Program Files (x86)\Graphviz*\bin\dot.exe
On Linux/Mac OS-X :
	Firstly in: /usr/local/bin/dot
	Then in: /usr/bin/dot

I have used version 2.36 on OSX El Capitan. The instructions can also be found at http://plantuml.com/graphvizdot.html


Open this Maven Project in Eclipse and Run by providing a source code directory to parse.
