package juixPreview.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class JuixMenuBar {

    private IWindow window;
    private JMenuBar menuBar;

    public JuixMenuBar(IWindow window)
    {
        this.window = window;
        this.menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem file_open = new JMenuItem("Open...");
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
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                }
            }
        });

        file_open.setAccelerator(KeyStroke.getKeyStroke(
            'O',
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
        ));
        file.add(file_open);

        JMenu edit = new JMenu("Edit");

        this.menuBar.add(file);
        this.menuBar.add(edit);
    }

    public JComponent getJComponent() {
        return this.menuBar;
    }



}
