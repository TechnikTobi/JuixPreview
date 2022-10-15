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

    private enum EFitMode {
        withResize,
        withScroll
    }

    private IWindow parent;
    private BufferedImage currentImage;
    private int zoomFactor;
    private EFitMode mode;
    private boolean hasDrawn;

    public JuixImageView(IWindow parent)
    {
        // super(new GridBagLayout());
        this.parent = parent;
        this.zoomFactor = 1;
        this.hasDrawn = false;

        this.setBackground(new Color(0, 0, 0));
        this.setLocation(0, 0);
    }

    public void zoomIn() {
        if (this.zoomFactor < 8)
        {
            this.zoomFactor = (int) Math.round(this.zoomFactor * 2.0);
        }
        this.repaint();
    }

    public void zoomOut() {
        if (this.zoomFactor > 1)
        {
            this.zoomFactor = (int) Math.round(this.zoomFactor / 2.0);
        }

        this.repaint();
    }


    @Override
    public void update(IMessage message) {
        if (this.parent.getFileManager().current() == null) return;

        try {
            this.currentImage = ImageIO.read(this.parent.getFileManager().current().getFile());
            this.mode = EFitMode.withResize;
            this.zoomFactor = 1;
            this.hasDrawn = false;
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.currentImage == null) return;

        this.mode = this.zoomFactor == 1 ? EFitMode.withResize : EFitMode.withScroll;

        BufferedImage resizedImage = this.getResizedImageToFit();


        g.drawImage(
                resizedImage,
                Math.max(0, (this.getWidth() - resizedImage.getWidth())/2),
                Math.max(0, (this.getHeight() - resizedImage.getHeight())/2),
                this
        );


        if (this.getWidth() < resizedImage.getWidth() || this.getHeight() < resizedImage.getHeight()) {
            // this.setSize(new Dimension(resizedImage.getWidth(), resizedImage.getHeight())); // this.setSize(this.currentSize);
        }

        this.hasDrawn = true;

    }

    private BufferedImage getResizedImageToFit()
    {
        int resizeWidth = 0;
        int resizeHeight = 0;

        if (this.mode == EFitMode.withResize)
        {
            int imageWidth = this.currentImage.getWidth();
            int imageHeight = this.currentImage.getHeight();
            double imageAspectRatio = (double) imageWidth / (double) imageHeight;

            int panelWidth = this.parent.getFrame().getWidth();
            int panelHeight = this.parent.getFrame().getHeight();

            System.out.println("Panel:");
            System.out.println(panelWidth);
            System.out.println(panelHeight);

            double panelAspectRatio = (double) panelWidth / (double) panelHeight;



            if (panelAspectRatio > imageAspectRatio)
            {
                // Panel is too wide for image
                resizeWidth = (int) (imageAspectRatio * panelHeight);
                resizeHeight = panelHeight;
            }
            else
            {
                // Panel is too narrow for image
                resizeWidth = panelWidth;
                resizeHeight = (int) (1/imageAspectRatio * panelWidth);
            }
        }
        else
        {
            resizeWidth = (int) (this.currentImage.getWidth() * this.zoomFactor);
            resizeHeight = (int) (this.currentImage.getHeight() * this.zoomFactor);
        }

        System.out.println("Resize!");
        System.out.println(resizeWidth);
        System.out.println(resizeHeight);

        Image temporaryImage = this.currentImage.getScaledInstance(
                resizeWidth,
                resizeHeight,
                Image.SCALE_FAST // Image.SCALE_SMOOTH
        );

        BufferedImage returnValue = new BufferedImage(resizeWidth, resizeHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D2d = returnValue.createGraphics();
        graphics2D2d.drawImage(temporaryImage, 0, 0, null);
        graphics2D2d.dispose();

        return returnValue;

    }

    @Override
    public Dimension getPreferredSize() {
        if (this.hasDrawn)
        {
            if (this.mode == EFitMode.withScroll)
            {
                return new Dimension(
                        (int) (zoomFactor * this.currentImage.getWidth()),
                        (int) (zoomFactor * this.currentImage.getHeight())
                );
            }
            else
            {
                return this.parent.getFrame().getSize();
            }
        }
        else
        {
            return new Dimension(400, 400);
        }
    }
}
