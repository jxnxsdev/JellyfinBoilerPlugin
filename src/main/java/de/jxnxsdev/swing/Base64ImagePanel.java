package de.jxnxsdev.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

public class Base64ImagePanel extends JPanel {

    private BufferedImage image;

    // Constructor to initialize the panel with a Base64 image string
    public Base64ImagePanel(String base64Image) {
        setImage(base64Image); // Set the initial image
    }

    /**
     * Method to set or replace the image using a Base64 string
     * @param base64Image Base64-encoded image string
     */
    public void setImage(String base64Image) {
        try {
            // Decode the Base64 string
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Convert byte array to BufferedImage
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            this.image = ImageIO.read(bis);
            bis.close();

            // Repaint the panel to show the new image
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load image", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the image if it's loaded
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
