package WebServices.RestAPI;

import org.testng.annotations.Test;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.asserts.SoftAssert;

public class NewTest {
	String BoardId;
	RestJava rest;
	String str;
	String Data;
	SoftAssert softAssert = new SoftAssert();
	JSONArray pl;
	Long Player_Id;
	@Test
	public void Test1Create_Board() throws IOException, Exception {
		rest = new RestJava();
		URL url = new URL("http://10.0.1.86/snl/rest/v1/board/new.json");
		str = rest.GetMethod(url);
		if (str == "") {
			softAssert.assertTrue(false);
		} else {
			JSONParser p = new JSONParser();
			JSONObject obj = new JSONObject();
			obj = (JSONObject) p.parse(str);
			JSONObject str1 = (JSONObject) obj.get("response");
			JSONObject str2 = (JSONObject) str1.get("board");
			BoardId = str2.get("id").toString();
		}
		softAssert.assertAll();
	}

	@Test
	public void Test2Player_Add() throws Exception {
		int count;
		for (int i = 0; i < 5; i++) {

			URL url1 = new URL("http://10.0.1.86/snl/rest/v1/board/" + BoardId + ".json");
			rest = new RestJava();
			str = rest.GetMethod(url1);
			pl = rest.player_Count(str);
			if (i == 0) {
				count = 0;
			} else
				count = pl.size();
			URL url = new URL("http://10.0.1.86/snl/rest/v1/player.json");
			JSONObject obj = new JSONObject();
			obj.put("name", "Hari" + i + "");
			JSONObject obj1 = new JSONObject();
			obj1.put("board", BoardId);
			obj1.put("player", obj);
			Data = obj1.toJSONString();
			rest.PostMethod(url, Data);
			rest = new RestJava();
			str = rest.GetMethod(url1);
			pl = rest.player_Count(str);
			if (count == pl.size()) {
				softAssert.assertTrue(false);
			} else {
				count = rest.player_Count(str).size();
				if (count > 4) {
					rest.Delete_Player(pl);
				} else
					System.out.println("Congrats!!! Player Has been Added");
			}

			softAssert.assertAll();
		}
	}

	@Test
	public void Test3Move() throws Exception {
		JSONObject obj;
		Long temp;
		do{
			int turn=rest.Return_Turn(str);
			obj=(JSONObject) pl.get(turn-1);
			Player_Id=(Long) obj.get("id");
			URL url = new URL("http://10.0.1.86/snl/rest/v1/move/"+BoardId+".json?player_id="+Player_Id);
			rest.GetMethod(url);
			temp=(Long)obj.get("position");
		}while(temp.intValue()==25);
		System.out.println("Player :"+Player_Id+"Player Name:"+obj.get("name")+"Won the Match");
	}
}
