package juixPreview.main;

public class SystemSetup {

    public static void setup()
    {

        if (OSdetection.isMac())
        {
            setupMac();
        }
        else if (OSdetection.isWindows())
        {
            setupWindows();
        }

    }

    private static void setupMac()
    {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // System.setProperty("apple.awt.application.appearance", "system");
        System.setProperty("apple.awt.application.name", JuixPreview.APPLICATION_STRING );
    }

    private static void setupWindows()
    {

    }

}
