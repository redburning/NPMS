package herbomics.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileUtil {

	public static boolean removeFile(String path) {
		File file = new File(path);
		validateFile(file);
		if (file.exists()&&file.isFile()) {
			if (file.delete()) {
				return true;
			}
		} else {
			new FileNotFoundException(path).printStackTrace();
		}
		return false;
	}
	
	public static boolean existFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
        	return true;
        }
        return false;
	}

	public static String readToString(String fileName) {  
        File file = new File(fileName);
        if (!(file.exists() && file.isFile())) {
        	return null;
        }
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }  
        return new String(filecontent);  
    }
	
	public static boolean deleteDir(String path){;
		return deleteDir(new File(path));
	}

    public static boolean deleteDir(File dir) {
    	validateDir(dir);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    
    /**
     * list files in dir
     */
    public static File[] listDir(File dir){
    	validateDir(dir);
    	if(dir.isDirectory()){
    		return dir.listFiles();
    	}
    	return null;
    }
    
    /**
     * 获取path下的文件完整路径列表
     * 
     * @param path
     * @param filter 过滤关键字
     * @return
     */
    public static List<String> listDir(String path, String... filters) {
    	List<String> fileList = new ArrayList<>();
    	File dir = new File(path);
    	validateDir(dir);
    	if (dir.isDirectory()) {
    		File[] files = dir.listFiles();
    		// 按照文件的修改时间排序
    		Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return (Long.valueOf(o1.lastModified()).compareTo(o2.lastModified()));
				}
			});
    		for (File f : files) {
    			String filePath = f.getPath();
    			for (String filter : filters) {
    				if (filePath.contains(filter)) {
        				fileList.add(filePath);
        			}
    			}
    		}
    	}
    	return fileList;
    }
    
    /**
     * 获取path下文件名列表
     * @param path
     * @param filters
     * @return
     */
    public static List<String> listFiles(String path, String... filters) {
    	List<String> fileList = new ArrayList<>();
    	File dir = new File(path);
    	validateDir(dir);
    	if (dir.isDirectory()) {
    		File[] files = dir.listFiles();
    		for (File f : files) {
    			String fileName = f.getName();
    			for (String filter : filters) {
    				if (fileName.contains(filter)) {
        				fileList.add(fileName);
        			}
    			}
    		}
    	}
    	return fileList;
    }
    
    /**
     * 获取文件大小
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
    	File file = new File(path);
    	return file.length();
    }

    /**
     * 获取文件上次修改时间
     * @param filePath
     * @return
     * @throws IOException
     */
    public static long getFileLastModifiedTime(String filePath) throws IOException {
    	Path path = Paths.get(filePath);
    	BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
    	return attrs.lastModifiedTime().toMillis();
    }
    
	public static void validateDir(File dir) {
		validateFile(dir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	public static void validateFile(File file) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
	}

}
