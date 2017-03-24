package org.szcloud.framework.metadesigner.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoPlement extends JFrame {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(AutoPlement.class);

	public AutoPlement() {
		this.setTitle("代码生成器");
		this.getContentPane().setLayout(null);
		this.setSize(600, 600);
		this.getContentPane().setBackground(Color.white);
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setLocation((int) (width - this.getWidth()) / 2, (int) (height - this.getHeight()) / 2);
		this.setVisible(true);
		add(getPackeage());
		add(getJTextPackage());
		add(getjlabelRootOut());
		add(getJTextCreatePath());
		add(getJButtonSubmit());
		add(getJButtonReset());
		add(getJLabelTableName());
		add(getJtextfiledTableName());
		add(getjlabelTempleteSrc());
		add(getJtextfiledClassName());
		add(getJlabelDataBase());
		add(getJTextDataBase());
		add(getJButtonPackage());
		add(getJButtonRootOut());
		add(getJButtonTableName());
		add(getJButtonTemplete());
		add(getJReadiButtonDefault());
		add(getJReadiButtonAutoPlement());
		add(getJCheckController());
		add(getJCheckService());
		add(getJCheckCore());
		add(getJCheckJsp());
		add(getJLabelChooseSrc());
		add(getJtextfiledClassName());
		add(getJLabelUserName());
		add(getJTextUserName());
		add(getJLabelPwd());
		add(getJTextPwd());
	}

	public static void main(String[] args) {
		AutoPlement auto = new AutoPlement();
	}

	private JLabel jlabelDataBase = new JLabel("数据库连接:");
	private JLabel jlabelBasePackage = new JLabel("包名:");
	private JLabel jlabelRootOut = new JLabel("生成路径:");
	private JLabel jlabelChooseSrc = new JLabel("请选择路径:");
	private JLabel jlabelTabelName = new JLabel("表名:");
	private JLabel jlabelTempleteSrc = new JLabel("模板路径:");
	private JTextField jtextDataBase = new JTextField();
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private JTextField jtextfiledTableName = new JTextField();
	private JTextField jtextfiledClass = new JTextField();
	private JTextField jtextfiledPackage = new JTextField();
	private JTextField jtextfiledCreatePath = new JTextField();
	private JCheckBox jcheckboxController = new JCheckBox("Controller");
	private JCheckBox jcheckboxService = new JCheckBox("Service");
	private JCheckBox jcheckboxCore = new JCheckBox("Core");
	private JCheckBox jcheckboxjsp = new JCheckBox("Jsp");
	private JButton jbuttonSubmit = new JButton("生成");
	private JButton jbuttonReset = new JButton("重置");
	private JButton jbuttonPackage = new JButton("选择");
	private JButton jbuttonRootOut = new JButton("选择");
	private JButton jbuttonTableName = new JButton("选择");
	private JRadioButton jrbDefault = new JRadioButton("默认");
	private JRadioButton jrbAutoPlement = new JRadioButton("自定义");
	private JFileChooser jfc = new JFileChooser();// 文件选择器
	private JLabel jlabelUserName = new JLabel("用户名:");
	private JLabel jlabelPwd = new JLabel("密码:");
	private JTextField jtextUserName = new JTextField();
	private JTextField jtextPwd = new JTextField();

	public JRadioButton getJReadiButtonDefault() {
		jrbDefault.setBounds(180, 320, 80, 30);
		jrbDefault.setBackground(Color.white);
		jrbDefault.setSelected(true);
		jrbDefault.setFont(new Font("宋体", Font.BOLD, 15));
		jrbDefault.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jrbDefault.setSelected(true);
				jrbAutoPlement.setSelected(false);
				jcheckboxController.setEnabled(true);
				jcheckboxService.setEnabled(true);
				jcheckboxCore.setEnabled(true);
				jcheckboxjsp.setEnabled(true);
				jlabelChooseSrc.setEnabled(false);
				jtextfiledClass.setEditable(false);
				jbuttonTempleteSrc.setEnabled(false);
			}
		});
		return jrbDefault;
	}

	public JRadioButton getJReadiButtonAutoPlement() {
		jrbAutoPlement.setBounds(270, 320, 100, 30);
		jrbAutoPlement.setBackground(Color.white);
		jrbAutoPlement.setFont(new Font("宋体", Font.BOLD, 15));
		jrbAutoPlement.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jrbDefault.setSelected(false);
				jcheckboxController.setEnabled(false);
				jcheckboxService.setEnabled(false);
				jcheckboxCore.setEnabled(false);
				jcheckboxjsp.setEnabled(false);
				jcheckboxController.setSelected(false);
				jcheckboxService.setSelected(false);
				jcheckboxCore.setSelected(false);
				jcheckboxjsp.setSelected(false);
				jlabelChooseSrc.setEnabled(true);
				jtextfiledClass.setEditable(true);
				jbuttonTempleteSrc.setEnabled(true);
			}
		});
		return jrbAutoPlement;
	}

	public JLabel getJlabelDataBase() {
		jlabelDataBase.setBounds(80, 20, 100, 30);
		jlabelDataBase.setFont(new Font("宋体", Font.BOLD, 15));
		return jlabelDataBase;
	}

	public JLabel getJLabelUserName() {
		jlabelUserName.setBounds(80, 70, 250, 30);
		jlabelUserName.setFont(new Font("宋体", Font.BOLD, 15));
		return jlabelUserName;
	}

	public JTextField getJTextUserName() {
		jtextUserName.setBounds(180, 70, 250, 30);
		return jtextUserName;
	}

	public JLabel getJLabelPwd() {
		jlabelPwd.setBounds(80, 120, 250, 30);
		jlabelPwd.setFont(new Font("宋体", Font.BOLD, 15));
		return jlabelPwd;
	}

	public JTextField getJTextPwd() {
		jtextPwd.setBounds(180, 120, 250, 30);
		return jtextPwd;
	}

	private JButton getJButtonPackage() {
		jfc.setCurrentDirectory(new File("d:\\"));// 文件选择器的初始目录定为d盘
		jbuttonPackage.setBounds(440, 170, 60, 30);
		jbuttonPackage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(jbuttonPackage)) {// 判断触发方法的按钮是哪个
					jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
					int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
					if (state == 1) {
						return;// 撤销则返回
					} else {
						File f = jfc.getSelectedFile();// f为选择到的目录
						jtextfiledPackage.setText(f.getAbsolutePath());
					}
				}
			}
		});
		return jbuttonPackage;
	}

	List<String> list = null;

	private JButton getJButtonTableName() {
		jfc.setCurrentDirectory(new File("d:\\"));// 文件选择器的初始目录定为d盘
		jbuttonTableName.setBounds(440, 270, 60, 30);
		jbuttonTableName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (jtextUserName.getText().trim().toString().equals("")) {
					JOptionPane.showMessageDialog(rootPane, "用户名不能为空！");
					return;
				}
				if (jtextPwd.getText().trim().toString().equals("")) {
					JOptionPane.showMessageDialog(rootPane, "密码不能为空！");
					return;
				}
				// sql语句，查询数据库为platformcloud的所有表名
				String sql = "select table_name from tables where TABLE_SCHEMA='platformcloud'";
				list = new ArrayList<String>();
				try {
					Class.forName("com.mysql.jdbc.Driver");
					// mysql连接,如果要用别的数据库，要修改连接
					conn = DriverManager.getConnection(jtextDataBase.getText().trim().toString() + "information_schema",
							jtextUserName.getText().trim().toString(), jtextPwd.getText().trim().toString());
					pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						list.add(rs.getString("table_name"));// 将表名添加到list中去
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(rootPane, "用户名或密码错误！");
					return;
				}
				if (conn == null) {
					JOptionPane.showMessageDialog(rootPane, "请打开数据库连接！");
					return;
				} else {
					final JFrame jf = new JFrame("请选择表名");
					jf.setBackground(Color.white);
					int windowWidth = jf.getWidth(); // 获得窗口宽
					int windowHeight = jf.getHeight(); // 获得窗口高
					Toolkit toolkit = Toolkit.getDefaultToolkit();
					int x = (int) (toolkit.getScreenSize().getWidth() - jf.getWidth());
					int y = (int) (toolkit.getScreenSize().getHeight() - jf.getHeight());
					jf.setLocation(x / 4, y / 6);// 窗体显示位置
					jf.setBackground(Color.white);
					jf.setSize(550, 600);
					jf.setLayout(null);
					jf.show();
					String[] array = new String[list.size()];
					list.toArray(array);
					JPanel jp = new JPanel();
					jp.setBackground(Color.white);
					// 定义一个JCheckBox数组来存储数据库的所有表
					final JCheckBox jcTableName[] = new JCheckBox[array.length];// 定义一个JCheckBox数组来装数据库中所有表
					for (int i = 0; i < array.length; i++) {
						// 循环创建JCheckBox，可勾选的表数组
						jcTableName[i] = new JCheckBox(array[i]);
						jcTableName[i].setBackground(Color.white);
						// 在循环加到JPanel中
						jp.add(jcTableName[i]);
					}
					JButton jbOK = new JButton("OK");
					jbOK.setBounds(250, 750, 250, 30);
					jbOK.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// 定义一个空字符串
							String str = "";
							for (int j = 0; j < jcTableName.length; j++) {
								// 循环JCheckBox数组，判断是否被选中
								if (jcTableName[j].isSelected()) {
									// 选中的话就拼接到字符串中，用逗号隔开
									str += jcTableName[j].getText() + ",";
								}
							}
							// 关闭窗体
							jf.setVisible(false);
							// 将你勾选的表（一个或者多个）赋值给文本框
							jtextfiledTableName.setText(str);
						}
					});
					jp.add(jbOK);
					JScrollPane jScrollBar1 = new JScrollPane(jp);
					jScrollBar1.setBounds(20, 20, 500, 800);
					jp.setPreferredSize(new Dimension(jScrollBar1.getWidth() - 50, jScrollBar1.getHeight() * 2));
					jf.add(jScrollBar1);
				}
			}
		});
		return jbuttonTableName;
	}

	private JButton jbuttonTempleteSrc = new JButton("选择");

	private JButton getJButtonTemplete() {
		jfc.setCurrentDirectory(new File("d:\\"));// 文件选择器的初始目录定为d盘
		jbuttonTempleteSrc.setBounds(440, 370, 60, 30);
		jbuttonTempleteSrc.setEnabled(false);
		jbuttonTempleteSrc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource().equals(jbuttonTempleteSrc)) {// 判断触发方法的按钮是哪个
					jfc.setFileSelectionMode(0);// 设定只能选择到文件
					int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
					if (state == 1) {
						return;// 撤销则返回
					} else {
						File f = jfc.getSelectedFile();// f为选择到的目录
						jtextfiledClass.setText(f.getAbsolutePath());
					}
				}
			}
		});
		return jbuttonTempleteSrc;
	}

	private JButton getJButtonRootOut() {
		jfc.setCurrentDirectory(new File("d:\\"));// 文件选择器的初始目录定为d盘
		jbuttonRootOut.setBounds(440, 220, 60, 30);
		jbuttonRootOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource().equals(jbuttonRootOut)) {// 判断触发方法的按钮是哪个
					jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
					int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
					if (state == 1) {
						return;// 撤销则返回
					} else {
						File f = jfc.getSelectedFile();// f为选择到的目录
						jtextfiledCreatePath.setText(f.getAbsolutePath());
					}
				}
			}
		});
		return jbuttonRootOut;
	}

	public JTextField getJTextDataBase() {
		jtextDataBase.setBounds(180, 20, 250, 30);
		jtextDataBase.setText("jdbc:mysql://10.86.0.29:3306/");
		return jtextDataBase;
	}

	public JLabel getPackeage() {
		jlabelBasePackage.setBounds(80, 170, 80, 30);
		jlabelBasePackage.setFont(new Font("宋体", Font.BOLD, 15));
		return jlabelBasePackage;
	}

	public JTextField getJTextPackage() {
		jtextfiledPackage.setBounds(180, 170, 250, 30);
		return jtextfiledPackage;
	}

	public JLabel getjlabelRootOut() {
		jlabelRootOut.setBounds(80, 220, 100, 30);
		jlabelRootOut.setFont(new Font("宋体", Font.BOLD, 15));
		return jlabelRootOut;
	}

	public JTextField getJTextCreatePath() {
		jtextfiledCreatePath.setBounds(180, 220, 250, 30);
		return jtextfiledCreatePath;
	}

	public JLabel getJLabelTableName() {
		jlabelTabelName.setBounds(80, 270, 100, 30);
		jlabelTabelName.setFont(new Font("宋体", Font.BOLD, 15));
		return jlabelTabelName;
	}

	public JTextField getJtextfiledTableName() {
		jtextfiledTableName.setBounds(180, 270, 250, 30);
		return jtextfiledTableName;
	}

	public JLabel getjlabelTempleteSrc() {
		jlabelTempleteSrc.setBounds(80, 320, 100, 30);
		jlabelTempleteSrc.setFont(new Font("宋体", Font.BOLD, 15));
		return jlabelTempleteSrc;
	}

	public JTextField getJtextfiledClassName() {
		jtextfiledClass.setBounds(180, 370, 250, 30);
		jtextfiledClass.setEditable(false);
		return jtextfiledClass;
	}

	public JLabel getJLabelChooseSrc() {
		jlabelChooseSrc.setBounds(80, 370, 100, 30);
		jlabelChooseSrc.setFont(new Font("宋体", Font.BOLD, 15));
		jlabelChooseSrc.setEnabled(false);
		return jlabelChooseSrc;
	}

	public JCheckBox getJCheckController() {
		jcheckboxController.setBounds(180, 410, 100, 30);
		jcheckboxController.setBackground(Color.white);
		return jcheckboxController;
	}

	public JCheckBox getJCheckService() {
		jcheckboxService.setBounds(300, 410, 100, 30);
		jcheckboxService.setBackground(Color.white);
		return jcheckboxService;
	}

	public JCheckBox getJCheckCore() {
		jcheckboxCore.setBounds(180, 450, 100, 30);
		jcheckboxCore.setBackground(Color.white);
		return jcheckboxCore;
	}

	public JCheckBox getJCheckJsp() {
		jcheckboxjsp.setBounds(300, 450, 100, 30);
		jcheckboxjsp.setBackground(Color.white);
		return jcheckboxjsp;
	}

	public JButton getJButtonSubmit() {
		jbuttonSubmit.setBounds(160, 500, 80, 30);
		jbuttonSubmit.setFont(new Font("宋体", Font.BOLD, 15));
		jbuttonSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HashMap map = new HashMap();
				if (conn == null) {
					JOptionPane.showMessageDialog(rootPane, "请打开数据库连接！");
					return;
				}
				String dataBaseConn = jtextDataBase.getText().trim().toString();
				if (dataBaseConn.equals("")) {
					JOptionPane.showMessageDialog(rootPane, "数据库连接不能为空！");
					return;
				}
				String userName = jtextUserName.getText().trim().toString();
				if (userName.equals("")) {
					JOptionPane.showMessageDialog(rootPane, "用户名不能为空！");
					return;
				}
				String pwd = jtextPwd.getText().trim().toString();
				if (pwd.equals("")) {
					JOptionPane.showMessageDialog(rootPane, "密码不能为空！");
					return;
				}
				String packageName = jtextfiledPackage.getText().trim().toString();
				if (packageName.equals("")) {
					JOptionPane.showMessageDialog(rootPane, "包名不能为空！");
					return;
				}
				map.put("packageName", packageName);
				String rootOut = jtextfiledCreatePath.getText().trim().toString();
				if (rootOut.equals("")) {
					JOptionPane.showMessageDialog(rootPane, "输出路径不能为空！");
					return;
				}
				map.put("rootOut", rootOut);
				String tableName = jtextfiledTableName.getText().toString();
				if (tableName.equals("")) {
					JOptionPane.showMessageDialog(rootPane, "表名不能为空！");
					return;
				}
				map.put("tableName", tableName);
				// 判断是否选中默认
				if (jrbDefault.isSelected()) {
					// 调用接口，传参数，执行代码生成。
					if (jcheckboxController.isSelected()) {
						String controller = jcheckboxController.getText();
						map.put("controller", controller);
					}
					if (jcheckboxService.isSelected()) {
						String service = jcheckboxService.getText();
						map.put("service", service);
					}
					if (jcheckboxCore.isSelected()) {
						String core = jcheckboxCore.getText();
						map.put("core", core);
					}
					if (jcheckboxjsp.isSelected()) {
						String jsp = jcheckboxjsp.getText();
						map.put("jsp", jsp);
					}
				} else// 否则是选中自定义
				{
					String templementSrc = jtextfiledClass.getText().trim().toString();
					if (templementSrc.equals("")) {
						JOptionPane.showMessageDialog(rootPane, "模板路径不能为空！");
						return;
					}
					map.put("templementSrc", templementSrc);
				}
				// 调用接口传入map参数
				logger.debug(map.get("packageName") + "");
				logger.debug(map.get("tableName") + "");
				logger.debug(map.get("rootOut") + "");
				logger.debug(map.get("controller") + "");
				logger.debug(map.get("core") + "");
				logger.debug(map.get("service") + "");
				logger.debug(map.get("jsp") + "");
				logger.debug(map.get("templementSrc") + "");
			}
		});
		return jbuttonSubmit;
	}

	public JButton getJButtonReset() {
		jbuttonReset.setBounds(340, 500, 80, 30);
		jbuttonReset.setFont(new Font("宋体", Font.BOLD, 15));
		jbuttonReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jtextDataBase.setText("");
				jtextfiledTableName.setText("");
				jtextfiledPackage.setText("");
				jtextfiledCreatePath.setText("");
				jtextUserName.setText("");
				jtextPwd.setText("");
				jrbDefault.setSelected(true);
				jrbAutoPlement.setSelected(false);
				jtextfiledClass.setEnabled(false);
				jlabelChooseSrc.setEnabled(false);
				jbuttonTempleteSrc.setText("");
				jbuttonTempleteSrc.setEnabled(false);
				jcheckboxController.setSelected(false);
				jcheckboxService.setSelected(false);
				jcheckboxCore.setSelected(false);
				jcheckboxjsp.setSelected(false);
				jcheckboxController.setEnabled(true);
				jcheckboxService.setEnabled(true);
				jcheckboxCore.setEnabled(true);
				jcheckboxjsp.setEnabled(true);
			}
		});
		return jbuttonReset;
	}
}
