package main;

import Shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelListener;

public class WindowView extends JFrame {

    JSlider horizontalSlider;
    JSlider verticalSlider;
    private Container pane;
    private Scene scene;
    private JTextField textSkeletonColor;
    private JTextField textFigureColor;

    public WindowView(WindowViewModel viewModel) {

        pane = getContentPane();
        pane.setLayout(new BorderLayout());

        horizontalSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 360, 150);
        verticalSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 30);

        scene = new Scene(this);
        scene.addShape(new Pyramid());

        MouseWheelListener mwl = e -> {
            scene.getShape().resize(e.getPreciseWheelRotation() * 10);
            scene.repaint();
        };

        horizontalSlider.addChangeListener(e -> scene.repaint());
        verticalSlider.addChangeListener(e -> scene.repaint());

        pane.add(horizontalSlider, BorderLayout.SOUTH);
        pane.add(verticalSlider, BorderLayout.EAST);
        pane.add(scene, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        JMenu menuInfo = new JMenu("Info");

        JMenuItem ItemExit = new JMenuItem("Exit");
        ItemExit.addActionListener(action -> viewModel.exit());

        JMenu ItemColor = new JMenu("Change the color");

        JMenu ItemChangeFigColor = new JMenu("Change figure's color");
        ItemColor.add(ItemChangeFigColor);

        textFigureColor = new JTextField();
        textFigureColor.addActionListener(actionEvent -> {
            Color color = viewModel.changeColor(textFigureColor.getText().toUpperCase());
            scene.getShape().setColor(color == null ? scene.getShape().getColor() : color);
            scene.repaint();
        });
        textFigureColor.setText("white");
        ItemChangeFigColor.add(textFigureColor);
        textFigureColor.setColumns(10);

        JMenu ItemChangeSkColor = new JMenu("Change skeleton's color");
        ItemColor.add(ItemChangeSkColor);

        textSkeletonColor = new JTextField();
        textSkeletonColor.addActionListener(actionEvent -> {
            Color color = viewModel.changeColor(textSkeletonColor.getText().toUpperCase());
            scene.getShape().setSkeletonColor(color == null ? scene.getShape().getSkeletonColor() : color);
            scene.repaint();
        });
        textSkeletonColor.setText("white");
        ItemChangeSkColor.add(textSkeletonColor);
        textSkeletonColor.setColumns(10);

        JCheckBoxMenuItem ItemChkSkeleton = new JCheckBoxMenuItem("Switch skeleton");
        ItemChkSkeleton.addActionListener(actionEvent -> {
            scene.switchSkeleton = !scene.switchSkeleton;
            scene.repaint();
        });

        JMenu ItemNewObject = new JMenu("Change figure");

        JMenuItem ItemPyramid = new JMenuItem("Pyramid");
        ItemPyramid.addActionListener(actionEvent -> scene.addShape(new Pyramid()).repaint());

        JMenuItem ItemSphere = new JMenuItem("Sphere");
        ItemSphere.addActionListener(actionEvent -> scene.addShape(new Sphere()).repaint());

        JMenuItem ItemCube = new JMenuItem("Cube");
        ItemCube.addActionListener(actionEvent -> scene.addShape(new Cube()).repaint());

        ItemNewObject.add(ItemPyramid);
        ItemNewObject.add(ItemSphere);
        ItemNewObject.add(ItemCube);

        JCheckBoxMenuItem ItemChkShades = new JCheckBoxMenuItem("Switch shades");
        ItemChkShades.addActionListener(actionEvent -> {
            scene.switchShades = !scene.switchShades;
            scene.repaint();
        });

        JTextPane textPaneInfo = new JTextPane();
        textPaneInfo.setEditable(false);
        textPaneInfo.setText("Available colors:\nRED; WHITE; BLUE;  BLACK;  GREEN;  YELLOW; \nPINK; CYAN; MAGENTA; LIGHT_GRAY; ORANGE;\n\n\u0421ase insensitive");

        JTextPane textAuthor = new JTextPane();
        textAuthor.setEditable(false);
        textAuthor.setText("Author: Vladimir");

        menu.add(ItemColor);
        menu.add(ItemNewObject);
        menu.add(ItemChkSkeleton);
        menu.add(ItemChkShades);
        menu.add(new JSeparator());
        menu.add(ItemExit);

        menuInfo.add(textPaneInfo);
        menuInfo.add(new JSeparator());
        menuInfo.add(textAuthor);

        menuBar.add(menu);
        menuBar.add(menuInfo);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setFocusable(true);
        setTitle("Render3D");
        setLocationRelativeTo(null);
        addMouseWheelListener(mwl);
        setJMenuBar(menuBar);

        setVisible(true);
    }
}
