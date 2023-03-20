package dp_detection_in_sources;

import java.io.File;
import java.util.ArrayList;

public class SearchClassFiles  {
    private ArrayList<File> result = new ArrayList();

    public SearchClassFiles() {
        result = new ArrayList();
    }

    public ArrayList<File> getClassFiles(String dirName) {
        getClassFilesRecursive(new File(dirName));
        
        return result;
    } 
    
    private void getClassFilesRecursive(File dir) {
        File[] files = dir.listFiles();
        
        for (File file : files) {
            if (file.isDirectory()) {
                getClassFiles(file.getAbsolutePath());
            } else if (file.getName().endsWith("class")) {
                result.add(file);
            }
        }
    }
}
