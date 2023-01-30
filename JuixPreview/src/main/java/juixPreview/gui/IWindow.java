package juixPreview.gui;

import juixPreview.file.FileManager;

import javax.swing.*;

public interface IWindow {

    public JFrame getFrame();
    public WindowManager getParent();
    public FileManager getFileManager();
    public JuixImageView getImageView();

}
