package gk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;



public class School {
	
	static String sqlStr = "jdbc:mysql://localhost:3306/iot?useUnicode=true&characterEncoding=utf-8&useSSL=false";  
    static String rootName = "";
    static String rootPwd = "";
    
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		File file = new File("school");
	    //showDirectory(file);
	    File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++){
        	StringBuffer stringBuffer = new StringBuffer();
        	try {
        		String line = null ;
        		String fileName = files[i].getName();
        		System.out.println(fileName);
        		BufferedReader br = new BufferedReader(new FileReader("school/"+fileName.toString()));
        		while( (line = br.readLine())!= null ){
        		stringBuffer.append(line);
        		} 
        		} catch (FileNotFoundException e) {
        		       e.printStackTrace();
        		} catch (IOException e) {
        		e.printStackTrace();
        		}
        		try {
        		JSONObject jsonObject = new JSONObject(stringBuffer.toString());
        		JSONArray school = jsonObject.getJSONArray("school") ;
        		
        		String schoolid = null ;
        		//StringBuffer jsonFileInfo = new StringBuffer();
        		String schoolname = null ;
        		String jianjie = null;
        		String province = null;
        		
        	    try {  
                    Class.forName("com.mysql.jdbc.Driver");  
                } catch (ClassNotFoundException e) {  
                    System.out.println("找不到驱动程序类 ，加载驱动失败！");  
                    e.printStackTrace();  
                }  
                Statement st = null;  
                Connection con  =null;  
                try { 
                	con = DriverManager.getConnection(sqlStr, rootName,rootPwd);
                	for (int j = 0; j < school.length(); j++) {
                		schoolid = school.getJSONObject(j).getString("schoolid");
                		System.out.println(schoolid);
                		schoolname = school.getJSONObject(j).getString("schoolname");
                		jianjie = school.getJSONObject(j).getString("jianjie");
                		if(jianjie.equals(null)){
                			jianjie = "暂无简介信息";
                		}else{
                			jianjie = school.getJSONObject(j).getString("jianjie");
                		}
                		province = school.getJSONObject(j).getString("province");
                		try{
                			String sql = "insert ignore into school(school_id,name,introduce,province) values(\""+schoolid+"\",\""+schoolname+"\",\""+jianjie+"\",\""+province+"\")";
                			System.out.println(sql);
                    		st =  con.createStatement(); 
                    		st.executeUpdate(sql);
                		} catch (SQLException e) {  
                            e.printStackTrace(); 
                            //String sql = "insert into school(school_id,name,introduce,province) values(\""+schoolid+"\",\""+schoolname+"\",\""+jianjie+"\",\""+province+"\")";
                			//System.out.println(sql);
                    		//st =  con.createStatement(); 
                    		//st.executeUpdate(sql); 
                    		continue;
                        }
        			} 
                }finally{  
                    try {  
                        st.close();  
                        con.close();  
                    } catch (SQLException e) {  
                        e.printStackTrace();  
                    }  
                }  
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	
        }
		
		}
}
