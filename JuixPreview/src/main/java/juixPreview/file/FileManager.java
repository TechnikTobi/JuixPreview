package juixPreview.file;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FileManager {

    private File directory;
    private List<FileController> controllers;
    private FileController currentFileController;

    public FileManager(File file)
    {
        this.setFiles(
            Stream.of(
                    file.isDirectory() ? file.listFiles() : file.getParentFile().listFiles()
            ).toList()
        );
    }

    public FileManager(List<File> files)
    {
        this.setFiles(files);
    }

    private void setFiles(List<File> files)
    {
        this.controllers = files.stream()
            .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) {
                        return isAllowedFileType(file.getName());
                    }
                }
            )
            .map(f -> new FileController(f))
            .sorted(new Comparator<FileController>() {
                    @Override
                    public int compare(FileController o1, FileController o2) {
                        return o1.getFile().getName().compareTo(o2.getFile().getName());
                    }
                }
            )
            .toList();
        this.currentFileController = this.controllers.stream().findFirst().orElse(null);
        this.directory = this.currentFileController.getFile().getParentFile();
    }

    public FileController current()
    {
        return this.currentFileController;
    }

    public FileController nextFile()
    {
        int currentIndex = this.controllers.indexOf(this.currentFileController);
        if (currentIndex < this.controllers.size()-1)
        {
            this.currentFileController = this.controllers.get(currentIndex + 1);
        }
        return this.currentFileController;
    }

    public FileController previousFile()
    {
        int currentIndex = this.controllers.indexOf(this.currentFileController);
        if (currentIndex > 0)
        {
            this.currentFileController = this.controllers.get(currentIndex - 1);
        }
        return this.currentFileController;
    }

    private static boolean isAllowedFileType(String filename)
    {
        String extension = Arrays.stream(filename.split(".")).reduce((first, second) -> second).orElse(null);
        if (extension == null)
        {
            return false;
        }
        switch(extension.toLowerCase())
        {
            case "png":
            case "jpg":
            case "jpeg": return true;
            default: return false;
        }
    }

}
