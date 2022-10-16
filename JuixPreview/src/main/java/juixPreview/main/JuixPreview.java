package juixPreview.main;

import juixPreview.file.FileManager;
import juixPreview.gui.JuixWindow;
import juixPreview.gui.WindowManager;

public class JuixPreview
{

    public static final String APPLICATION_STRING = "JuixPreview";
    private static WindowManager windowManager;

    public static void main(String[] args)
    {
        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        SystemSetup.setup();

        JuixPreview.windowManager = new WindowManager();
        windowManager.addWindow(new JuixWindow(windowManager, new FileManager()));

        System.out.println("Hello World!");
    }

}
