package gk;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mysql.jdbc.Connection;

public class Major_admission {
	private String subject_name;
	private String maxfs;
	private String varfs;
	private String minfs;
	private String pc_id;
	private String stype;
	private String annual_id;
	private String school_id;
	
	public String getPc_id() {
		return pc_id;
	}
	public void setPc_id(String pc_id) {
		this.pc_id = pc_id;
	}
	public String getStype() {
		return stype;
	}
	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getSchool_id() {
		return school_id;
	}
	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}
	public String getAnnual_id() {
		return annual_id;
	}
	public void setAnnual_id(String annual_id) {
		this.annual_id = annual_id;
	}
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	public String getMaxfs() {
		return maxfs;
	}
	public void setMaxfs(String maxfs) {
		this.maxfs = maxfs;
	}
	public String getVarfs() {
		return varfs;
	}
	public void setVarfs(String varfs) {
		this.varfs = varfs;
	}
	public String getMinfs() {
		return minfs;
	}
	public void setMinfs(String minfs) {
		this.minfs = minfs;
	}
	
		
		static String sqlstr = "jdbc:mysql://localhost:3306/iot?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		static String rootName = "";
		static String rootPwd = "";
		
		public static String matchfromMysql(String schoolName,String school_id){
			try{
				Class.forName("com.mysql.jdbc.Driver");
			}catch(ClassNotFoundException e){
				System.out.println("找不到驱动程序类 ，加载驱动失败！");
				e.printStackTrace();
			}
			
			try {
				Statement st1 = null;
				Connection con1 = null;
				con1 = (Connection) DriverManager.getConnection(sqlstr, rootName, rootPwd);
				String sql1 = "select * from school";
				st1 = con1.createStatement();
				ResultSet rs = st1.executeQuery(sql1);
				Map<String, String> map = new HashMap<String, String>();
				while(rs.next()){
				    String key = rs.getString(3);

				    String value = rs.getString(2);

				    map.put(key,value);
				}
				Iterator<String> it = map.keySet().iterator();  
		        while(it.hasNext()) {  
		            String key = (String)it.next();  
		            //System.out.println("key:" + key);  
		            //System.out.println("value:" + map.get(key));
		            if(key.equals(schoolName)){
		            	school_id = (String) map.get(key);
		            	System.out.println(school_id);
		            	System.out.println("测试");
		            }
		        } 
		        
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return school_id;
		}
		
		public static void writetoMysql(Major_admission major_admission){
			System.out.println(major_admission);
			try{
				Class.forName("com.mysql.jdbc.Driver");
			}catch(ClassNotFoundException e){
				System.out.println("找不到驱动程序类 ，加载驱动失败！");
				e.printStackTrace();
			}
			Statement st = null;
			Connection con = null;
			try{
				con = (Connection) DriverManager.getConnection(sqlstr, rootName, rootPwd);
				String subject_name = major_admission.getSubject_name();
				String maxfs = major_admission.getMaxfs();
				if(major_admission.getMaxfs().equals("--"))
					maxfs="-1";
				String varfs = major_admission.getVarfs();
				if(major_admission.getVarfs().equals("--"))
					varfs="-1";
				String minfs = major_admission.getMinfs();
				if(major_admission.getMinfs().equals("--"))
					minfs="-1";
				//String school_id = "1089";
				String schoolName = major_admission.getSchool_id();
				System.out.println(schoolName);
				String id = null;
				String Schoolid = matchfromMysql(schoolName, id);
				System.out.println(Schoolid);
				String annual_id = major_admission.getAnnual_id();
				String pc_id = major_admission.getPc_id();
				if(major_admission.getPc_id().equals("一批")){
					pc_id="1";
				}else if(major_admission.getPc_id().equals("二批")){
					pc_id="2";
				}
				String stype = major_admission.getStype();
				if(major_admission.getStype().equals("文科"))
					stype = "2";
				String sql = "insert ignore into major_admission(annual_id,subject_name,maxfs,varfs,minfs,pc_id,stype,school_id) values(\""+annual_id+"\",\""+subject_name+"\",\""+maxfs+"\",\""+varfs+"\",\""+minfs+"\",\""+pc_id+"\",\""+stype+"\",\""+Schoolid+"\")";
				System.out.println(sql);
				st = con.createStatement();
				st.executeUpdate(sql);
			}catch (SQLException e){
				e.printStackTrace();
			}finally{
				try {
					st.close();
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	    public static void main(String[] args) {
	    	File file = new File("major_wenke");
		    //showDirectory(file);
		    File[] files = file.listFiles();
	        for (int i1 = 0; i1 < files.length; i1++){
	        	ArrayList<Major_admission> list= new ArrayList<Major_admission>();
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		        DocumentBuilder builder;  
		        try {  
		        	   	String fileName = files[i1].getName();
		        	   	System.out.println(fileName);
		        	   	builder = factory.newDocumentBuilder();  
		        	   	Document doc;  
		        	   	doc = builder.parse(new File("major_wenke/"+fileName));  
		                Element root = doc.getDocumentElement();          
		                System.out.println("子节点："+root.getNodeName());  
		                NodeList personNodes = root.getElementsByTagName("areapiont");  
		                for(int i = 0; i<personNodes.getLength();i++){  
		                    Element personElement = (Element) personNodes.item(i);  
		                    Major_admission gkinfo = new Major_admission();
		                    //截取.XML之前的字符
		                    gkinfo.setSchool_id(StringUtils.substringBeforeLast(fileName,"1"));   
		                    NodeList childNodes = personElement.getChildNodes();  
		                    //System.out.println("*****"+childNodes.getLength());    
		                    for (int j = 0; j < childNodes.getLength(); j++) {  
		                    if(childNodes.item(j).getNodeType()==Node.ELEMENT_NODE){  
		                        if("specialname".equals(childNodes.item(j).getNodeName())){  
		                            gkinfo.setSubject_name(childNodes.item(j).getFirstChild().getNodeValue());
		                        }else if("maxfs".equals(childNodes.item(j).getNodeName())){  
		                            gkinfo.setMaxfs(childNodes.item(j).getFirstChild().getNodeValue());
		                        }else if("varfs".equals(childNodes.item(j).getNodeName())){  
		                            gkinfo.setVarfs(childNodes.item(j).getFirstChild().getNodeValue());  
		                        }else if("minfs".equals(childNodes.item(j).getNodeName())){  
		                            gkinfo.setMinfs(childNodes.item(j).getFirstChild().getNodeValue());  
		                        }else if("year".equals(childNodes.item(j).getNodeName())){  
		                            gkinfo.setAnnual_id(childNodes.item(j).getFirstChild().getNodeValue());  
		                        }else if("pc".equals(childNodes.item(j).getNodeName())){  
		                            gkinfo.setPc_id(childNodes.item(j).getFirstChild().getNodeValue());  
		                        }else if("stype".equals(childNodes.item(j).getNodeName())){  
		                            gkinfo.setStype(childNodes.item(j).getFirstChild().getNodeValue());  
		                        }         
		                    }  
		                }  
		                    list.add(gkinfo);  
		                }  
		        } catch (SAXException e) {  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            e.printStackTrace();          
		        } catch (ParserConfigurationException e) {  
		            e.printStackTrace();  
		        } catch (NullPointerException e) {
		        	e.printStackTrace();
		        }
		        for(int i=0;i<list.size();i++)  
		        {             
		            if (list.get(i)!=null)  
		            	writetoMysql(list.get(i));  
		        } 
	        }
	        
	        //list = getParserYear();  
	         
	    } 
}
