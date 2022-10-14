package juixPreview.gui;

import javax.swing.*;

public interface IWindow {

    public void addComponent(IWindowComponent component);

    public JFrame getFrame();
    public WindowManager getParent();

}
