package com.coffice.test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class MainApp {
    private static void createAndShowGUI() {
            JFrame mainFrame = new JFrame("测试主窗体");
            JLabel jLabel = new JLabel("这是一个标签", SwingConstants.CENTER);
            jLabel.setPreferredSize(new Dimension(400, 300));
mainFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

            mainFrame.pack();
            TrayFactory.createTray(mainFrame, "D:\\dz12345\\03编码\\oa2\\webapp\\document\\red.jpg", "你好，Java!\nHello,Java!");
            mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
            String lf = UIManager.getSystemLookAndFeelClassName();
            try {
                    UIManager.setLookAndFeel(lf);
            } 
            catch(Exception e) {
            }
            
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                    createAndShowGUI();
                            }
});
    }    
}
