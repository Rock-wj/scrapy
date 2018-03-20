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

public class Admission {
	private String annual_id;
	private String max;
	private String average;
	private String min;
	private String pc;
	private String school_id;
	private String subject_id;
	
	public String getPc() {
		return pc;
	}
	public void setPc(String pc) {
		this.pc = pc;
	}

	public String getAnnual_id() {
		return annual_id;
	}
	public void setAnnual_id(String annual_id) {
		this.annual_id = annual_id;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getAverage() {
		return average;
	}
	public void setAverage(String average) {
		this.average = average;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getSchool_id() {
		return school_id;
	}
	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}
	public String getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}
	
//			private static ArrayList<Admission> getParserYear() {  
//		        ArrayList<Admission> list= new ArrayList<Admission>();  
//		          
//		             
//		    }
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
			            }
			        } 
			        
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return school_id;
			}
			
			public static void writetoMysql(Admission admission){
				System.out.println(admission);
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
					String annual_id = admission.getAnnual_id();
					String max = admission.getMax();
					if(admission.getMax().equals("--"))
						max="-1";
					String average = admission.getAverage();
					if(admission.getAverage().equals("--"))
						average="-1";
					String min = admission.getMin();
					if(admission.getMin().equals("--"))
						min="-1";
					String pc = admission.getPc();
					if(admission.getPc().equals("一批"))
						pc="1";
					else if(admission.getPc().equals("二批"))
						pc="2";
					String subject_id = "2";//1:理科	2：文科
					String schoolName = admission.getSchool_id();
					System.out.println(schoolName);
					String id = null;
					String Schoolid = matchfromMysql(schoolName, id);
					//System.out.println(Schoolid);
					String sql = "insert ignore into admission(annual_id,max,average,min,pc_id,school_id,subject_id) values(\""+annual_id+"\",\""+max+"\",\""+average+"\",\""+min+"\",\""+pc+"\",\""+Schoolid+"\",\""+subject_id+"\")";
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
		    	File file = new File("pc1_wenke");
			    //showDirectory(file);
			    File[] files = file.listFiles();
		        for (int i1 = 0; i1 < files.length; i1++){
		        	ArrayList<Admission> list= new ArrayList<Admission>();
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
			        DocumentBuilder builder;
			        try {  
			        		String fileName = files[i1].getName();
			        		//System.out.println(StringUtils.substringBeforeLast(fileName,"."));
			        		//截取.XML之前的字符 
			        		//Admission gkinfo1 = new Admission(); 
			        		//Admission school_name = new Admission(); 
			        		builder = factory.newDocumentBuilder();  
			                Document doc;  
			                doc = builder.parse(new File("pc1_wenke/"+fileName));  
			                Element root = doc.getDocumentElement();          
			                System.out.println("子节点："+root.getNodeName());  
			                  
			                NodeList personNodes = root.getElementsByTagName("score");  
			                for(int i = 0; i<personNodes.getLength();i++){  
			                    Element personElement = (Element) personNodes.item(i);  
			                    Admission gkinfo = new Admission(); 
			                    //截取.XML之前的字符
			                    gkinfo.setSchool_id(StringUtils.substringBeforeLast(fileName,"."));   
			                    NodeList childNodes = personElement.getChildNodes();  
			                    //System.out.println("*****"+childNodes.getLength());    
			                      
			                    for (int j = 0; j < childNodes.getLength(); j++) {  
			                    if(childNodes.item(j).getNodeType()==Node.ELEMENT_NODE){  
			                        if("year".equals(childNodes.item(j).getNodeName())){  
			                            gkinfo.setAnnual_id(childNodes.item(j).getFirstChild().getNodeValue());
			                        }else if("maxScore".equals(childNodes.item(j).getNodeName())){  
			                            gkinfo.setMax(childNodes.item(j).getFirstChild().getNodeValue());
			                        }else if("avgScore".equals(childNodes.item(j).getNodeName())){  
			                            gkinfo.setAverage(childNodes.item(j).getFirstChild().getNodeValue());  
			                        }else if("minScore".equals(childNodes.item(j).getNodeName())){  
			                            gkinfo.setMin(childNodes.item(j).getFirstChild().getNodeValue());  
			                        }else if("rb".equals(childNodes.item(j).getNodeName())){  
			                            gkinfo.setPc(childNodes.item(j).getFirstChild().getNodeValue());  
			                        }        
			                    }  
			                }  
			                    list.add(gkinfo);
			                    //list.add(school_name);
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
//			        return list;
//			        System.out.println(list);
//			        list = getParserYear();  
			        for(int i2=0;i2<list.size();i2++)  
			        {             
			            if (list.get(i2)!=null)  
			            writetoMysql(list.get(i2));  
			        }  
		        }
		        
		    }
			
}
