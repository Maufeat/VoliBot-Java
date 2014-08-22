package objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Accounts {
	 public static List<String> accounts = new ArrayList<String>();
	 
	 public static void loadAccounts() throws IOException{
		 BufferedReader br = new BufferedReader(new FileReader("accounts.txt"));
		 String line;
		 while ((line = br.readLine()) != null) {
		    accounts.add(line);
		 }
		 br.close();
	 }
}
