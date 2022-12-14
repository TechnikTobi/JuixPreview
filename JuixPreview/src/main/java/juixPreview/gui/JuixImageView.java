package juixPreview.gui;

import juixPreview.observer.IMessage;
import juixPreview.observer.IObserver;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.io.IOException;


public class JuixImageView extends JPanel implements IWindowComponent, IObserver {

    private enum EFitMode {
        withResize,
        withScroll
    }

    private IWindow parent;
    private BufferedImage currentImage;
    private int zoomFactor;
    private EFitMode mode;

    private JLabel imageViewLabel;
    private JScrollPane imageViewScrollPane;

    public JuixImageView(IWindow parent)
    {
        // super(new GridBagLayout());
        this.parent = parent;
        this.zoomFactor = 1;

        this.setBackground(new Color(0, 0, 0));
        this.setLayout(new BorderLayout());

        this.imageViewLabel = new JLabel((ImageIcon) null, JLabel.CENTER);
        this.imageViewLabel.setBackground(new Color(0, 0, 0));
        this.imageViewLabel.setMinimumSize(new Dimension(300, 200));
        this.imageViewLabel.setOpaque(true);
        this.imageViewLabel.setAutoscrolls(true);;

        this.imageViewScrollPane = new JScrollPane(this.imageViewLabel);
        this.imageViewScrollPane.setBackground(new Color(0, 0, 0));
        this.imageViewScrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(imageViewScrollPane);

        MouseAdapter mouseAdapter = new MouseAdapter() {

            private Point origin;

            @Override
            public void mousePressed(MouseEvent e)
            {
                this.origin = new Point(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (this.origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, imageViewLabel);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX;
                        view.y += deltaY;

                        imageViewLabel.scrollRectToVisible(view);
                    }
                }
            }
        };

        this.imageViewLabel.addMouseListener(mouseAdapter);
        this.imageViewLabel.addMouseMotionListener(mouseAdapter);

    }

    public void zoomIn() {
        if (this.zoomFactor < 8) this.zoomFactor = (int) Math.round(this.zoomFactor * 2.0);
        this.repaint();
    }

    public void zoomOut() {
        if (this.zoomFactor > 1) this.zoomFactor = (int) Math.round(this.zoomFactor / 2.0);
        this.repaint();
    }






    @Override
    public void update(IMessage message) {
        if (this.parent.getFileManager().current() == null) return;

        try {
            this.currentImage = ImageIO.read(this.parent.getFileManager().current().getFile());
            this.imageViewLabel.setIcon(new ImageIcon(this.currentImage));
            this.mode = EFitMode.withResize;
            this.zoomFactor = 1;
            this.draw();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void draw()
    {
        if (this.currentImage == null) return;

        this.mode = EFitMode.withResize;
        //this.imageViewLabel.setBorder(BorderFactory.createLineBorder(Color.red));
        this.imageViewLabel.setIcon(new ImageIcon(this.getResizedImageToFit()));
    }

    @Override
    public void repaint()
    {
        super.repaint();
        this.draw();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.currentImage == null) return;
         // this.mode = this.zoomFactor == 1 ? EFitMode.withResize : EFitMode.withScroll;
        // this.mode = EFitMode.withResize;

        // BufferedImage resizedImage = this.getResizedImageToFit();

        /*
        g.drawImage(
                resizedImage,
                Math.max(0, (this.getWidth() - resizedImage.getWidth())/2),
                Math.max(0, (this.getHeight() - resizedImage.getHeight())/2),
                this
        );

        if (this.getWidth() < resizedImage.getWidth() || this.getHeight() < resizedImage.getHeight()) {
            // this.setSize(new Dimension(resizedImage.getWidth(), resizedImage.getHeight())); // this.setSize(this.currentSize);
        }
        */
    }

    private Image getResizedImageToFit()
    {
        int resizeWidth = 0;
        int resizeHeight = 0;

        System.out.println(this.parent.getFrame().getSize());
        System.out.println(this.imageViewLabel.getSize());

        if (this.mode == EFitMode.withResize)
        {
            int imageWidth = this.currentImage.getWidth();
            int imageHeight = this.currentImage.getHeight();
            double imageAspectRatio = (double) imageWidth / (double) imageHeight;

            int panelWidth = Math.max(10, this.getWidth());
            int panelHeight = Math.max(10, this.getHeight());

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

        return this.currentImage.getScaledInstance(resizeWidth, resizeHeight,  Image.SCALE_FAST);

        /*
        try
        {
            return Thumbnails.of(this.currentImage).size(resizeWidth, resizeHeight).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

         */
    }

    @Override
    public Dimension getPreferredSize() {

        return super.getPreferredSize();

        /*
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

         */


        // return super.getPreferredSize();

/*


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
 */

    }
}
