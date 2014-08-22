package main;

import java.io.IOException;
import objects.Room;
import com.gvaneyck.rtmp.encoding.TypedObject;
import com.gvaneyck.rtmp.LoLRTMPSClient;
import com.gvaneyck.rtmp.ServerInfo;
import com.kolakcc.loljclient.StartupClass;
import com.kolakcc.loljclient.model.Summoner;

import main.Bot;
import utils.CryptManager;

public class Login implements Runnable{

	LoLRTMPSClient Client;
	String AccountName;
	String Password;
	String summonerName;
	int Id;	
	int RPBalance;
	int IPBalance;
	Summoner summonerData;
	
	public Login(String accName, String password, int id){
		AccountName = accName;
		Password = password;
		Id = id;
	}
	
	public void run() {
		ServerInfo serverInfo = Bot.regionMap.get(Bot.region.toUpperCase());
		Client = new LoLRTMPSClient(serverInfo, Bot.PVPVersion, AccountName, Password);
		System.out.println(Bot.timeStamp() + "["+ AccountName + "]: Connecting...");
		try {
			Client.connectAndLogin();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Client.join();
		getSummonerName();
		System.out.println(Bot.timeStamp() + "["+ AccountName + "]: Logged as "+ summonerData.getSummonerName() +" @ Level " + (int) summonerData.getSummonerLevel().getSummonerLevel());
		if(Id == 1){ //HOST
			createCustomGame();
		}else{ //PLAYER			
			while(Room.getRoomStatus() != true){
				try {Thread.sleep(2000);}catch (Exception e) {}
			}
			joinCustomGame();
		}
		//Client.close();
		//System.out.println(Bot.timeStamp() + "["+ AccountName + "]: Logged out!");
	}
	
	public void getSummonerName(){
		try {
			int id = Client.invoke("clientFacadeService",
					"getLoginDataPacketForUser", new Object[] {});		
			TypedObject summoner = Client.getResult(id).getTO("data").getTO("body");
			summonerData = new Summoner(summoner.getTO("allSummonerData"));
			RPBalance = summoner.getDouble("rpBalance").intValue();
			IPBalance = summoner.getDouble("ipBalance").intValue();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void joinCustomGame(){
		try {
			Client.invoke("gameService", "joinGame", new Object[] { new Double(Room.getRoomID()), Room.getRoomPass() });
			Room.add1InRoom(AccountName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createCustomGame(){
        TypedObject practiceGameConfig = new TypedObject("com.riotgames.platform.game.PracticeGameConfig");
        TypedObject map = new TypedObject("com.riotgames.platform.game.map.GameMap");
        
        String gamePassword = CryptManager.randomString(9);
        String gameName = CryptManager.randomString(9);
        Room.setRoomName(gameName);
        Room.setRoomPass(gamePassword);
        
        map.put("totalPlayers", 10);
        map.put("minCustomPlayers", 1);
        map.put("mapId", 8);
        map.put("displayName", "Crystal Scar");
        map.put("name", "CrystalScar");
        map.put("description", "");
        map.put("dataVersion", null);
        map.put("futureData", null);

        practiceGameConfig.put("gamePassword", gamePassword);
        practiceGameConfig.put("allowSpectators", "NONE");
        practiceGameConfig.put("region", "EUW");
        practiceGameConfig.put("gameName", gameName);
        practiceGameConfig.put("maxNumPlayers", Bot.maxbots);
        practiceGameConfig.put("gameTypeConfig", 1);
        practiceGameConfig.put("gameMap", map);
        practiceGameConfig.put("passbackDataPacket", null);
        practiceGameConfig.put("passbackUrl", null);
        practiceGameConfig.put("gameMode", "CLASSIC");
        try{
            int id = Client.invoke("gameService", "createPracticeGame", new Object[] { practiceGameConfig });
            TypedObject result = Client.getResult(id);
            System.out.println(Bot.timeStamp() + "["+ AccountName + "]: Created Game: " + gameName + " pass: " + gamePassword);
            result.getTO("data").getTO("body");
            int roomID = result.getTO("data").getTO("body").getInt("id");
            Room.setRoomID(roomID);Room.setRoomStatus(true);Room.add1InRoom(AccountName);
			while(Room.getInRoom() != Bot.maxbots){
				Thread.sleep(10000);Room.add1InRoom(AccountName);
			}
			System.out.println(Bot.timeStamp() + "["+ AccountName + "]: TODO: Now, goto champ select.");
		}catch(Exception ex){
            ex.printStackTrace();
        }
		try {
			int id = Client.invoke("gameService", "startChampionSelection", new Object[] { Room.getRoomID(), 1 });
			TypedObject result1 = Client.getResult(id).getTO("data");
			System.out.println(result1.toPrettyString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
