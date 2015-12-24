package abc;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.coyote.Request;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

@WebServlet("/FileUploadServlet")
public class UploadServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	//Set the directory to upload the files to 
	private final String UPLOAD_DIRECTORY = "/";
    public UploadServlet()
    {
    	super();
    }
    String type = null;  //Variable to store type of file 
    /**
     * Performs the operations required after pressing Submit button
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
    	String context = request.getServletContext().getRealPath("/");      //Gets the address to the working folder
    	System.out.println(context);
    	File file = new File(context+UPLOAD_DIRECTORY);						
		//Create a folder if it does not exist
    	if (!file.exists()) {											
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} 
			else {
				System.out.println("Failed to create directory!");
			}
		}
    	//File Upload code starts
        if(ServletFileUpload.isMultipartContent(request))
       	{
        	try 
        	{
        		List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(new ServletRequestContext(request));
        		for(FileItem item : multiparts){
        			if(!item.isFormField()){
        				String name = new File(item.getName()).getName();
        				type = item.getContentType();
        				System.out.println("Original File Name "+name+" of type "+type);
       					item.write( new File(context+UPLOAD_DIRECTORY + File.separator + "circuit.xml"));
       				}
       			}
        		//File Type Validation
       			if(type.matches("text/xml"))
       			{
       				//request.setAttribute("message", "XML Uploaded Successfully");
       				parse(response, context);
       			}
       			else{
       				PrintWriter out = response.getWriter();
        			response.setContentType("text/html");
        			out.println("<h3>The file selected is not supported.</h3>");
        			out.println("<h3> Choose an XML File to Upload</h3>");
       				out.println("<form action='FileUploadServlet' method='post' enctype='multipart/form-data'>");
       				out.println("<input type='file' name='file' />");    
       				out.println("<input type='submit' value='upload' />");    
       				out.println("</form>");
       			}
       			//File uploaded successfully             
        		} 
        	catch (Exception ex) {
        		request.setAttribute("message", "XML Upload Failed due to " + ex);
       		}            
        }
       	else{
       		request.setAttribute("message","Sorry this Servlet only handles file upload request");
       	}
        //Guarantees only forwards when the correct file type is uploaded
       	if(type.matches("text/xml"))
       	{
       		//request.getRequestDispatcher("/imageimport.jsp").forward(request, response);
       		System.out.println("Done...............");
       	}
   	}
    public void parse(HttpServletResponse response, String path)
    {
    	try {
			PrintWriter out = response.getWriter();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(path+"circuit.xml"));
			Element root = document.getDocumentElement();							
			out.println(root.getNodeName()+"\n");									
			NodeList nodeList = root.getChildNodes();
			out.println("============================");						
			for (int temp = 0; temp < nodeList.getLength(); temp++)					//loop through the node list
			{
			 Node node = nodeList.item(temp);											//get an element out of the node list
			 out.println("");    //Just a separator
			 if (node.getNodeType() == Node.ELEMENT_NODE)							//
			 {
				 Element eElement = (Element) node;									//type cast the node into the element type
				 if(eElement.getNodeName()=="inputs")
				 {
					 out.println(eElement.getNodeName());
					 out.println("============================");
					 NodeList nodeList1 = eElement.getChildNodes();
					 for (int temp1 = 0; temp1 < nodeList1.getLength(); temp1++)
						{
						 Node node1 = nodeList1.item(temp1);
						 if (node1.getNodeType() == Node.ELEMENT_NODE)
						 {
							 Element eElement1 = (Element) node1;
							 out.println("Input "+eElement1.getAttribute("id"));
						 }
						}
				 }
				 if(eElement.getNodeName()=="gates")
				 {
					 out.println(eElement.getNodeName());
					 out.println("============================");
					 NodeList nodeList1 = eElement.getChildNodes();
					 for (int temp1 = 0; temp1 < nodeList1.getLength(); temp1++)
						{
						 Node node1 = nodeList1.item(temp1);
						 
						 if (node1.getNodeType() == Node.ELEMENT_NODE)
						 {
							 Element eElement1 = (Element) node1;
							 out.println("Gate "+eElement1.getAttribute("id")+" of type "+eElement1.getAttribute("type"));
						 }
						}
				 }
				 if(eElement.getNodeName()=="outputs")
				 {
					 out.println(eElement.getNodeName());
					 out.println("============================");
					 NodeList nodeList1 = eElement.getChildNodes();
					 for (int temp1 = 0; temp1 < nodeList1.getLength(); temp1++)
						{
						 Node node1 = nodeList1.item(temp1);
						 if (node1.getNodeType() == Node.ELEMENT_NODE)
						 {
							 Element eElement1 = (Element) node1;
							 out.print("Output "+eElement1.getAttribute("id"));
							 NodeList nodeList2 = eElement1.getChildNodes();
							 for(int temp2 = 0; temp2 < nodeList2.getLength(); temp2++)
							 {
								 Node node2 = nodeList2.item(temp2);
								 if(node2.getNodeType()==Node.ELEMENT_NODE)
								 {
									 Element eElement2 = (Element) node2;
									 out.print(" with source as "+ eElement2.getElementsByTagName("type").item(0).getTextContent()+" id "+eElement2.getElementsByTagName("id").item(0).getTextContent()+"\n");
								 }
								 
							 }
							 
						 }
						}
				 }
				 if(eElement.getNodeName()=="connections")
				 {
					 out.println(eElement.getNodeName());
					 out.println("============================");
					 NodeList nodeList1 = eElement.getChildNodes();
					 for (int temp1 = 0; temp1 < nodeList1.getLength(); temp1++)
						{
						 Node node1 = nodeList1.item(temp1);
						 if(node1.getNodeType()==node.ELEMENT_NODE)
						 {
							 Element eElement1 = (Element) node1;
							 out.print("Gate id "+eElement1.getAttribute("id")+"\n");
							 NodeList nodeList2 = eElement1.getChildNodes();
							 //initiate calling process
							 for(int temp2 = 0; temp2<nodeList2.getLength();temp2++)
							 {
								 Node node2 = nodeList2.item(temp2);
								 if(node2.getNodeType()==node.ELEMENT_NODE)
								 {
									 Element eElement2 = (Element) node2;
									 out.println("source type "+eElement2.getElementsByTagName("type").item(0).getTextContent()+" with id "+eElement2.getElementsByTagName("id").item(0).getTextContent());
								 }
							 }
						 }
						}
				 }
			 }
			}
						 
					
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
    }
}