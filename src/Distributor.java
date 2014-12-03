import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Distributor {
	public static void main(String arg[]) throws IOException {
		int hosts = 20;
		int counter = 0;
		File file = new File("access.log.3.replay");
		List<Long> iplist = new ArrayList<Long>();
		HashMap<String,Long> size_from_header = new HashMap<String,Long>();

		List<String>[] receivedLog = new List[hosts];
		for (int i = 0; i < hosts; ++i) {
			receivedLog[i] = new ArrayList<String>();
		}

		try {
			Scanner sc = new Scanner(file);
			String line;
			String addr;
			int separator;
			Long decimalAddr;
			
			int req_separate;
			int start_url;
			int end_url;
			
			String based_url = "http://www.cs.hbg.psu.edu";
			String full_url;
			String path_url;
			Long content_length;
			

			while (sc.hasNextLine()) {
				line = sc.nextLine();
								
				separator = line.indexOf(';');
				addr = line.substring(0, separator);
				decimalAddr = ipToLong(addr);
				
				
				
				
				req_separate = line.indexOf("GET");
				if(req_separate == -1)
				{
					req_separate = line.indexOf("HEAD");
					if(req_separate == -1)
					{
						req_separate = line.indexOf("POST");
					}
					if(req_separate == -1)continue;
						
					start_url = req_separate+5; 
				}
				
				else
				{
					start_url = req_separate+4;
				}
				
				end_url = line.lastIndexOf(" ");
				
				System.out.println("[" + line.substring(start_url, end_url) + "]");
				
				path_url = line.substring(start_url, end_url);
				
				content_length = size_from_header.get(path_url);
				
				if(content_length == null)
				{
					full_url = based_url + path_url;
					content_length = getHeaderDetail(full_url);
					if(content_length == null)continue;
					else
					{
						size_from_header.put(path_url, content_length);
					}
				}
								
				if (!iplist.contains(decimalAddr)) {
					//System.out.println(counter + " : " + counter % hosts);
					iplist.add(decimalAddr);
					receivedLog[counter % hosts].add(line+";"+content_length);
					counter++;
				}
				else
				{
					receivedLog[ iplist.indexOf(decimalAddr)% hosts ].add(line+";"+content_length);
				}
			}

			sc.close();
			
			try {
				for (int i = 0; i < hosts; ++i) {
					
					PrintWriter writer;
					writer = new PrintWriter("output/ipfor" + (i+1) + ".txt","UTF-8");

					for(int j = 0 ; j < receivedLog[i].size();++j)
					{
						writer.println(receivedLog[i].get(j));
					}
					writer.close();
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Long getHeaderDetail(String url) throws IOException{
		System.out.println(url);
		URL obj = new URL(url);
		URLConnection conn = obj.openConnection();
	 
		//get header by 'key'
		String length = conn.getHeaderField("Content-Length");
		if(length == null)return null;
		return Long.parseLong(length);
	}

	public static long ipToLong(String ipAddress) {

		String[] ipAddressInArray = ipAddress.split("\\.");

		long result = 0;
		for (int i = 0; i < 4; i++) {
			result <<= 8;
			result += Integer.parseInt(ipAddressInArray[i]);
		}

		return result;
	}
}
