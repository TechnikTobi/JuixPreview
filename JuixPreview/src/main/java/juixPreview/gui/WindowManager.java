package juixPreview.gui;

import juixPreview.file.FileManager;
import juixPreview.main.JuixPreview;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WindowManager
{

    private List<IWindow> windows;

    public WindowManager()
    {
        this.windows = new ArrayList<>();
    }

    public void addWindow(IWindow window)
    {
        this.windows.add(window);
    }

    public void newWindow()
    {

    }

    public void createWindow(File file)
    {
        this.addWindow(new JuixWindow(
                this,
                new FileManager(file),
                JuixPreview.APPLICATION_STRING
        ));
        this.clearWindows();
    }

    public void clearWindows()
    {
        while (true) {
            IWindow illegalWindow = this.windows.stream().filter(w -> w.getFileManager().isIllegal()).findFirst().orElse(null);
            if (illegalWindow == null)
            {
                break;
            }
            this.windows.remove(illegalWindow);
        }
    }

    public void printActiveStatus()
    {
        System.out.println("Status:");
        this.windows.stream().forEach(w -> System.out.println(w.getFrame().getTitle() + String.valueOf(w.getFrame().isActive())));
    }



}
