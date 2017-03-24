package org.szcloud.framework.workflow.core.module;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作模块
 */
public class MFile {
	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(MFile.class);

	/**
	 * 取文件的扩展名
	 * 
	 * @param as_FilePath
	 *            文件名称（可以不含文件路径）
	 * @return 返回文件扩展名
	 */
	public static String getFileExt(String as_FilePath) {
		if (as_FilePath == null || as_FilePath.equals(""))
			return "tmp";
		int i = as_FilePath.lastIndexOf(".");
		if (i == -1)
			return as_FilePath;
		return as_FilePath.substring(i + 1, as_FilePath.length());
	}

	/**
	 * 取临时文件名称
	 * 
	 * @param as_FileExt
	 *            临时文件扩展名
	 * @return 返回临时文件名称，返回空表示取临时文件失败
	 */
	public static String getTempFile(String as_FileExt) throws IOException {
		return getTempFile(as_FileExt, null, false, true);
	}

	/**
	 * 取临时文件名称
	 * 
	 * @param as_FileExt
	 *            临时文件扩展名
	 * @param as_FilePath
	 *            是否指定在某个目录下创建
	 * @return 返回临时文件名称，返回空表示取临时文件失败
	 */
	public static String getTempFile(String as_FileExt, String as_FilePath) throws IOException {
		return getTempFile(as_FileExt, as_FilePath, false, true);
	}

	/**
	 * 取临时文件名称
	 * 
	 * @param as_FileExt
	 *            临时文件扩展名
	 * @param as_FilePath
	 *            是否指定在某个目录下创建
	 * @param ab_ReturnFull
	 *            是否返回全路径
	 * @return 返回临时文件名称，返回空表示取临时文件失败
	 */
	public static String getTempFile(String as_FileExt, String as_FilePath, Boolean ab_ReturnFull) throws IOException {
		return getTempFile(as_FileExt, as_FilePath, ab_ReturnFull, true);
	}

	/**
	 * 取临时文件名称
	 * 
	 * @param as_FileExt
	 *            临时文件扩展名
	 * @param as_FilePath
	 *            是否指定在某个目录下创建
	 * @param ab_ReturnFull
	 *            是否返回全路径
	 * @param ab_DeleteFile
	 *            是否需要删除临时文件
	 * @return 返回临时文件名称，返回空表示取临时文件失败
	 */
	public static String getTempFile(String as_FileExt, String as_FilePath, Boolean ab_ReturnFull,
			Boolean ab_DeleteFile) throws IOException {
		File lo_Path = null;
		if (as_FilePath != null && as_FilePath != "")
			lo_Path = new File(as_FilePath);
		String ls_Ext = "." + getFileExt(as_FileExt);
		File lo_File = File.createTempFile("TMP", ls_Ext, lo_Path);
		ls_Ext = lo_File.getPath();
		if (ab_DeleteFile)
			lo_File.delete();
		lo_File = null;
		if (lo_Path != null) {
			if (!ab_ReturnFull) {
				if (as_FilePath.substring(as_FilePath.length() - 1, as_FilePath.length()).equals("\\")) {
					ls_Ext = ls_Ext.substring(as_FilePath.length(), ls_Ext.length());
				} else {
					ls_Ext = ls_Ext.substring(as_FilePath.length() + 1, ls_Ext.length());
				}
			}
			lo_Path = null;
		}
		return ls_Ext;
	}

	/**
	 * 比较两个文件是否相同
	 * 
	 * @param as_FileName1
	 *            文件名称1（含路径）
	 * @param as_FileName2
	 *            文件名称2（含路径）
	 * @return 返回是否相同的标记
	 */
	public static Boolean fileCompare(String as_FileName1, String as_FileName2) {
		FileInputStream inFile1 = null;
		FileInputStream inFile2 = null;
		try {
			inFile1 = new FileInputStream(as_FileName1);
			inFile2 = new FileInputStream(as_FileName1);
			if (inFile1.available() != inFile2.available()) {
				logger.debug("different");
				System.exit(0);
				return false;
			}
			int ch1, ch2;
			while (((ch1 = inFile1.read()) != -1) && ((ch2 = inFile2.read()) != -1)) {
				if (ch1 == ch2) {
					logger.debug("same");
					return true;
				} else {
					logger.debug("different");
					return false;
				}
			}
		} catch (Exception e) {
			logger.debug(e.toString());
			return false;
		} finally {
			try {
				if (inFile1 != null)
					inFile1.close();
				if (inFile2 != null)
					inFile2.close();
			} catch (IOException e) {
				logger.debug(e.toString());
			}
		}
		return false;
	}

