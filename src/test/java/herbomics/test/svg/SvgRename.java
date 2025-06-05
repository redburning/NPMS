package herbomics.test.svg;

import java.io.File;
import java.util.List;

import herbomics.test.util.FileUtil;

public class SvgRename {

	public static void main(String[] args) {
		String folderPath = "F:\\MyProjects\\herbomics\\资料\\data-v2\\NPMS-V1 Build0530封装材料\\Mol & SVG Structure\\SVG";
		List<String> svgFiles = FileUtil.listDir(folderPath, ".svg");
		
		for (String svgFile : svgFiles) {
			File oldFile = new File(svgFile);
			String oldName = oldFile.getName();
			String newName = oldName.split("_")[0] + ".svg";
			File newFile = new File(oldFile.getParent() + "\\" + newName);
			oldFile.renameTo(newFile);
		}
	}
}
