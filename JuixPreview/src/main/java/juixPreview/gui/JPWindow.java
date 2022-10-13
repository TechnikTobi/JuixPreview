package juixPreview.gui;

import javax.swing.*;

public class JPWindow
{
    private JFrame frame;

    public JPWindow(
            String title
    )
    {
        this.frame = new JFrame(title);
        this.frame.setSize(400, 400);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
    }

}
