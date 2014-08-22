package objects;

import main.Bot;

public class Room {
	
	public static String roomName = "";
	public static String roomPass = "";
	public static int roomID = 0;
	public static int inRoom = 0;
	public static boolean roomAvailable = false;
	
	
	public static void setRoomName(String name){
		roomName = name;
	}
	public static void setRoomPass(String pass){
		roomPass = pass;
	}
	public static void setRoomStatus(boolean status){
		roomAvailable = status;
	}	
	public static void add1InRoom(String accName){
		inRoom += 1;
		System.out.println(Bot.timeStamp() + "["+ accName + "]: Joined Room " + roomName + " = " + inRoom + "/"+ Bot.maxbots);
	}
	public static String getRoomName(){
		return roomName;
	}	
	public static void setRoomID(int id){
		roomID = id;
	}
	public static String getRoomPass(){
		return roomPass;
	}
	public static boolean getRoomStatus(){
		return roomAvailable;
	}	
	public static int getRoomID(){
		return roomID;
	}	
	public static int getInRoom(){
		return inRoom;
	}
}
