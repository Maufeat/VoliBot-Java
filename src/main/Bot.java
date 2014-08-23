package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import objects.Accounts;

import com.gvaneyck.rtmp.LoLRTMPSClient;
import com.gvaneyck.rtmp.ServerInfo;


public class Bot {

	private static final String CONFIG_FILE = "config.txt";
	private static String Version = "0.0.1";
	//private static String lollocation = "C:\\Riot Games\\League of Legends\\"; //NOT USED ATM
	public static String PVPVersion = "4.14.VoliBot";
	public static String region = "EUW";
	
	public static int maxbots;
	
	public static LoLRTMPSClient Client;
	public static Map<String, ServerInfo> regionMap;
	public static boolean isInChampSelect;
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("==============================================================");
		System.out.println("VoliBot - Open Source Referral Bot. Version: " + Version);
		System.out.println("==============================================================");
		System.out.println(timeStamp() + "Loading config.txt");
		loadConfiguration();
		initRegionMap();
		System.out.println(timeStamp() + "Loading accounts");
		Accounts.loadAccounts();
		int curRunning = 0;
		for(String acc: Accounts.accounts){
			  String accSplit = acc;
			  String[] splitResult = accSplit.split("[|]");
			  String accname = splitResult[0];
			  String password = splitResult[1];
			  int threadID = curRunning + 1;
			  Thread loginThread = new Thread(new Login(accname, password, threadID));
			  loginThread.start();
			  curRunning += 1;
			  if(curRunning+1 == maxbots)
				  break;
		}
	}
	
	public static String timeStamp(){
	    SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return "["+strDate+"] ";
	}
    public static void initRegionMap() {
        regionMap = new HashMap<String, ServerInfo>();
        regionMap.put("NA", ServerInfo.NA);
        regionMap.put("EUW", ServerInfo.EUW);
        regionMap.put("EUNE", ServerInfo.EUNE);
        regionMap.put("KR", ServerInfo.KR);
        regionMap.put("BR", ServerInfo.BR);
        regionMap.put("TR", ServerInfo.TR);
        regionMap.put("PBE", ServerInfo.PBE);
        regionMap.put("SG", ServerInfo.SG);
        regionMap.put("TW", ServerInfo.TW);
        regionMap.put("TH", ServerInfo.TH);
        regionMap.put("PH", ServerInfo.PH);
        regionMap.put("VN", ServerInfo.VN);
        regionMap.put("OCE", ServerInfo.OCE)
    }
	private static void loadConfiguration() {
		// TODO Auto-generated method stub
		try {
			@SuppressWarnings("resource")
			BufferedReader config = new BufferedReader(new FileReader(CONFIG_FILE));
			String line = "";
			while ((line=config.readLine())!=null)
			{
				if(line.split("=").length == 1) continue ;
				String param = line.split("=")[0].trim();
				String value = line.split("=")[1].trim();
				if(param.equalsIgnoreCase("lollcation"))
				{
					//Bot.lollocation = value;
				}
				else if(param.equalsIgnoreCase("version"))
				{
					Bot.PVPVersion = value;
				}
				else if(param.equalsIgnoreCase("region"))
				{
					Bot.region = value;
				}
				else if(param.equalsIgnoreCase("maxBots"))
				{
					Bot.maxbots = Integer.parseInt(value);
				}
			}
		} catch (Exception e) {
            System.out.println(e.getMessage());
			System.out.println("Config.txt not found or invisible!");
			System.out.println("Closing bot...");
			System.exit(1);
		}
	}
}
