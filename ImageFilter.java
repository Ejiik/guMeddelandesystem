package guMeddelandesystem;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter {
	public boolean accept(File file) {
		if(file.isDirectory()) {
			return true;
		}
		String filename = file.getName();
		String filetype = filename.substring(filename.lastIndexOf("."));
		if (filetype != null) {
			if(filetype.equals("jpg") || filetype.equals("gif") || filetype.equals("png")
					|| filetype.equals("jpeg")) {
				return true;
			} else {
				return false;
			}
		}
			return false;
	}
	
	public String getDescription() {
		return "Endast .jpg., .gif eller .png.";
	}
}
