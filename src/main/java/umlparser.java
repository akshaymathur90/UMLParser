
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class umlparser {
	
	public static FileOutputStream out = null;
	public static HashSet<String> relationships = null;
	public static String className = "";
	public static boolean isInterface = false;
	public static Set<String> allInterfaceList = new HashSet<String>();
	public static Set<String> allClassList = new HashSet<String>();
	public static Set<String> interfaceList = new HashSet<String>();
	public static Map<String,String> variableList = new HashMap<String,String>();
	public static Map<String, GetSetObject> getterSetter = new HashMap<String, GetSetObject>();
	public static Map<String, String> classMethods = new HashMap<String, String>();
    public static void main(String[] args) throws Exception {
    	System.out.println("/****Working on generating the Class diagarm****/");
    	relationships = new HashSet<String>();
    	File folder = new File(args[0]);
    	FileInputStream in = null;
    	out = new FileOutputStream(args[1]);
    	out.write("@startuml\nskinparam classAttributeIconSize 0\n".getBytes());
    	createMetaData(args[0]);
    	for (File files : folder.listFiles())
    	{
    		if(files.getName().endsWith(".java"))
    		{
	    		//System.out.println("Path-->"+folder.toString());
	    		//System.out.println("File-->"+files.getName());
	    		/*if(folder.toString().endsWith("//"))
	    		{
	    			in = new FileInputStream(folder.getAbsolutePath()+files.getName());
	    		}
	    		else
	    		{*/
	    			in = new FileInputStream(folder.getAbsolutePath()+"//"+files.getName());
	    		//}
		        CompilationUnit cu;
		        try {
		            // parse the file
		            cu = JavaParser.parse(in);
		        } finally {
		            in.close();
		        }
		
		        // visit and print the methods names
		        variableList.clear();
		        getterSetter.clear();
		        classMethods.clear();
		        new ClassVistor().visit(cu, null);
		        new FieldVistor().visit(cu, null);
		        new ConstructorVistor().visit(cu, null);
		        new MethodVisitor().visit(cu, null);
		        Set<String> finalMethods = new HashSet<String>();
		        Set<String> finalPublicVariables = new HashSet<String>();
		        for(String keys : getterSetter.keySet())
		        {
		        	//System.out.println(keys+"--"+getterSetter.get(keys).getGetMethodName()+"--"+getterSetter.get(keys).getSetMethodName());
		        	if(getterSetter.get(keys).getGetMethodName()!=null && getterSetter.get(keys).getSetMethodName()!=null)
		        	{
		        		//System.out.println("public private var-->"+getterSetter.get(keys).getPrivateVariableName());
		        		finalMethods.add(getterSetter.get(keys).getGetMethodName());
		        		finalMethods.add(getterSetter.get(keys).getSetMethodName());
		        		finalPublicVariables.add(getterSetter.get(keys).getPrivateVariableName());
		        	}
		        }
		        //System.out.println("Final Methods-->"+finalMethods.toString());
		        for(String mkey : classMethods.keySet())
		        {
		        	//System.out.println("mkey-->"+mkey);
		        	if(!finalMethods.contains(mkey))
		        	{
		        		if(classMethods.get(mkey).startsWith("+"))
		        		{
		        			out.write(classMethods.get(mkey).getBytes());
		        		}
		        	}
		        }
		        //System.out.println("Private Variables-->"+variableList.toString());
		        for(String privateV : variableList.keySet())
		        {
		        	if(finalPublicVariables.contains(privateV))
		        	{
		        		out.write(("+ "+privateV+" : "+variableList.get(privateV)+"\n").getBytes());
		        	}
		        	else
		        	{
		        		out.write(("- "+privateV+" : "+variableList.get(privateV)+"\n").getBytes());
		        	}
		        }
		        //System.out.println(getterSetter.keySet());
		        //new ReferenceDataType().visit(cu, null);
		        out.write("\n}\n".getBytes());
		        
    		}
    	}
    	
    	for (String relation : relationships)
    	{
    		/*//System.out.println("Interface List--> "+interfaceList.toString());
    		//System.out.println("Relation-->"+relation);
    		//System.out.println("Hmm-->"+ relation.split(" ")[relation.split(" ").length-1].trim());
    		//System.out.println("contains-->"+interfaceList.contains(relation.split(" ")[relation.split(" ").length-1].trim())+"  "+relation.split(" ")[relation.split(" ").length-2].trim());
    		if(!(interfaceList.contains(relation.split(" ")[relation.split(" ").length-1].trim()) && (relation.split(" ")[relation.split(" ").length-2].trim().equalsIgnoreCase("..>") || relation.split(" ")[relation.split(" ").length-2].trim().equalsIgnoreCase("--"))))
    		{*/
    			out.write(relation.getBytes());
    			//System.out.println(relation);
    		//}
    	}
        out.write("@enduml\n".getBytes());
        File source = new File(args[1]);
        SourceFileReader reader = new SourceFileReader(source);
        List<GeneratedImage> list = reader.getGeneratedImages();
        // Generated files
        File png = list.get(0).getPngFile();
        System.out.println("The output file generated is -->"+png.getName());
    }
    public static void createMetaData(String filePath) throws Exception
    {
    	File folder = new File(filePath);
    	FileInputStream in = null;
    	for (File files : folder.listFiles())
    	{
    		if(files.getName().endsWith(".java"))
    		{
	    		//System.out.println("Path-->"+folder.toString());
	    		//System.out.println("File-->"+files.getName());
	    		if(folder.toString().endsWith("//"))
	    		{
	    			in = new FileInputStream(folder.getAbsolutePath()+files.getName());
	    		}
	    		else
	    		{
	    			in = new FileInputStream(folder.getAbsolutePath()+"//"+files.getName());
	    		}
		        CompilationUnit cu;
		        try {
		            // parse the file
		            cu = JavaParser.parse(in);
		        } finally {
		            in.close();
		        }
		        new ClassMetaDataVistor().visit(cu, null);
    		}
    	}
    	
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    private static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            String methodstring="";
            if(n.getDeclarationAsString(true, false).startsWith("public")|| isInterface)
            {
            	methodstring += "+ ";
            	//String declaration[] = n.getDeclarationAsString(true, false).split(" ",2);
            	//methodstring = methodstring+declaration[1];
            	//System.out.println("Parameters-->"+n.getParameters());
            	//System.out.println("Type-->"+n.getType());
            	String methodReturnType = n.getType().toString();
            	if(n.getBody()!=null && (n.getName().startsWith("get")||n.getName().startsWith("set")))
            	{
            		String varInMethod = n.getName().substring(3);
            		char c[] = varInMethod.toCharArray();
            		c[0] = Character.toLowerCase(c[0]);
            		varInMethod = new String(c);
            		//checking for get method
            		if(variableList.containsKey(varInMethod))
            		{
	            	int returnIndex = n.getBody().toStringWithoutComments().indexOf("return");
	            	//System.out.println("return index-->"+returnIndex);
	            	if(returnIndex!=-1)
	            	{
	            		//System.out.println(n.getBody().toStringWithoutComments().indexOf(';', returnIndex));
	            		String returnVariable = n.getBody().toStringWithoutComments().substring(returnIndex+6, n.getBody().toStringWithoutComments().indexOf(';', returnIndex)).trim();
	            		returnVariable = returnVariable.replace("this.", "");
	            		//System.out.println("return variable-->"+returnVariable);
	            		//System.out.println("VariableContains-->"+variableList.containsKey(n.getBody().toStringWithoutComments().substring(returnIndex+6, n.getBody().toStringWithoutComments().indexOf(';', returnIndex)).trim()));
	            		if(variableList.containsKey(n.getBody().toStringWithoutComments().substring(returnIndex+6, n.getBody().toStringWithoutComments().indexOf(';', returnIndex)).trim().replace("this.", "")))
	            		{
	            			if(getterSetter.containsKey(returnVariable))
	            			{
	            				GetSetObject updateobj = getterSetter.get(returnVariable);
	            				updateobj.setGetMethodName(n.getName());
	            				getterSetter.put(returnVariable, updateobj);
	            			}
	            			else
	            			{
	            				getterSetter.put(returnVariable,new GetSetObject(n.getName(), null, returnVariable));
	            			}
	            		}
	            	}
	            	//checking for set method
	            	
	            	if(n.getParameters().size()!=0)
	            	{
	            		String parameterType = n.getParameters().get(0).toString().split(" ")[0];
	            		String parameterVariable = n.getParameters().get(0).toString().split(" ")[1];
		            	//System.out.println("Parameter List-->"+parameterType);
		            	for(Statement statements : n.getBody().getStmts())
		            	{
		            		if(statements.toStringWithoutComments().contains(parameterVariable) && statements.toStringWithoutComments().contains("="))
		            		{
		            			if(statements instanceof ExpressionStmt)
		            			{
		            			if(((ExpressionStmt)statements).getExpression() instanceof AssignExpr)
		            			{
			            			//System.out.println("Voila-->"+((AssignExpr)((ExpressionStmt)statements).getExpression()).getTarget());
			            			String targetVariable = ((AssignExpr)((ExpressionStmt)statements).getExpression()).getTarget().toString();
			            			targetVariable = targetVariable.replace("this.", "");
			            			if(variableList.containsKey(targetVariable))
			            			{
			            				if(getterSetter.containsKey(targetVariable))
				            			{
				            				GetSetObject updateobj = getterSetter.get(targetVariable);
				            				updateobj.setSetMethodName(n.getName());
				            				getterSetter.put(targetVariable, updateobj);
				            			}
				            			else
				            			{
				            				getterSetter.put(targetVariable,new GetSetObject(null, n.getName(), targetVariable));
				            			}
			            			}
		            			}
		            		}
		            		}
		            	}
	            	}
	            	
            	}
            	}
            }
            else if(n.getDeclarationAsString(true, false).startsWith("private"))
            {
            	methodstring += "- ";
            	//String declaration[] = n.getDeclarationAsString(true, false).split(" ",2);
            	//methodstring = methodstring+declaration[1];
            	//methodstring = methodstring+n.getName();
            	//System.out.println("Modifier-->"+n.getModifiers());
            }
            else
            {
            	methodstring += "# ";
            	//String declaration[] = n.getDeclarationAsString(true, false).split(" ",2);
            	//methodstring = methodstring+declaration[1];
            }
            methodstring = methodstring + n.getName() + " ( ";
            //out.write((methodstring+"\n").getBytes());
            if(n.getChildrenNodes().size() != 0)
            {
                //System.out.println("Method Dec.-->"+n.getChildrenNodes());
                //Parameter param =(Parameter) n.getChildrenNodes().get(1);
                for(Parameter methodParam : n.getParameters())
                {
                	if(methodParam!=null)
                	{
	                	methodstring = methodstring+methodParam.toString().split(" ")[1]+" : ";
	                	methodstring = methodstring+methodParam.toString().split(" ")[0]+",";
                	}
                }
                methodstring = methodstring.substring(0, methodstring.length()-1);
                for (Node param :  n.getChildrenNodes())
                {
	                ////System.out.println("Param Type-->"+param.toString()+"  "+param.getClass());
	                if (param instanceof Parameter && !(((Parameter) param).getType().toString().trim().equalsIgnoreCase("String")||((Parameter) param).getType().toString().trim().equalsIgnoreCase("String[]")) && !isInterface)
	                {
	                	if(((Parameter) param).getType() instanceof ReferenceType)
	                	{
	                	//System.out.println("Uses-->"+((Parameter) param).getType());
	                	//System.out.println(className+ " ..> "+((Parameter) param).getType());
	                	relationships.add(className+ " ..> "+((Parameter) param).getType()+"\n");
	                	}
	                	
	                }
	                /*if(param.getChildrenNodes().size() != 0)
	                {
	                	//System.out.println("Multiple-->"+param.getChildrenNodes());
		                if(((BlockStmt)param).get instanceof ReferenceType)
		                {
		                	//System.out.println("Uses-->"+((Parameter)param).getType());
		                }
	                }*/
                }
                BlockStmt methodBody = n.getBody();
                if(methodBody!=null)
                {
	                for(Statement st : methodBody.getStmts())
	                {
	                	if(st instanceof ExpressionStmt)
	                	{
	                		Expression exp = ((ExpressionStmt) st).getExpression();
	                		//System.out.println(exp.toString());
	                		if(exp instanceof VariableDeclarationExpr)
	                		{
	                			//System.out.println(((VariableDeclarationExpr) exp).getType());
	                			Type ty = ((VariableDeclarationExpr) exp).getType();
	                			if(ty instanceof ReferenceType)
	                			{
	                				////System.out.println("Uses Body-->"+((ReferenceType) ty).getType().toString());
	                				//System.out.println(allInterfaceList.toString());
	                				if(allInterfaceList.contains(((ReferenceType) ty).getType().toString()))
	                				{
	                					//System.out.println("Uses Body-->"+((ReferenceType) ty).getType().toString());
	                					relationships.add(className+ " ..> "+((ReferenceType) ty).getType().toString()+"\n");
	                					
	                				}
	                			}
	                		}
	                		
	                	}
	      
	                }
	               
                }
                methodstring = methodstring + "): "+ n.getType()+"\n";
                classMethods.put(methodstring.split(" ")[1].trim(),methodstring);
                //out.write((methodstring+"\n").getBytes());
            }
        	

        }
    }
    private static class FieldVistor extends VoidVisitorAdapter {

        @Override
        public void visit(FieldDeclaration n, Object arg) {
            //System.out.println("Field-->"+n.toStringWithoutComments());
            String variable = "";
            String association = "";
            if(n.toStringWithoutComments().contains("private"))
            {
            	//System.out.println("-");
            	variable += "- ";
            	
            }
            else if (n.toStringWithoutComments().contains("public"))
            {
            	//System.out.println("+");
            	variable += "+ ";
            }
            else if(n.toStringWithoutComments().contains("protected"))
            {
            	variable += "# ";
            }
            else
            {
            	variable += "~ ";
            }
            Node x = n.getChildrenNodes().get(0);
            //System.out.println("need to check -->"+n.getChildrenNodes().get(1).toStringWithoutComments().split(" ")[0]);
           
            variable += n.getChildrenNodes().get(1).toStringWithoutComments().split(" ")[0] + " : ";
            if (x instanceof ReferenceType)
            {
            	////System.out.println("need to check -->"+x.toStringWithoutComments());
	        	if (x.getChildrenNodes().size() != 0)
	        	{
	        		if(x.getChildrenNodes().get(0) instanceof PrimitiveType)
	        		{
	        			//System.out.println("Primi array-->"+x.getChildrenNodes().get(0));
	        			variable += x.getChildrenNodes().get(0) ;
	        			variable += "[]";
	        		}
	        		else
	        		{
	        			
	        			Node y = x.getChildrenNodes().get(0);
	        			if(y.getChildrenNodes().size() != 0)
	        			{
		        			//System.out.println("ReferenceCollection-->"+y.getChildrenNodes().get(0));
		        			variable = variable+ "Collection<"+y.getChildrenNodes().get(0)+">";
		        			association = className + " -- " +"\"0..*\" "+ y.getChildrenNodes().get(0) ;
	        			}
	        			else
	        			{
	        				if(!y.toString().equalsIgnoreCase("String"))
	        				{
		        				//System.out.println("ReferenceClass-->"+y);
		        				variable = variable+ y;
		        				association = className + " -- " + y;
	        				}
	        				else
	        				{
	        					variable = variable+ y;
	        					association ="";
	        				}
	        			}
	        		}
	        	}
	        	else
	        	{
	        		//System.out.println("ReferenceDT2-->"+x.toString());
	        	}
            }
            else if(x instanceof PrimitiveType)
            {
            	//System.out.println("Primi i am here");
            	//String fieldData = "";
            	//variable = "";
            	String fieldDec[] = x.toStringWithoutComments().split(" ", 2);
            	//System.out.println("what?"+x.toStringWithoutComments());
            	variable = variable + x.toStringWithoutComments();
            	/*if (fieldDec[0].equals("public"))
            	{
            		variable = "+"+fieldDec[1];
            	}
            	else if(fieldDec[0].equals("private"))
            	{
            		variable = "-"+fieldDec[1];
            	}
            	else if(fieldDec[0].equals("protected"))
            	{
            		variable = "#"+fieldDec[1];
            	}*/
            	
            }
            //System.out.println("Final variable string-->"+variable);
            String variableSplit[] = variable.split(" ");
            if(variableSplit[0].equals("-"))
            {
                variableList.put(variableSplit[1],variableSplit[3]);
            }
            else
            {
	            try {
	            	if(!(variable.startsWith("#")||variable.startsWith("~")))
	            	{	
	            		out.write((variable+"\n").getBytes());
	            	}
	            	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            String a[]=association.split(" ");
            //!(allInterfaceList.contains(a[a.length-1])
            if (association != "" && !isInterface)
            {
            	//check for multiplicity here
            	
            	//System.out.println(a[2]+" -- "+a[0]);
            	//System.out.println(association);
            	//System.out.println(relationships.toString());
            	if(!(relationships.contains(a[a.length-1]+" -- "+a[0]+"\n")||relationships.contains(a[a.length-1]+" -- "+"\"0..*\" "+a[0]+"\n")))
            	{
            		relationships.add(association+"\n");
            	}
            	
            }
        }
    }
    private static class ClassVistor extends VoidVisitorAdapter {

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        	isInterface = false;
          try
          {
            //System.out.println("Class-->"+n.getName());
            className = n.getName();
            //System.out.println("Extends-->"+n.getExtends());
            //System.out.println("Is Interface-->"+n.isInterface());
            if (n.isInterface())
            {
            	out.write(("interface "+n.getName()+" {\n").getBytes());
            	isInterface = true;
            	interfaceList.add(n.getName().toString().trim());
            }
            else
            {
                out.write(("Class "+n.getName()+" {\n").getBytes());
            }
            if (n.getExtends().size() != 0)
            {
            	for (ClassOrInterfaceType inherits : n.getExtends())
            	{
            		//System.out.println(inherits.getName());
            		//System.out.println(n.getName() +" --|> "+inherits.getName());
            		if(relationships.contains(inherits.getName() +" --|> "+n.getName()) || relationships.contains(n.getName() +" --|> "+inherits.getName()))
            		{
            			//System.out.println("Already there");
            		}
            		else
            		{
            			relationships.add(n.getName() +" --|> "+inherits.getName()+"\n");
            		}
            		
            	}
            }
            //System.out.println("Interface-->"+n.getImplements());
            if (n.getImplements().size() != 0)
            {
            	for (ClassOrInterfaceType interfaces : n.getImplements())
            	{
            		//System.out.println(interfaces.getName());
            		//System.out.println(n.getName() +" ..|> "+interfaces.getName());
            		String dep = n.getName() +" ..|> "+interfaces.getName();
            		//relationships.add("A2 ..|> B2");
            		if(relationships.contains(interfaces.getName() +" ..|> "+n.getName()) || relationships.contains(n.getName() +" ..|> "+interfaces.getName()))
            		{
            			//System.out.println("Already there "+ dep);
            		}
            		else
            		{
            			relationships.add(n.getName() +" ..|> "+interfaces.getName()+"\n");
            		}
            	}
            }
        }
          catch(IOException e)
          {
        	  System.out.println(e.getMessage());
          }
        }
        
    }
    private static class ClassMetaDataVistor extends VoidVisitorAdapter {

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        	//isInterface = false;
        	//System.out.println("<-- Creating Meta Data -->");
            //System.out.println("Class-->"+n.getName());
            className = n.getName();
            //System.out.println("Extends-->"+n.getExtends());
            //System.out.println("Is Interface-->"+n.isInterface());
            if (n.isInterface())
            {
            	/*out.write(("interface "+n.getName()+" {\n").getBytes());
            	isInterface = true;
            	interfaceList.add(n.getName().toString().trim());*/
            	allInterfaceList.add(n.getName().toString().trim());
            }
            else
            {
                //out.write(("Class "+n.getName()+" {\n").getBytes());
            	allClassList.add(n.getName().toString().trim());
            }
        }
        
    }
    private static class ConstructorVistor extends VoidVisitorAdapter {

        @Override
        public void visit(ConstructorDeclaration n, Object arg) {
        	if(n.getParameters().size()!=0)
        	{
	        	for(Parameter p : n.getParameters())
	        	{
	        		//System.out.println(allInterfaceList.toString());
	        		if(allInterfaceList.contains(p.getType().toString()))
	        		{
	        			//System.out.println("Constructor uses-->"+p.getType().toString());
	        			relationships.add(className+ " ..> "+p.getType().toString()+"\n");
	        		}
	        	}
        	}
        	try {
				out.write(("+ "+n.getDeclarationAsString(false, false, true).toString()+"\n").getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}