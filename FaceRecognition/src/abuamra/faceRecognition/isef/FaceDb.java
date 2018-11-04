package abuamra.faceRecognition.isef;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;


// todo: this should be persisted in db
public class FaceDb {

	private HashMultimap<String, BufferedImage> pics = HashMultimap.create();
	private Map<String, BufferedImage> idIndex = Maps.newHashMap();
	public static int idCounter = 1;

	public static String removeExtension(String s) {

		String separator = System.getProperty("file.separator");
		String filename;

		// Remove the path upto the filename.
		int lastSeparatorIndex = s.lastIndexOf(separator);
		if (lastSeparatorIndex == -1) {
			filename = s;
		} else {
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1)
			return filename;

		return filename.substring(0, extensionIndex);
	}


	public static void counter(){
		File folder = new File("/root/workspace/FaceRecognition/dataTest/");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				idCounter++;
			}
		}
	}



	public ArrayList<String> getAllImages() throws IOException {
		ArrayList<String> resultList = new ArrayList<String>(256);
		File directory = new File("/root/workspace/FaceRecognition/dataTest/");
		File[] f = directory.listFiles();
		for (File file : f) {
			if (file != null && file.getName().toLowerCase().endsWith(".jpg") && !file.getName().startsWith("tn_")) {
				String name = removeExtension(file.getName());
				pics.put(name,ImageIO.read(file));
			}
			////          if (descendIntoSubDirectories && file.isDirectory()) {
			////              ArrayList<String> tmp = getAllImages(file, true);
			////              if (tmp != null) {
			////                  resultList.addAll(tmp);
			////              }
			////          }
		}
		////      if (resultList.size() > 0)
		////          return resultList;
		////      else
		return null;
	}

	public String add(String name, BufferedImage pic) throws IOException {
		pics.put(name, pic);
		final String id = UUID.randomUUID().toString();
		idIndex.put(id, pic);

		File outputfile = new File("/root/workspace/FaceRecognition/dataTest/"+name+".jpg");
		ImageIO.write(pic, "jpg", outputfile);
		////    System.out.println(pics.toString());
		afterWrite();
		return id;
	}

	public Set<BufferedImage> get(String name) {
		return pics.get(name);
	}

	public Set<String> names() {
		return pics.keySet();
	}

	public int size() {
		return pics.values().size();
	}

	// todo: delete function

	@Override
	public int hashCode() {
		return pics.hashCode() ^ idIndex.hashCode();
	}

	@Override
	public boolean equals(Object that) {
		return that instanceof FaceDb && ((FaceDb) that).idIndex.equals(idIndex) && ((FaceDb) that).pics.equals(pics);
	}

	private void afterWrite() {
		FacialRecognition.invalidateTrainingCache(this);
	}
}