	/**
	 * 取文件的名称
	 * 
	 * @param as_FullFileName
	 *            文件全路径
	 * @return 返回文件路径
	 */
	public static String getFileName(String as_FullFileName) {
		int i = as_FullFileName.lastIndexOf("\\");
		return as_FullFileName.substring(i + 1, as_FullFileName.length());
	}

	/**
	 * 取文件的路径
	 * 
	 * @param as_FullFileName
	 *            文件全路径
	 * @return 返回文件路径
	 */
	public static String getFilePath(String as_FullFileName) {
		int i = as_FullFileName.lastIndexOf("\\");
		return as_FullFileName.substring(0, i + 1);
	}

	/**
	 * 获取当前应用的路径
	 * 
	 * @return
	 */
	public static String getPath() {
		return System.getProperty("user.dir") + "\\";
	}

	/**
	 * 将一个文件转换为字节数组
	 * 
	 * @param ao_File
	 *            文件对象
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static byte[] getBytesFromFile(File ao_File) throws IOException {
		InputStream lo_Stream = new FileInputStream(ao_File);
		// 获取文件大小
		if (ao_File.length() > Integer.MAX_VALUE) {
			// 文件太大，无法读取
			throw new IOException("File is to large " + ao_File.getName());
		}
		// 创建一个数据来保存文件数据
		byte[] bytes = new byte[(int) ao_File.length()];
		// 读取数据到byte数组中
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = lo_Stream.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + ao_File.getName());
		}
		// Close the input stream and return bytes
		lo_Stream.close();
		return bytes;
	}

	// public static void main(String[] args) throws IOException {
	// File f=new File("E:/project/szgovcloudsvn.rar");
	// byte[] b=getBytesFromFile(f);
	// logger.debug(b.length);
	// }

	/**
	 * 写二进制文件
	 * 
	 * @param as_FileName
	 *            文件夹全路径
	 * @param ay_FileData
	 *            文件二进制内容
	 * @return 返回空表示写二进制文件成功，否则返回写二进制文件错误的信息
	 */
	public static String writeFile(String as_FileName, byte[] ay_FileData) {
		File f = createFile(as_FileName);
		FileOutputStream fos;
		try {
			logger.debug("创建输出流...");
			fos = new FileOutputStream(f);
			logger.debug("创建输出流完成");
			logger.debug("开始写数据...");
			fos.write(ay_FileData);
			fos.flush();
			logger.debug("写数据完成");
			fos.close();
			return null;
		} catch (FileNotFoundException e) {
			logger.debug("文件没有找到");
			e.printStackTrace();
			return "没有找到文件";
		} catch (IOException e) {
			e.printStackTrace();
			return "读取文件失败";
		}
	}

	// public static void main(String[] args) throws IOException {
	// File f=new File("E:/project/szgovcloudsvn.rar");
	// byte[] b=getBytesFromFile(f);
	// String s=writeFile("F:/test/jjj.rar",b);
	// logger.debug(s);
	// }

	/**
	 * 读二进制文件
	 * 
	 * @param as_FileName
	 *            文件全路径
	 * @return 返回空表示读取失败，否则返回文件的二进制内容
	 */
	public static byte[] readFile(String as_FileName) throws IOException {
		File f = new File(as_FileName);
		byte[] b = getBytesFromFile(f);
		return b;
	}

	// public static void main(String[] args) throws IOException {
	// File f=new File("E:/project/szgovcloudsvn.rar");
	// byte[] b=readFile("E:/project/szgovcloudsvn.rar");
	// logger.debug(b.length);
	// }

	/**
	 * 取当前计算机的名称
	 */
	public static String computerName() {
		logger.debug(System.getProperty("user.name"));
		return System.getProperty("user.name");
	}

