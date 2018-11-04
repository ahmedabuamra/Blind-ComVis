package abuamra.ocr.isef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GPIO {

	String port;
	int pin;

	//Constructor
	public GPIO(int pin){
		this.port = "gpio"+pin;
		this.pin = pin;
	}

	Process p = null ;
	ProcessBuilder pb ;

	public int getState()
	{
		//String command = String.format("cat /sys/class/gpio/%s/value",this.port);
		try {
			pb = new ProcessBuilder("cat","/sys/class/gpio/"+this.port+"/value");
			pb.redirectErrorStream(true);

			try {
				p = pb.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//			p = Runtime.getRuntime().exec(new String[] {command});
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder text = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null){
				text.append(line);
				text.append("\n");
			}
			try {
				String retour= text.toString();
				if(retour.equals(null)){
					return -1;
				} else {
					return Integer.parseInt(retour.substring(0, 1));
				}
			} catch(NumberFormatException nfe) {
				return -1;
			}
		} catch (IOException e) {
			return -1;
		}
	}
}