package juixPreview.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class JuixMenuBar {

    private IWindow window;
    private JMenuBar menuBar;

    public JuixMenuBar(IWindow window) {
        this.window = window;
        this.menuBar = new JMenuBar();

        // File section
        JMenu file = new JMenu("File");
        {
            JMenuItem file_open = new JMenuItem("Open...");
            JMenuItem file_previous = new JMenuItem("Previous");
            JMenuItem file_next = new JMenuItem("Next");

            file_open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser file_chooser = new JFileChooser();

                    file_chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                    file_chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    file_chooser.setMultiSelectionEnabled(true);

                    int result = file_chooser.showOpenDialog(window.getFrame());
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = file_chooser.getSelectedFile();
                        window.getParent().createWindow(selectedFile);
                    }
                }
            });

            file_previous.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(window.getFileManager().previousFile().getFile().getAbsolutePath());
                    file_previous.setEnabled(window.getFileManager().hasPrevious());
                    file_next.setEnabled(window.getFileManager().hasNext());
                }
            });
            file_next.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(window.getFileManager().nextFile().getFile().getAbsolutePath());
                    file_previous.setEnabled(window.getFileManager().hasPrevious());
                    file_next.setEnabled(window.getFileManager().hasNext());
                }
            });

            file_open.setAccelerator(KeyStroke.getKeyStroke('O',
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
            ));

            file_previous.setAccelerator(KeyStroke.getKeyStroke("LEFT"));
            file_next.setAccelerator(KeyStroke.getKeyStroke("RIGHT"));

            file.add(file_open);
            file.add(new JSeparator());
            file.add(file_previous);
            file.add(file_next);
        }

        // Edit section
        JMenu edit = new JMenu("Edit");


        // View section
        JMenu view = new JMenu("View");
        {
            JMenuItem view_zoom_in = new JMenuItem("Zoom in");
            JMenuItem view_zoom_out = new JMenuItem("Zoom out");

            view_zoom_in.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    window.getImageView().zoomIn();
                }
            });

            view_zoom_out.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    window.getImageView().zoomOut();
                }
            });


            view_zoom_in.setAccelerator(KeyStroke.getKeyStroke(521, // '+'
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
            ));

            view_zoom_out.setAccelerator(KeyStroke.getKeyStroke(45, // '-'
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
            ));

            view.add(view_zoom_in);
            view.add(view_zoom_out);
        }

        // Adding all sections to the menu bar
        this.menuBar.add(file);
        this.menuBar.add(edit);
        this.menuBar.add(view);
    }

    public JComponent getJComponent() {
        return this.menuBar;
    }



}
