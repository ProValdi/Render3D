package com.company;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;


public class Render3D {

	private static int size = 100;
	private static Color currentSkeletonColor = Color.ORANGE;
	private static Color currentFigureColor = Color.GREEN;
	private static boolean switchSkeleton = false;
	private static boolean switchShades = false;
	private static boolean Pyramid = true;
	private static boolean Sphere = false;
	private static boolean Octaedr = false;
	private static boolean Cube = false;
	
	private static JTextField textSkeletonColor;
	private static JTextField textFigureColor;
	
	public static void main(String... args) {

		JFrame frame = new JFrame();
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());

		JSlider horizontalSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 360, 150);
		pane.add(horizontalSlider, BorderLayout.SOUTH);

		JSlider verticalSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 30);
		pane.add(verticalSlider, BorderLayout.EAST);

		JPanel renderPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.GRAY);
				g2.fillRect(0, 0, getWidth(), getHeight());

				List<Triangle> tris = new ArrayList<>();

				if(!Cube) {
					tris.add(new Triangle(new Vertex(size, size, size),
							new Vertex(-size, -size, size),
							new Vertex(-size, size, -size),
							currentFigureColor));

					tris.add(new Triangle(new Vertex(size, size, size),
							new Vertex(-size, -size, size),
							new Vertex(size, -size, -size),
							currentFigureColor));

					tris.add(new Triangle(new Vertex(-size, size, -size),
							new Vertex(size, -size, -size),
							new Vertex(size, size, size),
							currentFigureColor));

					tris.add(new Triangle(new Vertex(-size, size, -size),
							new Vertex(size, -size, -size),
							new Vertex(-size, -size, size),
							currentFigureColor));	
				}

				if(Cube) {
				//A
				tris.add(new Triangle(new Vertex(-size, size, size),
						new Vertex(size, size, size),
						new Vertex(-size, size, -size),
						currentFigureColor));
				//B
				tris.add(new Triangle(new Vertex(size, size, size),
						new Vertex(size, size, -size),
						new Vertex(-size, size, -size),
						currentFigureColor));
				//C
				tris.add(new Triangle(new Vertex(size, -size, size),
						new Vertex(size, size, -size),
						new Vertex(size, size, size),
						currentFigureColor));
				//D
				tris.add(new Triangle(new Vertex(size, -size, size),
						new Vertex(size, -size, -size),
						new Vertex(size, size, -size),
						currentFigureColor));
				//E
				tris.add(new Triangle(new Vertex(-size, -size, size),
						new Vertex(size, -size, size),
						new Vertex(-size, size, size),
						currentFigureColor));

				//F
				tris.add(new Triangle(new Vertex(size, -size, size),
						new Vertex(size, size, size),
						new Vertex(-size, size, size),
						currentFigureColor));
				//G
				tris.add(new Triangle(new Vertex(-size, -size, size),
						new Vertex(-size, size, size),
						new Vertex(-size, -size, -size),
						currentFigureColor));
				//H
				tris.add(new Triangle(new Vertex(-size, size, size),
						new Vertex(-size, size, -size),
						new Vertex(-size, -size, -size),
						currentFigureColor));
				//I
				tris.add(new Triangle(new Vertex(-size, size, -size),
						new Vertex(size, size, -size),
						new Vertex(-size, -size, -size),
						currentFigureColor));
				//J
				tris.add(new Triangle(new Vertex(-size, -size, -size),
						new Vertex(size, size, -size),
						new Vertex(size, -size, -size),
						currentFigureColor));
				//K
				tris.add(new Triangle(new Vertex(size, -size, size),
						new Vertex(-size, -size, size),
						new Vertex(-size, -size, -size),
						currentFigureColor));
				//L
				tris.add(new Triangle(new Vertex(-size, -size, -size),
						new Vertex(size, -size, -size),
						new Vertex(size, -size, size),
						currentFigureColor));
				}

				double horizontal = Math.toRadians(horizontalSlider.getValue());

				// rotate XZ across Y
				Matrix3 horizontalTransform = new Matrix3(new double[] {Math.cos(horizontal), 0, Math.sin(horizontal),
						0, 1, 0, 
						-Math.sin(horizontal), 0, Math.cos(horizontal)}); 
				double vertical = Math.toRadians(verticalSlider.getValue()); 

				// rotate YZ across X
				Matrix3 verticalTransform = new Matrix3(new double[] {1, 0, 0, 
						0, Math.cos(vertical), Math.sin(vertical), 
						0, -Math.sin(vertical), Math.cos(vertical)}); 

				Matrix3 transform = horizontalTransform.multiply(verticalTransform);

				if(switchSkeleton) {
					g2.translate(getWidth() / 2, getHeight() / 2);
					g2.setColor(currentSkeletonColor);
				}

				BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

				double[] zBuffer = new double[img.getWidth() * img.getHeight()];

				// initialize array with extremely far away depths
				for (int q = 0; q < zBuffer.length; q++) {
				    zBuffer[q] = Double.NEGATIVE_INFINITY;
				}
				
				for (Triangle t : Sphere? inflate(tris, 6) : Octaedr? inflate(tris, 1) : tris) {
					Vertex v1 = transform.transform(t.v1);
					Vertex v2 = transform.transform(t.v2);
					Vertex v3 = transform.transform(t.v3);
					
					if(switchSkeleton) {
					    Path2D path = new Path2D.Double();
					    path.moveTo(v1.x, v1.y);
					    path.lineTo(v2.x, v2.y);
					    path.lineTo(v3.x, v3.y);
					    path.closePath();
					    g2.draw(path);
					} else {
						
	                    Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
	                    Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
	                    Vertex norm = new Vertex(
	                                             ab.y * ac.z - ab.z * ac.y,
	                                             ab.z * ac.x - ab.x * ac.z,
	                                             ab.x * ac.y - ab.y * ac.x
	                                             );
	                    double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
	                    norm.x /= normalLength;
	                    norm.y /= normalLength;
	                    norm.z /= normalLength;
	
	                    double angleCos = Math.abs(norm.z);
						
						// since we are not using Graphics2D anymore, we have to do translation manually
						v1.x += getWidth() / 2;
						v1.y += getHeight() / 2;
						v2.x += getWidth() / 2;
						v2.y += getHeight() / 2;
						v3.x += getWidth() / 2;
						v3.y += getHeight() / 2;
	
						// compute rectangular bounds for triangle
						int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
						int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
						int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
						int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
	
						double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
						for (int y = minY; y <= maxY; y++) {
							for (int x = minX; x <= maxX; x++) {
								double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
								double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
								double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
								if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
								    double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
								    int zIndex = y * img.getWidth() + x;
								    if (zBuffer[zIndex] < depth) {
								    	if(!switchShades) {
								    		img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
								    	} else {
								    		img.setRGB(x, y, t.color.getRGB());
								    	}								 
								        zBuffer[zIndex] = depth;
								    }
								}
							}
						}
					}
				}
				
				//System.out.println(size);
				g2.drawImage(img, 0, 0, null);

			}
		};
		pane.add(renderPanel, BorderLayout.CENTER);
			
		MouseWheelListener mwl = new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				size += e.getPreciseWheelRotation() * 10;
				renderPanel.repaint();
			}
		};
		
		horizontalSlider.addChangeListener(e -> renderPanel.repaint()); 
		verticalSlider.addChangeListener(e -> renderPanel.repaint());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.setTitle("Render3D");
		frame.setLocationRelativeTo(null);
		frame.addMouseWheelListener(mwl);
		
		//Render3D obj = new Render3D(frame);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu Menu = new JMenu("New menu");
		menuBar.add(Menu);
		
		JMenu menuInfo = new JMenu("Info");
		menuBar.add(menuInfo);
		
		
		JMenuItem ItemExit = new JMenuItem("Exit");
		ItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				if (JOptionPane.showConfirmDialog(null, "Do u want to exit?", "Exit", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});
		
		JMenu ItemColor = new JMenu("Change the color");
		Menu.add(ItemColor);
		
		JMenu ItemChangeFigColor = new JMenu("Change figure's color");
		ItemColor.add(ItemChangeFigColor);
		
		textFigureColor = new JTextField();
		textFigureColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actE) {
				String color = textFigureColor.getText().toUpperCase();
				switch(color) {
					case "RED": currentFigureColor = Color.RED; break;
					case "WHITE": currentFigureColor = Color.WHITE; break;
					case "BLUE": currentFigureColor = Color.BLUE; break;
					case "BLACK": currentFigureColor = Color.BLACK; break;
					case "GREEN": currentFigureColor = Color.GREEN; break;
					case "YELLOW": currentFigureColor = Color.YELLOW; break;
					case "PINK": currentFigureColor = Color.PINK; break;
					case "CYAN": currentFigureColor = Color.CYAN; break;
					case "ORANGE": currentFigureColor = Color.ORANGE; break;
					case "MAGENTA": currentFigureColor = Color.MAGENTA; break;
					case "LIGHT_GRAY": currentFigureColor = Color.LIGHT_GRAY; break;
					default: JOptionPane.showMessageDialog(null, "Undefined color");
				}
				renderPanel.repaint();
			}
		});
		textFigureColor.setText("white");
		ItemChangeFigColor.add(textFigureColor);
		textFigureColor.setColumns(10);
		
		JMenu ItemChangeSkColor = new JMenu("Change skeleton's color");
		ItemColor.add(ItemChangeSkColor);
		
		textSkeletonColor = new JTextField();
		textSkeletonColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actE) {
				String color = textSkeletonColor.getText().toUpperCase();
				switch(color) {
					case "RED": currentSkeletonColor = Color.RED; break;
					case "WHITE": currentSkeletonColor = Color.WHITE; break;
					case "BLUE": currentSkeletonColor = Color.BLUE; break;
					case "BLACK": currentSkeletonColor = Color.BLACK; break;
					case "GREEN": currentSkeletonColor = Color.GREEN; break;
					case "YELLOW": currentSkeletonColor = Color.YELLOW; break;
					case "PINK": currentSkeletonColor = Color.PINK; break;
					case "CYAN": currentSkeletonColor = Color.CYAN; break;
					case "ORANGE": currentSkeletonColor = Color.ORANGE; break;
					case "MAGENTA": currentSkeletonColor = Color.MAGENTA; break;
					case "LIGHT_GRAY": currentSkeletonColor = Color.LIGHT_GRAY; break;
					default: JOptionPane.showMessageDialog(null, "Undefined color");
				}
				renderPanel.repaint();
			}
		});
		textSkeletonColor.setText("white");
		ItemChangeSkColor.add(textSkeletonColor);
		textSkeletonColor.setColumns(10);
		
		JCheckBoxMenuItem ItemChkSkeleton = new JCheckBoxMenuItem("Switch skeleton");
		ItemChkSkeleton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actE) {
				switchSkeleton = !switchSkeleton;
				renderPanel.repaint();
			}
		});
		
		JMenu ItemNewObject = new JMenu("Change figure");
		Menu.add(ItemNewObject);
		
		JMenuItem ItemPyramid = new JMenuItem("Pyramid");
		ItemPyramid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actE) {
				Pyramid = true;
				Sphere = false;
				Octaedr = false;
				Cube = false;
				renderPanel.repaint();
			}
		});
		ItemNewObject.add(ItemPyramid);
		
		JMenuItem ItemSphere = new JMenuItem("Sphere");
		ItemSphere.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actE) {
				Pyramid = false;
				Sphere = true;
				Octaedr = false;
				Cube = false;
				renderPanel.repaint();
			}
		});
		ItemNewObject.add(ItemSphere);
		
		JMenuItem ItemOcto = new JMenuItem("Octaedr");
		ItemOcto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actE) {
				Pyramid = false;
				Sphere = false;
				Octaedr = true;
				Cube = false;
				renderPanel.repaint();
			}
		});
		ItemNewObject.add(ItemOcto);
		
		JMenuItem ItemCube = new JMenuItem("Cube");
		ItemCube.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actE) {
				Pyramid = false;
				Sphere = false;
				Octaedr = false;
				Cube = true;
				renderPanel.repaint();
			}
		});
		ItemNewObject.add(ItemCube);
		
		Menu.add(ItemChkSkeleton);
	
		
		JCheckBoxMenuItem ItemChkShades = new JCheckBoxMenuItem("Switch shades");
		ItemChkShades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actE) {
				switchShades = !switchShades;
				renderPanel.repaint();
			}
		});
		Menu.add(ItemChkShades);
		
		JSeparator separator = new JSeparator();
		Menu.add(separator);
		Menu.add(ItemExit);
	
		
		JTextPane textPaneInfo = new JTextPane();
		textPaneInfo.setEditable(false);
		textPaneInfo.setText("Available colors:\nRED; WHITE; BLUE;  BLACK;  GREEN;  YELLOW; \nPINK; CYAN; MAGENTA; LIGHT_GRAY; ORANGE;\n\n\u0421ase insensitive");
		menuInfo.add(textPaneInfo);
		
		JSeparator separator_3 = new JSeparator();
		menuInfo.add(separator_3);
		
		JTextPane textAuthor = new JTextPane();
		textAuthor.setEditable(false);
		textAuthor.setText("Author: Vladimir");
		menuInfo.add(textAuthor);
		

	} 
	
	public static Color getShade(Color color, double shade) {
	    int red = (int) (color.getRed() * shade);
	    int green = (int) (color.getGreen() * shade);
	    int blue = (int) (color.getBlue() * shade);

	    return new Color(red, green, blue);
    }

	public static List<Triangle> inflate(List<Triangle> tris, int depth) {
		if(depth-- == 0) {
			return tris;
		}
		List<Triangle> result = new ArrayList<>();
		for (Triangle t : tris) {
			int divider = 16;	
			
			Vertex m1 = new Vertex((t.v1.x + t.v2.x)/divider, (t.v1.y + t.v2.y)/divider, (t.v1.z + t.v2.z)/divider);
			Vertex m2 = new Vertex((t.v2.x + t.v3.x)/divider, (t.v2.y + t.v3.y)/divider, (t.v2.z + t.v3.z)/divider);
			Vertex m3 = new Vertex((t.v1.x + t.v3.x)/divider, (t.v1.y + t.v3.y)/divider, (t.v1.z + t.v3.z)/divider);
			
			if(Octaedr) {
				Vertex n1 = new Vertex(-(t.v1.x + t.v2.x)/divider, -(t.v1.y + t.v2.y)/divider, -(t.v1.z + t.v2.z)/divider);
				Vertex n2 = new Vertex(-(t.v2.x + t.v3.x)/divider, -(t.v2.y + t.v3.y)/divider, -(t.v2.z + t.v3.z)/divider);
				Vertex n3 = new Vertex(-(t.v1.x + t.v3.x)/divider, -(t.v1.y + t.v3.y)/divider, -(t.v1.z + t.v3.z)/divider);
				
				result.add(new Triangle(m1, m2, m3, t.color));
				result.add(new Triangle(n1, n2, n3, t.color));
			} else {
				result.add(new Triangle(t.v1, m1, m3, t.color));
				result.add(new Triangle(t.v2, m1, m2, t.color));
				result.add(new Triangle(t.v3, m2, m3, t.color));
				result.add(new Triangle(m1, m2, m3, t.color));
			}

		}
		for (Triangle t : result) {
			for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 }) {
				double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / size;
				v.x /= l;
				v.y /= l;
				v.z /= l;
			}
		}
		return inflate(result, depth);
	}
	
}

class Vertex {
	double x;
	double y;
	double z;

	public Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}

class Triangle {
	Vertex v1;
	Vertex v2;
	Vertex v3;
	Color color;

	public Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
	}
}

class Matrix3 {
	double[] values;

	Matrix3(double[] values) {
		this.values = values;
	}

	Matrix3 multiply(Matrix3 other) {
		double[] result = new double[9];

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				for (int i = 0; i < 3; i++) {
					result[row * 3 + col] += this.values[row * 3 + i] * other.values[i * 3 + col];
				}
			}
		}
		return new Matrix3(result);
	}

	Vertex transform(Vertex in) {
		return new Vertex(in.x * values[0] + in.y * values[3] + in.z * values[6],
				in.x * values[1] + in.y * values[4] + in.z * values[7],
				in.x * values[2] + in.y * values[5] + in.z * values[8]);
	}
}
