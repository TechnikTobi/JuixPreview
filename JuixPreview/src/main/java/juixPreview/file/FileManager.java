package juixPreview.file;

import juixPreview.observer.IMessage;
import juixPreview.observer.IObserver;
import juixPreview.observer.ISubject;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FileManager implements ISubject {

    private File directory;
    private List<FileController> controllers;
    private FileController currentFileController;
    private List<IObserver> observers;

    public FileManager()
    {
        this.directory = null;
        this.controllers = null;
        this.currentFileController = null;
        this.observers = new ArrayList<>();
    }

    public FileManager(File file)
    {
        this();
        this.setFiles(
            Stream.of(
                    file.isDirectory() ? file.listFiles() : file.getParentFile().listFiles()
            ).toList()
        );
    }

    public FileManager(List<File> files)
    {
        this();
        this.setFiles(files);
    }

    public boolean isIllegal()
    {
        return (this.controllers == null);
    }

    private void setFiles(List<File> files)
    {
        this.controllers = files.stream()
            .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) {
                        return file.isFile();
                    }
            })
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

        System.out.println("Found files:");
        for (FileController controller: this.controllers)
        {
            System.out.println(controller.getFile().getAbsolutePath());
        }
        this.currentFileController = this.controllers.stream().findFirst().orElse(null);

        if (this.currentFileController != null)
        {
            this.directory = this.currentFileController.getFile().getParentFile();
        }
        else
        {
            if (files.size() > 0)
            {
                this.directory = files.get(0).getParentFile();
            }
            else
            {
                this.directory = null;
            }
        }

        System.out.println("Current file: " + this.currentFileController.getFile().getAbsolutePath());
        System.out.println("Directory: " + this.directory.getAbsolutePath());
        this.notifyObservers(null);
    }

    public FileController current()
    {
        return this.currentFileController;
    }

    public boolean hasPrevious()
    {
        if (currentFileController == null)
        {
            return false;
        }
        int currentIndex = this.controllers.indexOf(this.currentFileController);
        return (currentIndex > 0);
    }

    public boolean hasNext()
    {
        if (currentFileController == null)
        {
            return false;
        }
        int currentIndex = this.controllers.indexOf(this.currentFileController);
        return (currentIndex < this.controllers.size()-1);
    }

    public FileController nextFile()
    {
        int currentIndex = this.controllers.indexOf(this.currentFileController);
        if (currentIndex < this.controllers.size()-1)
        {
            this.currentFileController = this.controllers.get(currentIndex + 1);
            this.notifyObservers(null);
        }
        return this.currentFileController;
    }

    public FileController previousFile()
    {
        int currentIndex = this.controllers.indexOf(this.currentFileController);
        if (currentIndex > 0)
        {
            this.currentFileController = this.controllers.get(currentIndex - 1);
            this.notifyObservers(null);
        }
        return this.currentFileController;
    }

    private static String getFileExtension(String filename) {

        if (filename == null)
        {
            throw new IllegalArgumentException("filename is null!");
        }

        int indexOfLastExtension = filename.lastIndexOf(".");

        int lastSeparatorPositionWindows = filename.lastIndexOf("\\");
        int lastSeparatorPositionUnix = filename.lastIndexOf("/");

        int indexOfLastSeparator = Math.max(lastSeparatorPositionWindows, lastSeparatorPositionUnix);

        if (indexOfLastExtension > indexOfLastSeparator) {
            return filename.substring(indexOfLastExtension + 1);
        }

        return null;
    }

    private static boolean isAllowedFileType(String filename)
    {
        String extension = getFileExtension(filename);
        System.out.println("Extension" + extension);
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

    @Override
    public void registerObserver(IObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void deregisterObserver(IObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(IMessage message) {
        this.observers.stream().forEach(o -> o.update(message));
    }
}
