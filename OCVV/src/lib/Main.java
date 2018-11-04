package lib;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.IOException;

import lib.GPIO;

public class Main {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	  public static void main(String[] args) throws InterruptedException, IOException {
		    TEST5 myApplet = new TEST5(); // define applet of interest
		    Frame myFrame = new Frame("Applet Holder"); // create frame with title
		    // Call applet's init method (since Java App does not
		    // call it as a browser automatically does)
		    myApplet.init();
		    myApplet.start();
		    // add applet to the frame
		    myFrame.add(myApplet, BorderLayout.CENTER);
		    myFrame.pack(); // set window to appropriate size (for its elements)
		    myFrame.setVisible(true); // usual step to make frame visible
		    
		    
			ProcessBuilder pb;
//			Process p ;

			while (true){
				GPIO gpio199 = new GPIO(199);
				GPIO gpio200 = new GPIO(200);
				GPIO gpio204 = new GPIO(204);

				if (gpio199.getState() == 1){
					//train FACE
					myApplet.trainFace();
				}

				if (gpio200.getState() == 1){
					//lunch OCR
					//java -jar /root/PROJECT_MAIN/OCR.jar

					pb = new ProcessBuilder("java","-jar","/root/PROJECT_MAIN/OCR.jar");
					pb.redirectErrorStream(true);
					pb.start();
//					Thread.sleep(1000);					
					System.exit(0);
					System.exit(1);
				}

				if (gpio204.getState() == 1){
					//lunch OR
					
					pb = new ProcessBuilder("xvfb-run","-a","java" ,"-jar","/root/PROJECT_MAIN/ObjectsRecognition.jar");
					pb.redirectErrorStream(true);
					pb.start();
//					Thread.sleep(1000);
					
					System.exit(0);
					System.exit(1);
				}
				Thread.sleep(500);
			}
	  } // end main
}

//import com.googlecode.javacpp.Loader;
//import com.googlecode.javacv.*;
//
//import static com.googlecode.javacv.cpp.opencv_core.*;
//import static com.googlecode.javacv.cpp.opencv_imgproc.*;
//import static com.googlecode.javacv.cpp.opencv_calib3d.*;
//import static com.googlecode.javacv.cpp.opencv_objdetect.*;
//
//public class Main {
//
//public static void main(String[] args) throws Exception {
//OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
//grabber.start();
//
//IplImage image = grabber.grab();
//
//
//CanvasFrame canvasFrame = new CanvasFrame("Web Cam Detection Going on");
//canvasFrame.setCanvasSize(image.width(), image.height());
//canvasFrame.setDefaultCloseOperation(CanvasFrame.EXIT_ON_CLOSE);
//
//// CvMemStorage storage = CvMemStorage.create();
//
//while (true) {
//image=grabber.grab();
//canvasFrame.showImage(image);
//}
//
//}
//
//}
//
