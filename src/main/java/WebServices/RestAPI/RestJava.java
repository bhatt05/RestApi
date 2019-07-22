package WebServices.RestAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RestJava {
String GetMethod(URL url) throws IOException {
		HttpURLConnection con=(HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		String readLine;
		int responseCode = con.getResponseCode();
		StringBuffer response = null;
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
	        response = new StringBuffer();
	        while ((readLine = in.readLine()) != null) {
	            response.append(readLine);
	        }
	        in .close();
	        return response.toString();
	    }
	    else {
	        System.out.println("GET NOT WORKED");
	        return "";
	    }
	    
	}
	void PostMethod(URL url,String Data) throws IOException {  
		
		HttpURLConnection pCon = (HttpURLConnection) url.openConnection();
		pCon.setRequestMethod("POST");
		pCon.setRequestProperty("Content-Type", "application/json");
		pCon.setDoOutput(true);
		OutputStream os =(OutputStream) pCon.getOutputStream();
		byte[] data=Data.getBytes();
		os.write(data);
		os.flush();
		os.close();
		int responseCode = pCon.getResponseCode();
		System.out.println(responseCode);
		System.out.println(pCon.getResponseMessage());
		if (responseCode == 500) {
		    System.out.println("POST WORKED");
		    } 
		else {
			System.out.println("POST NOT WORKED");
		}
		   
	}
	JSONArray player_Count(String JSONData) throws Exception {
		int count=0;
		JSONParser p=new JSONParser();
	  	JSONObject obj=new JSONObject();
	  	obj=(JSONObject) p.parse(JSONData);
	  	JSONObject str1=(JSONObject) obj.get("response");
	  	JSONObject str2=(JSONObject)str1.get("board");
	  	JSONArray pl= (JSONArray) str2.get("players");
		return pl;
	}
	void Delete_Player(JSONArray arr) throws Exception {
		int count=arr.size();
		JSONObject obj=(JSONObject) arr.get(count-1);
		String ID=obj.get("id").toString();
		URL url=new URL("http://10.0.1.86/snl/rest/v1/player/"+ID+".json");
		HttpURLConnection con=(HttpURLConnection) url.openConnection();
		con.setRequestMethod("DELETE");
		int responseCode = con.getResponseCode();
		System.out.println(responseCode);
		System.out.println(con.getResponseMessage());
		System.out.println("More than 4 players can not play the game");
	}
	public int Return_Turn(String JSONData) throws Exception {
		Long turn;
		JSONParser p=new JSONParser();
	  	JSONObject obj=new JSONObject();
	  	obj=(JSONObject) p.parse(JSONData);
	  	JSONObject str1=(JSONObject) obj.get("response");
	  	JSONObject str2=(JSONObject)str1.get("board");
	  	turn=(Long) (str2.get("turn"));
	  	int tu=turn.intValue();
		return tu;
	}
}
