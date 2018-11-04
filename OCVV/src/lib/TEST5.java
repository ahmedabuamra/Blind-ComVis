package lib;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JOptionPane;

import lib.FacialRecognition.PotentialFace;

import com.google.common.collect.Lists;


/* <applet code = "test" width = 300 height = 300> </applet> */

public class TEST5 extends JApplet implements Runnable, MouseListener  {
			
	private final WebCam cam = new WebCam();

	private List<Rectangle> currentFaces = Lists.newArrayList();
	private BufferedImage currentDisplay;
	  
	private boolean running = true;
	private boolean dialog = false;

    Process p ;
    ProcessBuilder pb ;

	  
	@Override
	public void init() {
		
	    pb = new ProcessBuilder("espeak","-ven+f3" , "\"Face Recognition mode\"");
	    pb.redirectErrorStream(true);
		try {
			p = pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    pb = new ProcessBuilder("espeak","-ven+f3" , "\"Handeling,, Please wait\"");
	    pb.redirectErrorStream(true);
		try {
			p = pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//to close camera from athor program
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		cam.start();
		BufferedImage first;
		while((first = cam.capture()) == null);
		setSize(first.getWidth(), first.getHeight());
		addMouseListener(this);
	  }	
	
	
	  
	@Override
    public void start() {
		new Thread(this).start();
		try {
			db.getAllImages();
			db.counter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	public void run() {
		while (running) {
		      repaint();
		}
	}

	
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}

	
	
	@Override
	public void paint(Graphics g) {
		currentDisplay = cam.capture();
		if (currentDisplay == null || dialog) {
			return;
		}

		try {
			drawFaces(currentDisplay);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.drawImage(currentDisplay, 0, 0, null);
	}
	

	
	public void drawFaces(BufferedImage image) throws IOException {
		final List<PotentialFace> faces = FacialRecognition.run(image, db);
		if (faces.isEmpty()) {
			return;
		}
		Graphics2D g2 = image.createGraphics();
		g2.setStroke(new BasicStroke(2));
		currentFaces.clear();
		for (PotentialFace face : faces) {
			final Rectangle r = face.box;
		    final Color c1, c2;
		    final String msg;
		    if (face.name == null) {
		    	c1 = c2 = new Color(scale(r.x, getWidth(), 255d), scale(r.y, getHeight(), 255d), 0).brighter();
		        msg = "Click to tag";
		     } else {
		        c1 = new Color(face.name.hashCode()).brighter();
		        c2 = new Color((int) (c1.getRGB() - 10*face.confidence));
		        msg = String.format("%s: %f", face.name, face.confidence);
		     }
		     g2.setColor(c1);
		     g2.drawRect(r.x, r.y, r.width, r.height);
		     g2.setColor(c2);
		     g2.drawString(msg, r.x + 5, r.y - 5);
		     currentFaces.add(r);
		}
	}

	
	
	private static int scale(double num, double maxNum, double maxTarget) {
		return (int) (num*maxTarget/maxNum);
	}

	
	
	@Override
	public void stop() {
		running = false;
	}

	@Override
	public void destroy() {
		cam.stop();
	}
	
	private final FaceDb db = new FaceDb();

	public void trainFace(){
		if (currentFaces == null) {
		    pb = new ProcessBuilder("espeak","-ven+f3" , "\""+ "Sorry, No face detected. please try again" + "\"");
		    pb.redirectErrorStream(true);
			try {
				p = pb.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		dialog = true;
		
		for(Rectangle r : currentFaces) {
				
		    	pb = new ProcessBuilder("espeak","-ven+f3" , "\""+ "please, give me the name" + "\"");
		    	pb.redirectErrorStream(true);
		    	
				try {
					p = pb.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		    	try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				final BufferedImage clickedFace = currentDisplay.getSubimage(r.x, r.y, r.width, r.height);
//		        final ImageIcon preview = new ImageIcon(clickedFace, "Preview");
//		        final String name = (String) JOptionPane.showInputDialog(null, "Save as?", "Tag", JOptionPane.QUESTION_MESSAGE, preview, null, null);
		        	System.out.println("Saving " + db.idCounter++ + "...");
		        	try {
		        		db.add(String.valueOf(db.idCounter), clickedFace);
		        	} catch (IOException e) {
		        		// TODO Auto-generated catch block
		        		e.printStackTrace();
		        	}
		        	
		        	
			    	try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	
			    	int recorCounter = db.idCounter;
			    	pb = new ProcessBuilder("arecord","-d 3","/root/workspace/OCVV/soundNames/"+String.valueOf(recorCounter)+".wav","-D","sysdefault:CARD=1");
			    	pb.redirectErrorStream(true);
			    	
					try {
						p = pb.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	
			    	try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	
			}
		    	dialog = false;
	}
	
	@Override
	public void mouseClicked(MouseEvent evt) {
		trainFace();
	}

	@Override
	public void mousePressed(MouseEvent evt) {
	}
	
	@Override
	public void mouseReleased(MouseEvent evt) {
	}
	
	@Override
	public void mouseEntered(MouseEvent evt) {
	}
	
	@Override
	public void mouseExited(MouseEvent evt) {
	}
}