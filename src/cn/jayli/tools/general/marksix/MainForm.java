package cn.jayli.tools.general.marksix;
import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class MainForm extends JFrame implements ActionListener {
	/**
	 * @author Jayli
	 * @date 2015-11-20 01:16:07
	 */
	private static final long serialVersionUID = 1L;
	static String directory = null, filename = "marksix.txt";// 路径名+文件名
	static String url = "";// 开奖页地址
	static String hsurl = "http://www.227226.com/kj/kjjl.htm#88";// 开奖历史地址
	static ArrayList<Record> records = new ArrayList<Record>();//结果集
	static String curUrl = "";//当前下载链接
	
	JPanel jp = new JPanel(new MigLayout());
	JLabel jdzl = new JLabel("开奖年份：");
	JLabel kjqh = new JLabel("  期号：");
	JLabel jgl = new JLabel("结果列表");
	List<ComboBoxItem> sl = new ArrayList<ComboBoxItem>();
	JComboBox<ComboBoxItem> jcb = new JComboBox<ComboBoxItem>();
	JComboBox<String> jcbqh = new JComboBox<String>();
	JTextField jurl = new JTextField(url);
	JTextArea jga = new JTextArea(100, 80);
	JButton queryBtn = new JButton("更新");
	JButton saveBtn = new JButton("保存");
	JButton resetBtn = new JButton("重置");
	JButton kjlsBtn = new JButton("历史开奖");

	JScrollPane js = new JScrollPane(jga);
	private FileDialog openDia;
	JFileChooser chooser = new JFileChooser();

	public MainForm() {
		super("六合彩开奖结果");
		jcb.addItem(new ComboBoxItem( "http://www.227226.com/rlist/20160518210831.htm","2016猴年"));
		jcb.addItem(new ComboBoxItem( "http://www.227226.com/rlist/20160518210748.htm","2015羊年"));
		jcb.addItem(new ComboBoxItem( "http://www.227226.com/rlist/20160518210715.htm","2014马年"));
		jcb.addItem(new ComboBoxItem( "http://www.227226.com/rlist/20160518210654.htm","2013蛇年"));
		jcb.addItem(new ComboBoxItem( "http://www.227226.com/rlist/20160518210621.htm","2012龙年"));
		jcb.addItem(new ComboBoxItem( "http://www.227226.com/rlist/20160518210549.htm","2011兔年"));
		jcb.addItem(new ComboBoxItem( "http://www.227226.com/rlist/20160518210528.htm","2010虎年"));
		
		jcbqh.addItem(new String("全部"));
		for(int i=1; i<160;i++){
			String str = "00000" + String.valueOf(i);
			jcbqh.addItem(str.substring(str.length()-3));
		}
		
		jp.add(jdzl, "cell 0 0 ");
		jp.add(jcb, "cell 0 0 ");
		jp.add(kjqh, "cell 0 0 ");
		jp.add(jcbqh, "cell 0 0 ");
		jp.add(kjlsBtn, "cell 0 0 ");
		jp.add(jurl, "cell 0 1 6, span, grow");
		jp.add(queryBtn, "cell 0 2");
		jp.add(resetBtn, "cell 0 2");
		jp.add(saveBtn, "cell 0 2");
		jp.add(jgl, "cell 0 3");
		jp.add(js, "cell 0 4");
		jga.setEditable(false);
		jga.setLineWrap(true);
		
		jcb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBoxItem item = (ComboBoxItem)jcb.getSelectedItem();
				jurl.setText(item.getKey());
			}
		});
		
		queryBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					queryBtn.setEnabled(false);
					if(jurl.getText().isEmpty() || jurl.getText().equals("")){
						ComboBoxItem item = (ComboBoxItem)jcb.getSelectedItem();
						jurl.setText(item.getKey());
					}
					if(!curUrl.equalsIgnoreCase(jurl.getText())){
						updateInfo(); // 更新开奖信息
						curUrl = jurl.getText();
					}
					showInfo();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				queryBtn.setEnabled(true);
			}
		});

		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveInfo(); // 更新开奖信息
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		resetBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jcb.setSelectedIndex(0);
					jcbqh.setSelectedIndex(0);
					jurl.setText(url);
					jga.setText("");
					curUrl = "";
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		kjlsBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					OpenUrl.openURL(hsurl);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.add(jp, BorderLayout.CENTER);
		this.setSize(380, 500);
		openDia = new FileDialog(this, "保存", FileDialog.SAVE);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

	public void updateInfo() {
		// 定义即将访问的链接
		String url = jurl.getText();
		// 访问链接并获取页面内容
		String content = MarkSix.SendGet(url);
		// 根据正则匹配指定内容
		Pattern pattern;
		Matcher matcher;
		String marksix;
		// 匹配标题
		pattern = Pattern.compile("<!--msg-->(.+?)<!--endmsg-->");
		matcher = pattern.matcher(content);
		if (matcher.find()) {
			marksix = matcher.group(1);
		} else {
			return;
		}
		if (marksix.indexOf("<bR>") > 0) {
			marksix = marksix.replace("<bR>", " \n");
		} else {
			marksix = marksix.replace("<br>", " \n");
		}
		String[] rets = marksix.split(" \n");

		records.clear();

		for (String s : rets) {
			s = s.replace("：", " ");
			String[] rds = s.split(" ");
			String no = rds[0].replace("期", "");
			if (!isNumeric(no))
				continue;
			String date = rds[1];
			String first = rds[2];
			String second = rds[3];
			String third = rds[4];
			String forth = rds[5];
			String fifth = rds[6];
			String sixth = rds[7];
			String seventh = rds[8].substring(1, 3);
			Record rd = new Record(no, date, first, second, third, forth, fifth, sixth, seventh);
			records.add(rd);
		}
		// JOptionPane.showMessageDialog(this, "数据更新成功！");
	}

	public void showInfo() {
		jga.setText("");
		jga.append(" 期号       日期          星期        一     二     三    四    五     六   特码  \n");
		String qh = (String)jcbqh.getSelectedItem();
		if(!qh.equals("全部")){
			boolean isExist = false;
			for (Record rd : records) {
				if(rd.getNo().equals(qh)){
					jga.append(rd.toString());
					jga.append("\n");
					isExist = true;
					break;
				}
			}
			if(!isExist){
				jga.append(qh +  "期未开奖！--------");
				JOptionPane.showMessageDialog(this, qh + "期未开奖！");
			}
			return;
		}
		for (Record rd : records) {
			jga.append(rd.toString());
			jga.append("\n");
		}
	}

	public void saveInfo() {
		if (jga.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "无数据，请先更新后再保存。");
			return;
		}

		openDia.setFile(filename);
		;
		openDia.setVisible(true);
		String dirPath = openDia.getDirectory();
		String fileName = openDia.getFile();
		filename = fileName;

		if (dirPath == null || fileName == null)
			return;
		// 写入本地
		try {
			MarkSix.writeIntoFile(MarkSix.changeCharset(jga.getText(), "GB2312", "GBK"), dirPath + fileName, false);
			System.out.println("开奖记录成功写入文件" + dirPath + fileName);
			JOptionPane.showMessageDialog(this, "记录保存成功！\n已写入文件" + dirPath + fileName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "记录保存失败。\n错误信息：" + e.getMessage());
		}
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			//System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		new MainForm();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
