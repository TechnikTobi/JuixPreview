package juixPreview.gui;

import juixPreview.observer.IMessage;
import juixPreview.observer.IObserver;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

public class JuixImageView extends JPanel implements IWindowComponent, IObserver {

    private IWindow parent;
    private BufferedImage currentImage;
    private Dimension currentSize;
    private double zoomFactor;

    public JuixImageView(IWindow parent)
    {
        // super(new GridBagLayout());
        this.parent = parent;
        this.zoomFactor = 1.0;
        this.currentSize = new Dimension(0, 0);

        this.setBackground(new Color(0, 0, 0));
        this.setLocation(0, 0);
    }

    public void zoomIn() {
        if (this.zoomFactor < 8)
        {
            this.zoomFactor = Math.round(this.zoomFactor * 2.0);
        }
        this.repaint();
    }

    public void zoomOut() {
        if (this.zoomFactor > 1)
        {
            this.zoomFactor = Math.round(this.zoomFactor / 2.0);
        }
        this.repaint();
    }


    @Override
    public void update(IMessage message) {
        if (this.parent.getFileManager().current() == null) return;

        try {
            this.currentImage = ImageIO.read(this.parent.getFileManager().current().getFile());
            this.currentSize = new Dimension(this.currentImage.getWidth(), this.currentImage.getHeight());
            this.zoomFactor = 1.0;
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.currentImage == null) return;

        BufferedImage resizedImage = this.getResizedImageToFit();
        this.currentSize = new Dimension(resizedImage.getWidth(), resizedImage.getHeight());

        g.drawImage(
                resizedImage,
                Math.max(0, (this.getWidth() - resizedImage.getWidth())/2),
                Math.max(0, (this.getHeight() - resizedImage.getHeight())/2),
                this
        );


        if (this.getWidth() < resizedImage.getWidth() || this.getHeight() < resizedImage.getHeight()) {
            // this.setSize(new Dimension(resizedImage.getWidth(), resizedImage.getHeight())); // this.setSize(this.currentSize);
        }

    }

    private BufferedImage getResizedImageToFit()
    {
        int imageWidth = this.currentImage.getWidth();
        int imageHeight = this.currentImage.getHeight();
        double imageAspectRatio = (double) imageWidth / (double) imageHeight;

        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        double panelAspectRatio = (double) panelWidth / (double) panelHeight;

        int resizeWidth = this.getWidth();
        int resizeHeight = this.getHeight();

        if (panelAspectRatio > imageAspectRatio)
        {
            // Panel is too wide for image
            resizeWidth = (int) (imageAspectRatio * panelHeight);
        }
        else
        {
            // Panel is too narrow for image
            resizeHeight = (int) (1/imageAspectRatio * panelWidth);
        }

        resizeWidth *= this.zoomFactor;
        resizeHeight *= this.zoomFactor;

        Image temporaryImage = this.currentImage.getScaledInstance(
                resizeWidth,
                resizeHeight,
                Image.SCALE_SMOOTH // Image.SCALE_FAST // Image.SCALE_SMOOTH
        );

        BufferedImage returnValue = new BufferedImage(resizeWidth, resizeHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D2d = returnValue.createGraphics();
        graphics2D2d.drawImage(temporaryImage, 0, 0, null);
        graphics2D2d.dispose();

        return returnValue;
    }

    @Override
    public Dimension getPreferredSize() {
        if (this.currentImage != null)
        {
            return new Dimension(
                    (int) (zoomFactor * this.currentImage.getWidth()),
                    (int) (zoomFactor * this.currentImage.getHeight())
            );
        }
        else
        {
            return new Dimension(0, 0);
        }
    }
}
