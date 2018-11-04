package abuamra.gpio.isef;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws InterruptedException, IOException {
		
		ProcessBuilder pb;
		
	    pb = new ProcessBuilder("espeak","-ven+f3" , "\"device is ready to use\"");
	    pb.redirectErrorStream(true);
		try {
			pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		while (true){
			GPIO gpio199 = new GPIO(199);
			GPIO gpio200 = new GPIO(200);
			GPIO gpio204 = new GPIO(204);

			if (gpio199.getState() == 1){
				//lunch FR
				pb = new ProcessBuilder("xvfb-run","-a","java" ,"-jar","/root/PROJECT_MAIN/FaceRecognition.jar");
				pb.redirectErrorStream(true);
				pb.start();
				
				pb = new ProcessBuilder("killall","pulseaudio");
				pb.redirectErrorStream(true);
				pb.start();

				
				System.exit(0);
				System.exit(1);
			}

			if (gpio200.getState() == 1){
				//lunch OCR
				pb = new ProcessBuilder("xvfb-run","-a","java" ,"-jar","/root/PROJECT_MAIN/OCR.jar");
				pb.redirectErrorStream(true);
				pb.start();
				
				pb = new ProcessBuilder("killall","pulseaudio");
				pb.redirectErrorStream(true);
				pb.start();
				
				System.exit(0);
				System.exit(1);
			}

			if (gpio204.getState() == 1){
				//lunch OR
				pb = new ProcessBuilder("xvfb-run","-a","java" ,"-jar","/root/PROJECT_MAIN/ObjectsRecognition.jar");
				pb.redirectErrorStream(true);
				pb.start();

				pb = new ProcessBuilder("killall","pulseaudio");
				pb.redirectErrorStream(true);
				pb.start();
				
				System.exit(0);
				System.exit(1);
			}
			Thread.sleep(500);
		}
	}
}
