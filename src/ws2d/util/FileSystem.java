package ws2d.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A utility class for working with the file system.
 * 
 * @author Ian
 */
public final class FileSystem {
    /**
     * Private to prevent instantiation.
     */
    private FileSystem() { }
    
    /**
     * Created a file from a list of path elements.
     * 
     * @param elements the elements to create a file from.
     * @return a file created from the give path elements.
     */
    public static File getFile(String... elements) {
        return new File(String.join(File.separator, elements));
    }
    
    /**
     * Appends path elements to a given file.
     * 
     * @param source the base path.
     * @param appendage the elements to append.
     * @return a file with the specified elements appended to the soruce file.
     */
    public static File appendElement(File source, String appendage) {
        return new File(source.getAbsolutePath() + File.separator + appendage);
    }
    
    /**
     * Creates a file and any parent directories as well.
     * 
     * @param file the file to create.
     * @return <code>true</code>, if the file was created, <code>false</code> otherwise.
     * @throws IOException if an I/O error occurs.
     */
    public static boolean createFile(File file) throws IOException {
        if(file.exists())
            return false;
        file = file.getAbsoluteFile();
        file.getParentFile().mkdirs();
        return file.createNewFile();
    }
    
    /**
     * Renames a specified file to have the given new name.
     * 
     * @param old the old file.
     * @param newName the new file name.
     * @return <code>true</code>, if the file was renamed, <code>false</code> otherwise.
     */
    public static boolean rename(File old, String newName) {
        String absp = old.getAbsolutePath();
        return old.renameTo(new File(absp.substring(absp.lastIndexOf(File.separator)) + File.separator + newName));
    }
    
    /**
     * Deletes a give file or directory. If the file is a directory, then all contents
     * of that directory are deleted recursively.
     * 
     * @param file the file to delete.
     * @throws IOException if and I/O error occurs.
     */
    public static void delete(File file) throws IOException {
        if(!file.exists())
            return;
        if(file.isDirectory()) {
            Files.walkFileTree(file.toPath(), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) throws IOException {
                    File f = p.toFile();
                    if(f.isFile())
                        f.delete();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    dir.toFile().delete();
                    return FileVisitResult.CONTINUE;
                }
            });
        }else
            file.delete();
    }
    
    /**
     * Directs the data in an input stream through a buffer to the specified destination
     * file.
     * 
     * @param source the source stream.
     * @param dest the destination file.
     * @param bufferSize the buffer size.
     * @throws IOException if an I/O error occurs.
     */
    public static void copy(InputStream source, File dest, int bufferSize) throws IOException {
        if(!dest.exists())
            createFile(dest);
        FileOutputStream ofstream = new FileOutputStream(dest);
        byte[] buffer = new byte[bufferSize];
        int len;
        while((len = source.read(buffer)) > 0)
            ofstream.write(buffer, 0, len);
        source.close();
        ofstream.flush();
        ofstream.close();
    }
    
    /**
     * Directs the data in an input stream through a buffer with a size of <code>65536</code>
     * to the specified destination file.
     * 
     * @param source the source stream.
     * @param dest the destination file.
     * @throws IOException if an I/O error occurs.
     */
    public static void copy(InputStream source, File dest) throws IOException {
        copy(source, dest, 65536);
    }
    
    /**
     * Copies a source file to a destination file with a specified buffer size.
     * 
     * @param source the source file.
     * @param dest the destination file.
     * @param bufferSize the buffer size.
     * @throws IOException if an I/O error occurs.
     */
    public static void copyFile(File source, File dest, int bufferSize) throws IOException {
        FileInputStream ifstream = new FileInputStream(source);
        if(dest.exists() && dest.isDirectory()) {
            dest = getFile(dest.getAbsolutePath(), source.getName());
            createFile(dest);
        }else if(!dest.exists())
            createFile(dest);
        FileOutputStream ofstream = new FileOutputStream(dest);
        byte[] buffer = new byte[bufferSize];
        int read;
        while((read = ifstream.read(buffer)) > 0)
            ofstream.write(buffer, 0, read);
        ifstream.close();
        ofstream.flush();
        ofstream.close();
    }
    
    /**
     * Copies a directory to another location.
     * 
     * @param source the source directory.
     * @param dest the destination directory.
     * @param bufferSize the buffer size.
     * @throws IOException if an I/O error occurs.
     */
    public static void copyDir(File source, File dest, int bufferSize) throws IOException {
        String sabsp = source.getAbsolutePath();
        final String basedir = sabsp.substring(0, sabsp.lastIndexOf(File.separator));
        final String destdir = dest.getAbsolutePath();
        Files.walkFileTree(source.toPath(), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                String absp = dir.toAbsolutePath().toString();
                if(absp.equals(basedir))
                    return FileVisitResult.CONTINUE;
                getFile(destdir, absp.substring(basedir.length() + 1)).mkdirs();
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) throws IOException {
                copyFile(p.toFile(), getFile(destdir, p.toAbsolutePath().toString().substring(basedir.length() + 1)), bufferSize);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
