package juixPreview.main;

public class OSdetection {

    private static String getOSstring()
    {
        return System.getProperty("os.name").toLowerCase();
    }

    public static boolean isWindows()
    {
        return getOSstring().contains("win");
    }

    public static boolean isMac()
    {
        return getOSstring().contains("mac");
    }

    public static boolean isSolaris()
    {
        return getOSstring().contains("sunos");
    }

    public static boolean isOtherUnix()
    {
        return (
            getOSstring().contains("nix") ||
            getOSstring().contains("nux") ||
            getOSstring().contains("aix")
        );
    }

}
