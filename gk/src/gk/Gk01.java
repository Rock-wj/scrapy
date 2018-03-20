package gk;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.mysql.jdbc.Connection;

public class Gk01 {
	private String province_name;
	private String province_id;

	public String getProvince_id() {
		return province_id;
	}

	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	
	
	private static ArrayList<Gk01> getParser_Province_name(){
		ArrayList<Gk01> list =new ArrayList<Gk01>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try{
			builder = factory.newDocumentBuilder();
			Document doc;
			doc = builder.parse(new File("province.xml"));
			//NodeList personNodes = doc.getElementsByTagName("div");
			//System.out.println(personNodes.getLength());
			for(int i=0; i < 33; i++){
				Gk01 gk01 = new Gk01();
				gk01.setProvince_name(doc.getElementsByTagName("em").item(i).getFirstChild().getNodeValue());
				gk01.setProvince_id(doc.getElementsByTagName("ol").item(i).getFirstChild().getNodeValue());
				list.add(gk01);
			}
		}catch (SAXException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}catch (ParserConfigurationException e){
			e.printStackTrace();
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		return list;
	}
	
	static String sqlstr = "jdbc:mysql://localhost:3306/iot?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	static String rootName = "";
	static String rootPwd = "";
	
	public static void writetoMysql(Gk01 gk01){
		System.out.println(gk01);
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			System.out.println("");
			e.printStackTrace();
		}
		Statement st = null;
		Connection con = null;
		try{
			con = (Connection) DriverManager.getConnection(sqlstr, rootName, rootPwd);
			String province_name = gk01.getProvince_name();
			String province_id = gk01.getProvince_id();
			String sql = "insert ignore into province(province_id,province_name) values(\""+province_id+"\",\""+province_name+"\")";
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
	public static void main(String[] args){
		ArrayList<Gk01> list = new ArrayList<Gk01>();
		System.out.println(list);
		list = getParser_Province_name();
		for(int i=0;i<list.size();i++)  
        {             
            if (list.get(i)!=null)  
            	writetoMysql(list.get(i));  
        } 
	}
	
	
	
	
	
	
	
	
}
