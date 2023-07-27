package gui;

import com.wind.meditor.utils.ShellCmdUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import util.FileIOUtils;
import util.FileUtils;
import util.Log;
import util.TextUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Stream;

public class IpaUI {
    public static JFrame frmIpa;
    private final JList<String> jList;
    private final DefaultListModel<String> modlist;

    public IpaUI(String arg) {
        // 窗口框架
        frmIpa = new JFrame();
        frmIpa.setTitle("快手APP代码定位工具");
        frmIpa.setBounds(600, 300, 800, 600);
        frmIpa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 面板1
        JPanel panel = new JPanel();
        frmIpa.getContentPane().add(panel, BorderLayout.NORTH);
        JButton injectDeclaration = new JButton("注入注释模板");
        panel.add(injectDeclaration);
        injectDeclaration.addActionListener(e -> {
            try {

                String property = System.getProperty("user.home");

                String path = property + "/Library/Application Support/Google";
                File pathFile = new File(path);
                File[] studioFile = pathFile.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().startsWith("AndroidStudio");
                    }
                });

                for (File file : studioFile) {
                    InputStream deStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("coronaDeclaration.xml");
                    InputStream styleStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("kuaishou.xml");
                    File outFile = new File(file, "/templates/coronaDeclaration.xml");
                    FileIOUtils.writeFileFromIS(outFile, deStream);

                    File styleOutFile = new File(file, "/codestyles/kuaishou.xml");
                    FileIOUtils.writeFileFromIS(styleOutFile, styleStream);
                }
                appendText("注入完成");
            } catch (Exception exception) {
                exception.printStackTrace();
                appendText("注入失败");
            }

        });
        appendVirus(arg, panel);
        appendTrace(arg, panel);
        // 可滚动面板
        JScrollPane scrollPane = new JScrollPane();
        frmIpa.getContentPane().add(scrollPane, BorderLayout.CENTER);
        modlist = new DefaultListModel<String>();
        jList = new JList<String>(modlist);
        appendText("virus  当前感染模块: [" + arg + "]");
        scrollPane.setViewportView(jList);
        Log.setTextBack((tag, msg) -> {
            appendText(tag + "  " + msg);
            return null;
        });
        JPanel panelTips = new JPanel();
        JTextArea tipsText = new JTextArea();
        panelTips.add(tipsText);
        tipsText.setText("提示信息");
        Log.setTextTips((tag, msg) -> {
            tipsText.setText(tag + "  " + msg);
            return null;
        });
        frmIpa.getContentPane().add(panelTips, BorderLayout.SOUTH);
        //这个最好放在最后，否则会出现视图问题。
        frmIpa.setVisible(true);
    }

    private void appendTrace(String arg, JPanel panel) {
        JButton button = new JButton("选择注入TraceAPK");
        // 监听button的选择路径
        ActionListener apk = e -> {
            // 显示打开的文件对话框
            JFileChooser jfc = new JFileChooser();
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("感染APK路径.apk", "apk");
            jfc.addChoosableFileFilter(filter);
            int state = jfc.showOpenDialog(frmIpa);
            if (state == jfc.APPROVE_OPTION) {
                try {
                    // 使用文件类获取选择器选择的文件
                    File file = jfc.getSelectedFile();
                    new Thread(() -> virusApp(file, arg, "1")).start();
                } catch (Exception e2) {
//				JPanel panel3 = new JPanel();
//				JOptionPane.showMessageDialog(panel3, "没有选中任何文件", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }

        };
        button.addActionListener(apk);
        panel.add(button);
    }

    private void appendVirus(String arg, JPanel panel) {
        JButton button = new JButton("选择感染APK");
        JButton forwardButton = new JButton("forward 端口");
        panel.add(forwardButton);
        forwardButton.addActionListener(e -> {
            try {
                String cmd = "adb forward tcp:8000 localabstract:app_hook";
                appendText("执行命令 " + cmd);
                String s = ShellCmdUtil.execCmd(cmd, null);
                if (TextUtils.isEmpty(s)) {
                    s = "成功!";
                }
                appendText("adb forward 端口 " + s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });
        // 监听button的选择路径
        ActionListener apk = e -> {
            // 显示打开的文件对话框
            JFileChooser jfc = new JFileChooser();
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("感染APK路径.apk", "apk");
            jfc.addChoosableFileFilter(filter);
            jfc.showOpenDialog(frmIpa);
            int state = jfc.showOpenDialog(frmIpa);
            if (state == jfc.APPROVE_OPTION) {
                try {
                    // 使用文件类获取选择器选择的文件
                    File file = jfc.getSelectedFile();
                    new Thread(() -> virusApp(file, arg, "0")).start();
                } catch (Exception e2) {
//				JPanel panel3 = new JPanel();
//				JOptionPane.showMessageDialog(panel3, "没有选中任何文件", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }

        };
        button.addActionListener(apk);
        panel.add(button);
    }

    private void appendText(String msg) {
        modlist.addElement(msg);
        jList.setSelectedIndex(modlist.getSize() - 1);
        jList.ensureIndexIsVisible(jList.getSelectedIndex());
    }

    private void virusApp(File file, String arg, String isTrace) {
        ArrayList<String> commandList = new ArrayList<String>();
        commandList.add("-host");
        commandList.add(file.getAbsolutePath());
        commandList.add("-virus");
        commandList.add(arg);
        commandList.add("-debug");
        commandList.add("1");
        commandList.add("-dex");
        commandList.add("1");
        commandList.add("-install");
        commandList.add("1");
        commandList.add("-isTrace");
        commandList.add(isTrace);
        String[] strings = new String[commandList.size()];
        XpRootMainCore.INSTANCE.call((String[]) commandList.toArray(strings));
    }

}