	// public static void main(String[] args) {
	// String s=computerName();
	// logger.debug(s);
	// }

	/*
	 * 写文本文件
	 * 
	 * @param as_FileName 文件全路径
	 * 
	 * @param ay_FileData 文件文本内容
	 * 
	 * @return 返回空表示写文本文件成功，否则返回写文本文件错误的信息 注：覆盖性的添加
	 */
	public static String writeTxtFile(String as_FileName, String av_FileData) {

		try {
			FileWriter fwriter = new FileWriter(as_FileName);
			BufferedWriter bfwriter = new BufferedWriter(fwriter);
			bfwriter.write(av_FileData, 0, av_FileData.length());
			bfwriter.flush();
			bfwriter.close();
			return null;
		} catch (IOException e) {
			logger.debug(e.toString());
			return "失败";
		}
	}

	/**
	 * 读文本文件
	 * 
	 * @param as_FileName
	 *            文件全路径
	 * @return 返回空表示读取失败，否则返回文件的文本内容
	 */
	public static String readTxtFile(String as_FileName) {
		List<String> ls = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(as_FileName)));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				ls.add(line);
			}
			br.close();
			StringBuffer s = new StringBuffer();
			for (String ss : ls) {
				s.append(ss);
			}
			return s.toString();
		} catch (Exception e) {
			logger.debug(e.toString());
			return null;
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param as_FolderName
	 *            文件夹路径
	 * @return 返回是否成功创建的标记
	 */
	public static Boolean createFolder(String as_FolderName) {
		try {
			File file = new File(as_FolderName);
			file.mkdirs();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 删除文件夹和文件夹下所有的文件
	 * 
	 * @param as_FolderName
	 *            文件夹路径
	 * @return 返回是否成功创建的标记
	 */
	public static Boolean deleteFolder1(String as_FolderName) {
		boolean flag;
		if (!as_FolderName.endsWith(File.separator)) {
			as_FolderName = as_FolderName + File.separator;
		}
		File dirFile = new File(as_FolderName);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteFolder1(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param sPath
	 * @return
	 */
	public static boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteFolder1(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag;
		flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 取文件的创建、最近访问、最近修改时间
	 * 
	 * @param as_FileName
	 *            文件全路径
	 * 
	 * @param ai_DateType
	 *            获取类型：=0-创建时间；=1-最近访问时间；=2-最近修改时间；
	 * 
	 * @return 返回对应时间
	 */
	private static native String getFileCreationTime(String fileName);

	/**
	 * 获取文件时间
	 * 
	 * @param as_FileName
	 * @param ai_DateType
	 * @return
	 * @throws Exception
	 */
	public static String getFileDate(String as_FileName, int ai_DateType) throws Exception {
		if (ai_DateType == 0) {
			try {
				Process ls_proc = Runtime.getRuntime().exec("cmd.exe /c dir " + as_FileName + " /tc");
				BufferedReader br = new BufferedReader(new InputStreamReader(ls_proc.getInputStream()));
				for (int i = 0; i < 5; i++) {
					br.readLine();
				}
				String stuff = br.readLine();
				StringTokenizer st = new StringTokenizer(stuff);
				String dateC = st.nextToken();
				String time = st.nextToken();
				String datetime = dateC.concat(time);
				br.close();
				return datetime;
			} catch (Exception e) {
				return null;
			}
		} else if (ai_DateType == 1) {
			FileInputStream fis = new FileInputStream(new File(as_FileName));
			logger.debug("File Size:" + fis.available());
			fis.close();
			return null;
		} else {
			String ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new File(as_FileName).lastModified()));
			logger.debug("最后修改时间:" + ss);
			return ss;
		}

	}

	/**
	 * 复制由Word文件生成的HTML文件到某一个地方
	 * 
	 * @param as_SourceFile
	 *            源文件路径
	 * @param as_GoalFile
	 *            目标文件夹路径
	 * @param as_SourceFolder
	 *            源文件夹
	 * @return 返回是否成功复制的标记
	 */
	public static Boolean copySpecialFile(String as_SourceFile, String as_GoalFile, String as_SourceFolder) {
		try {
			FileInputStream fis = new FileInputStream(as_SourceFile);
			BufferedInputStream bufis = new BufferedInputStream(fis);

			FileOutputStream fos = new FileOutputStream(as_GoalFile);
			BufferedOutputStream bufos = new BufferedOutputStream(fos);

			if (as_SourceFolder != null && !as_SourceFolder.equals("")) {
				// 获取目标文件的文件夹
				String o = getFilePath(as_GoalFile);
				copyDirectory(as_SourceFolder, o);
			} else {
				String sourceName = as_SourceFile.substring(as_SourceFile.lastIndexOf("/") + 1,
						as_SourceFile.lastIndexOf("."));
				String sor = getFilePath(as_SourceFile);
				String ssf = sor + sourceName + ".files";
				// 获取目标文件的文件夹
				// String o = getFilePath(as_GoalFile);
				copyDirectory(ssf, as_GoalFile);
			}

			int len = 0;
			while ((len = bufis.read()) != -1) {
				bufos.write(len);
			}
			bufis.close();
			bufos.close();

		} catch (Exception e) {
			logger.debug("复制单个文件操作出错");
			e.printStackTrace();

		}
		return true;
	}

	/**
	 * 复制文件夹
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static void copyDirectory(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyDirectory(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			logger.debug("复制整个文件夹内容操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					logger.debug(bytesum + "");
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			logger.debug("复制单个文件操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			logger.debug("复制整个文件夹内容操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 删除由Word文件生成的HTML文件
	 * 
	 * @param as_FileName
	 *            文件路径
	 * @param as_Folder
	 *            文件夹
	 * @return 返回是否成功删除的标记
	 */
	public static Boolean killSpecialFile(String as_FileName, String as_Folder) {
		deleteFile(as_FileName);
		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @param as_File
	 *            被删除的文件路径，多个使用【|】分隔
	 * @return 返回是否成功删除的标记
	 */
	public static boolean killFile(String as_File) {
		try {
			String[] s = as_File.split("|");
			for (String ss : s) {
				deleteFile(ss);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取文件对象，如果文件不存在，则创建文件
	 * 
	 * @param as_File
	 *            文件名称
	 * @return
	 */
	public static File createFile(String as_File) {
		File lo_File = new File(as_File);
		if (lo_File.exists()) {
			logger.debug("文件已经存在");
			return lo_File;
		} else {
			logger.debug("文件不存在");
			try {
				logger.debug("创建文件...");
				lo_File.createNewFile();
				logger.debug("创建文件完成");
				return lo_File;
			} catch (IOException ex) {
				logger.debug("文件创建失败");
				ex.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param as_File
	 *            文件名称
	 * @return
	 */
	public static boolean existFile(String as_File) {
		return (new File(as_File)).exists();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param as_File
	 *            文件名称
	 * @return
	 */
	public static boolean existFolder(String as_Folder) {
		return (new File(as_Folder)).exists();
	}

	/**
	 * 文件重命名
	 * 
	 * @param as_OldName
	 *            原来的文件名(含路径)
	 * @param as_NewName
	 *            新文件名(含路径)
	 */
	public static boolean rename(String as_OldName, String as_NewName) {
		if (as_OldName.toLowerCase().equals(as_NewName.toLowerCase()))
			return false; // 新文件名和旧文件名相同

		File lo_NewFile = new File(as_NewName);
		if (lo_NewFile.exists())
			return false; // 若在该目录下已经有一个文件和新文件名相同，则不允许重命名

		File lo_OldFile = new File(as_OldName);
		if (!lo_OldFile.exists())
			return false; // 重命名文件不存在
		return lo_OldFile.renameTo(lo_NewFile);
	}

	/**
	 * 文件重命名
	 * 
	 * @param as_Path
	 *            文件目录
	 * @param as_OldName
	 *            原来的文件名
	 * @param as_NewName
	 *            新文件名
	 */
	public static boolean rename(String as_Path, String as_OldName, String as_NewName) {
		return rename(as_Path + "\\" + as_OldName, as_Path + "\\" + as_NewName);
	}

}
