package ws2d.init.setup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import ws2d.util.FileSystem;
import ws2d.util.Logger;

/**
 * This class instantiates a new project to create a Ws2D game. It initializes the
 * template given a root directory, main class path, and project name.
 * 
 * @author Ian
 */
public final class ProjectSetup {
    /**
     * The root directory of the project.
     */
    private final File root;
    /**
     * The main class of the project.
     */
    private final String mainClass;
    /**
     * The name of the project.
     */
    private final String projectName;
    /**
     * The Ws2D jar.
     */
    private final String ws2dJar;
    /**
     * <code>true</code>, if bash files should be generated, <code>false</code> otherwise.
     */
    private final boolean bashOrBatch;
    
    /**
     * The project setup logger.
     */
    private static final Logger LOG = new Logger("SETUP");
    
    /**
     * Constructs a new instance of <code>ProjectSetup</code> with a specified root
     * directory, main class, and project name.
     * 
     * @param root the root directory.
     * @param mainClass the class name, for example: <code>com.test.MyClass</code>.
     * @param projectName the name of the project.
     * @param ws2dJar the Ws2D jar
     * @param bashOrBatch <code>true</code>, if bash files should be generated,
     * <code>false</code> otherwise.
     */
    public ProjectSetup(File root, String mainClass, String projectName, String ws2dJar, boolean bashOrBatch) {
        this.root = root;
        if(!mainClass.matches("(\\w+\\.)+\\w+"))
            throw new IllegalArgumentException("Invalid class name.");
        this.mainClass = mainClass;
        this.projectName = projectName;
        this.ws2dJar = ws2dJar;
        this.bashOrBatch = bashOrBatch;
    }
    
    /**
     * Sets up the project by creating any files or directories that are needed.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void setup() throws IOException {
        if(!root.exists())
            root.mkdirs();
        File gameJar = new File(ws2dJar);
        if(!gameJar.exists()) {
            LOG.error("The specified game jar does not exist.");
            return;
        }
        LOG.info("Extracting and writing files...");
        
        /* Project Files */
        copy("index.html", getFile("src", "client", "index.html"),
                "        <title>" + projectName + "</title>");
        copy("stylesheet.css", getFile("src", "client", "stylesheet.css"));
        copy("bootstrap.js", getFile("src", "client", "bootstrap.js"));
        copy("client.js", getFile("src", "client", "client.js"));
        copy("game.java_dat", getFile("src", mainClass.replaceAll("\\.", "/") + ".java"),
                "package " + mainClass.substring(0, mainClass.lastIndexOf('.')) + ";",
                "public class " + mainClass.substring(mainClass.lastIndexOf('.') + 1) + " {");
        copy("manifest.mf", getFile("META-INF", "manifest.mf"));
        copy("game.json_dat", getFile("src", "game.json"),
                "    \"main\": \"" + mainClass + "\",",
                "    \"name\": \"" + projectName + "\",");
        
        /* System Files */
        FileSystem.copyFile(gameJar, getFile("run", "ws2d.jar"), 65536);
        File classpath = getFile("classpath.txt");
        FileSystem.createFile(classpath);
        PrintStream ofstream = new PrintStream(new FileOutputStream(classpath));
        ofstream.print(bashOrBatch ? "./run/ws2d.jar" : "..\\run\\ws2d.jar");
        ofstream.flush();
        ofstream.close();
        if(bashOrBatch) {
            copy("build.sh", getFile("build.sh"));
            copy("launch.sh", getFile("run", "launch.sh"));
        }else{
            copy("build.bat", getFile("build.bat"));
            copy("launch.bat", getFile("run", "launch.bat"));
        }
        
        LOG.info("Project setup finished.");
    }
    
    /**
     * Returns a file with all the specified path elements stemming off the root
     * directory.
     * 
     * @param pathElements the path elements to combine.
     * @return a file with all the specified path elements stemming off the root
     * directory.
     */
    private File getFile(String... pathElements) {
        return new File(root.getAbsolutePath() + File.separator + String.join(File.separator, pathElements));
    }
    
    /**
     * Copies a resource file to a destination file.
     * 
     * @param resource the resource name.
     * @param dest the destination.
     * @param replacementLines any lines to fill in argument-dependent data.
     * @throws IOException if an I/O error occurs.
     */
    private void copy(String resource, File dest, String... replacementLines) throws IOException {
        Scanner istream = new Scanner(ProjectSetup.class.getResourceAsStream(resource));
        if(!dest.exists()) {
            dest.getParentFile().mkdirs();
            dest.createNewFile();
        }
        FileOutputStream ofstream = new FileOutputStream(dest);
        String line;
        while(istream.hasNextLine()) {
            line = istream.nextLine();
            if(!line.isEmpty() && line.charAt(0) == '%')
                ofstream.write(replacementLines[Integer.parseInt(line.substring(1))].getBytes());
            else
                ofstream.write(line.getBytes());
            if(istream.hasNextLine())
                ofstream.write('\n');
        }
        istream.close();
        ofstream.flush();
        ofstream.close();
    }
}
