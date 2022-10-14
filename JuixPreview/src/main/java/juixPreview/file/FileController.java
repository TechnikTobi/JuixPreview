package juixPreview.file;

import java.io.File;

public class FileController {

    private File file;

    public FileController(File file)
    {
        if (file.isDirectory())
        {
            throw new RuntimeException("Can't control a directory - only files");
        }

        this.file = file;
    }

    public File getFile()
    {
        return this.file;
    }

}
