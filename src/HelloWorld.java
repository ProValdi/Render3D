import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class HelloWorld {

    public static void main(String... args) {

        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        // slider to control horizontal rotation
        JSlider horizontalSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 360, 180);
        pane.add(horizontalSlider, BorderLayout.SOUTH);

        // slider to control vertical rotation
        JSlider verticalSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(verticalSlider, BorderLayout.EAST);

        // panel to display render results
        JPanel renderPanel = new JPanel() {
			public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                repaint();
        
                // rendering magic will happen here
            }
        };

        pane.add(renderPanel, BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setVisible(true);
        
    }
}

