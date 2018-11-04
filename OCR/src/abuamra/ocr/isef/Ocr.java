package abuamra.ocr.isef;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import abuamra.ocr.isef.GPIO;


public class Ocr {


	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception 
	{	

		InputStream is ;
		Process p ;
		ProcessBuilder pb ;

		pb = new ProcessBuilder("espeak","-ven+f3" , "\"Reading mode\"");
		pb.redirectErrorStream(true);                                                                       // 
		//
		p = pb.start();                                                                                   //

		//to close camera from athor program
		Thread.sleep(3000);
		pb = new ProcessBuilder("espeak","-ven+f3" , "\"please wait\"");
		pb.redirectErrorStream(true);                                                                       // 
		//
		p = pb.start();                                                                                   //
		
		Thread.sleep(3000);
		
		pb = new ProcessBuilder("fswebcam","-r 1280x720","--no-banner","/root/OCR/image.jpg");
		pb.redirectErrorStream(true);

		p = pb.start();
		Thread.sleep(3000);


		///////////Terminal////////////////////////////////////////////////////////////////////////////////////////////
		// get text from (teee.png) and send it to (OOO.txt)
		// Using tesseract
		pb = new ProcessBuilder("tesseract", "/root/OCR/image.jpg","/root/OCR/out");
		pb.redirectErrorStream(true);

		p = pb.start();
		Thread.sleep(3000);
		is = p.getInputStream();
		System.out.println(toString(is));
		is.close();
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//////////Get text from OOO.txt and put it in (line) variable ////////////////////////////////////////////////
		//
		BufferedReader reader = new BufferedReader(new FileReader("/root/OCR/out.txt"));  //
		//
		String line = null;	                                                                                //
		//
		//To Specialized value                                                                              //
		StringBuilder stringBuilder = new StringBuilder();                                                  //
		//
		//reader looping                                                                                    //
		while ((line = reader.readLine()) != null) {                                                        //
			//			System.out.println(line);                                                                       //
			stringBuilder.append(line);                                                                     //
			stringBuilder.append(System.lineSeparator());                                                   //
			line = reader.readLine();                                                                       //
		}                                                                                                   //
		//
		//Builder to String                                                                                 //
		String allText = stringBuilder.toString();                                                          //
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////   
		System.out.println(allText);
		//////////Text-to-speach//////////////////////////////////////////////////////////////////////////////////////
		//Using (espeak)                                                                                    //
		pb = new ProcessBuilder("espeak","-ven+f3" , "\""+ allText +"\"");
		pb.redirectErrorStream(true);                                                                       // 
		//
		p = pb.start();                                                                                   //



		while (true){
			GPIO gpio199 = new GPIO(199);
			GPIO gpio200 = new GPIO(200);
			GPIO gpio204 = new GPIO(204);

			if (gpio199.getState() == 1){
				//lunch FR
				pb = new ProcessBuilder("xvfb-run","-a","java" ,"-jar","/root/PROJECT_MAIN/FaceRecognition.jar");
				pb.redirectErrorStream(true);
				pb.start();

				System.exit(0);
				System.exit(1);
			}

			if (gpio200.getState() == 1){
				//lunch OCR
//				pb = new ProcessBuilder("xvfb-run","-a","java" ,"-jar","/root/PROJECT_MAIN/OCR.jar");
				pb = new ProcessBuilder("espeak","-ven+f3" , "\"computer vision for blind\"");
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

				System.exit(0);
				System.exit(1);
			}
			Thread.sleep(500);
		}
	}                                                                                                       //
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////










	///////Function///////////////////////////////////////////////////////////////////////////////////////////////
	////////to convert OUTPUT 
	///////( from terminal ) to String
	public static String toString(InputStream is) throws IOException {
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		int n;
		while ((n = reader.read(buffer)) != -1) {
			writer.write(buffer, 0, n);
		}
		return writer.toString();
	}
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////