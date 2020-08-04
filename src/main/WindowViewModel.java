package main;

import javax.swing.*;
import java.awt.*;

public class WindowViewModel {
    private static int shapeSize = 100;
    private static Color currentSkeletonColor = Color.ORANGE;
    private static Color currentFigureColor = Color.GREEN;
    private static boolean switchSkeleton = false;
    private static boolean switchShades = false;
    private static boolean Pyramid = true;
    private static boolean Sphere = false;
    private static boolean Octaedr = false;
    private static boolean Cube = false;


    public WindowViewModel() {


    }

    public void exit() {
        if (JOptionPane.showConfirmDialog(null, "Do u want to exit?", "Exit", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    public Color changeColor(String color) {
        Color currentColor = null;
        switch(color) {
            case "RED": currentColor = Color.RED; break;
            case "WHITE": currentColor = Color.WHITE; break;
            case "BLUE": currentColor = Color.BLUE; break;
            case "BLACK": currentColor = Color.BLACK; break;
            case "GREEN": currentColor = Color.GREEN; break;
            case "YELLOW": currentColor = Color.YELLOW; break;
            case "PINK": currentColor = Color.PINK; break;
            case "CYAN": currentColor = Color.CYAN; break;
            case "ORANGE": currentColor = Color.ORANGE; break;
            case "MAGENTA": currentColor = Color.MAGENTA; break;
            case "LIGHT_GRAY": currentColor = Color.LIGHT_GRAY; break;
            default: JOptionPane.showMessageDialog(null, "Undefined color");
        }
        return currentColor;
    }

}
