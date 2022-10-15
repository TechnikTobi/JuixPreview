package juixPreview.gui;

import juixPreview.file.FileManager;
import juixPreview.main.JuixPreview;
import juixPreview.observer.IMessage;
import juixPreview.observer.IObserver;
import juixPreview.observer.ISubject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JuixWindow implements IWindow, IObserver {

    private WindowManager parent;
    private List<IWindowComponent> components;
    private FileManager fileManager;

    private JFrame frame;
    private JuixMenuBar menuBar;

    private JuixImageView imageView;

    public JuixWindow(
            WindowManager parent,
            FileManager fileManager
    )
    {
        this.parent = parent;
        this.components = new ArrayList<>();
        this.fileManager = fileManager;

        this.frame = new JFrame();
        this.frame.setLayout(new BorderLayout());

        this.menuBar = new JuixMenuBar(this);
        this.frame.setJMenuBar((JMenuBar) this.menuBar.getJComponent());

        this.imageView = new JuixImageView(this);

        JScrollPane imageViewScroll = new JScrollPane(this.imageView);
        this.frame.setContentPane(imageViewScroll);

        this.fileManager.registerObserver(this);
        this.fileManager.registerObserver(this.imageView);
        this.fileManager.notifyObservers(null);

        this.frame.setMinimumSize(new Dimension(400, 400));
        this.frame.pack();
        this.frame.setVisible(true);

    }

    public JFrame getFrame()
    {
        return this.frame;
    }

    public WindowManager getParent()
    {
        return this.parent;
    }

    public FileManager getFileManager()
    {
        return this.fileManager;
    }

    public JuixImageView getImageView()
    {
        return this.imageView;
    }


    @Override
    public void update(IMessage message) {
        this.frame.setTitle(
            this.fileManager.current() != null ?
                    this.fileManager.current().getFile().getName() : JuixPreview.APPLICATION_STRING
        );
    }

}
