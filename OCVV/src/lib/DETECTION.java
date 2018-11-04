package lib;

import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

import java.io.IOException;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import com.googlecode.javacv.cpp.opencv_objdetect;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

public class DETECTION {
  public static void main(String[] args) throws Exception {
    
    // Load object detection
    Loader.load(opencv_objdetect.class);

    // Construct classifiers from xml.
    CvHaarClassifierCascade CarClassifier1 = loadHaarClassifier(
        "/home/abuamra/Desktop/HaarCascadeXMLs/CarDetect-master/carcascade.xml");
    CvHaarClassifierCascade CarClassifier2 = loadHaarClassifier(
            "/home/abuamra/Desktop/HaarCascadeXMLs/CarDetect-master/carcascade_full_14.xml");
    CvHaarClassifierCascade CarClassifier3 = loadHaarClassifier(
            "/home/abuamra/Desktop/HaarCascadeXMLs/CarDetect-master/carcascade_full_18.xml");
    CvHaarClassifierCascade CarClassifier4 = loadHaarClassifier(
            "/home/abuamra/Desktop/HaarCascadeXMLs/CarDetect-master/carcascade_pics_at_tollnaka_12_stages.xml");

    
    CvHaarClassifierCascade wallClockClassifire = loadHaarClassifier("/home/abuamra/Desktop/HaarCascadeXMLs/WallClock/classifierWallClock.xml");
    
    
    // Grab the default video device. This work for both built-win 
    // and usb webcams.
    CvCapture capture = opencv_highgui.cvCreateCameraCapture(0);

    // Set the capture resolution 480x320 gives decent quality 
    // and the lower resolution will make our real-time video 
    // processing a little faster.    
    opencv_highgui.cvSetCaptureProperty(capture,
        opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT, 320);
    opencv_highgui.cvSetCaptureProperty(capture,
        opencv_highgui.CV_CAP_PROP_FRAME_WIDTH, 480);

    // Contruct a JavaCV Image that matches the properties of the 
    // captured imaged.    
    IplImage grabbedImage = opencv_highgui.cvQueryFrame(capture);
    IplImage mirrorImage = grabbedImage.clone();
    IplImage grayImage = IplImage.create(mirrorImage.width(),
        mirrorImage.height(), IPL_DEPTH_8U, 1);

    // OpenCV's C++ roots means we need to allocate memory
    // to use as working storage for object detection.
    CvMemStorage faceStorage = CvMemStorage.create();
////    CvMemStorage handStorage = CvMemStorage.create();

    //Create a frame to echo to.
    CanvasFrame frame = new CanvasFrame("Object Detection Demo", 1);

    // Keep looping while our frame is visible and we're getting 
    // images from the webcam
    while (frame.isVisible()
        && (grabbedImage = opencv_highgui.cvQueryFrame(capture)) 
          != null) {

      // Clear out storage 
      cvClearMemStorage(faceStorage);
////      cvClearMemStorage(handStorage);

      // Flip the image because a mirror image looks more natural 
      cvFlip(grabbedImage, mirrorImage, 1);
      // Create a black and white image - best for face detection
      // according to OpenCV sample.
      cvCvtColor(mirrorImage, grayImage, CV_BGR2GRAY);

      // Find faces in grayImage and mark with green 
      // rectangles on mirrorImage.
      findAndMarkObjects(CarClassifier1, faceStorage,
          CvScalar.GREEN, grayImage,mirrorImage , "Car in front you");
      
      findAndMarkObjects(CarClassifier2, faceStorage,
              CvScalar.BLACK, grayImage,mirrorImage , "Car in front you");

      findAndMarkObjects(CarClassifier3, faceStorage,
              CvScalar.RED, grayImage,mirrorImage , "Car in front you");

      findAndMarkObjects(CarClassifier4, faceStorage,
              CvScalar.YELLOW, grayImage,mirrorImage , "Car in front you");

      findAndMarkObjects(wallClockClassifire, faceStorage,
              CvScalar.YELLOW, grayImage,mirrorImage , "Wall Clock in front you");

      
      // display mirrorImage on frame
      frame.showImage(mirrorImage);
    }

   // display captured image on frame
    frame.dispose();
    opencv_highgui.cvReleaseCapture(capture);
  }

  /**
   * Find objects matching the supplied Haar classifier.
   * 
   * @param classifier The Haar classifier for the object we're looking for.
   * @param storage In-memory storage to use for computations
   * @param colour Colour of the marker used to make objects found.
   * @param inImage Input image that we're searching.
   * @param outImage Output image that we're going to mark and display.
   * @throws IOException 
   * @throws InterruptedException 
   */
  private static void findAndMarkObjects(
      CvHaarClassifierCascade classifier, 
      CvMemStorage storage, 
      CvScalar colour,
      IplImage inImage,
      IplImage outImage
      , String speech) throws IOException, InterruptedException {
    
    CvSeq object = cvHaarDetectObjects(inImage, classifier,
        storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
    int total = object.total();
    for (int i = 0; i < total; i++) {
      CvRect r = new CvRect(cvGetSeqElem(object, i));
      int x = r.x(), y = r.y(), w = r.width(), h = r.height();
      cvRectangle(outImage, cvPoint(x, y), cvPoint(x + w, y + h),
          colour, 1, CV_AA, 0);
      
      ProcessBuilder pb;
	  Process p ;

	    pb = new ProcessBuilder("espeak","-ven+f3" , "-s130" , "\""+ speech + "\"");
	    pb.redirectErrorStream(true);
	    pb.start();
//	    Thread.sleep(5000);

    }
  }

  /**
   * Load a Haar classifier from its xml representation.
   * 
   * @param classifierName Filename for the haar classifier xml.
   * @return a Haar classifier object.
   */
  private static CvHaarClassifierCascade loadHaarClassifier(
      String classifierName) {
    
    CvHaarClassifierCascade classifier = new CvHaarClassifierCascade(
        cvLoad(classifierName));
    if (classifier.isNull()) {
      System.err.println("Error loading classifier file \"" + classifier
          + "\".");
      System.exit(1);
    }
    
    return classifier;
  }
}