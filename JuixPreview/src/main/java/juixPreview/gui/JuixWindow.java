package juixPreview.gui;

import juixPreview.main.JuixPreview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JuixWindow implements IWindow {

    private WindowManager parent;
    private List<IWindowComponent> components;

    private JFrame frame;
    private JuixMenuBar menuBar;

    public JuixWindow(
            WindowManager parent,
            String title
    )
    {
        this.parent = parent;
        this.components = new ArrayList<>();

        this.menuBar = new JuixMenuBar(this);

        /*
        this.menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem open_item = new JMenuItem("Open...");
        open_item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JuixPreview.test();
            }
        });
        menu.add(open_item);
        this.menuBar.add(menu);

         */

        this.frame = new JFrame(title);
        this.frame.setSize(400, 400);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        this.frame.setJMenuBar((JMenuBar) this.menuBar.getJComponent());

    }

    public JFrame getFrame()
    {
        return this.frame;
    }

    public WindowManager getParent()
    {
        return this.parent;
    }


    @Override
    public void addComponent(IWindowComponent component) {
        this.components.add(component);
    }


}
