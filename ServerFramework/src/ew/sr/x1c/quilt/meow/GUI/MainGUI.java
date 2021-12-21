/* 程式入口點
 * 棉被家族範例專案 : 服務端封裝框架
 * 部分想法來源 : SX
 */
package ew.sr.x1c.quilt.meow.GUI;

import ew.sr.x1c.quilt.meow.command.ConsoleCommandSender;
import ew.sr.x1c.quilt.meow.constant.Constant;
import ew.sr.x1c.quilt.meow.schedule.TaskScheduler.ScheduleTimer;
import ew.sr.x1c.quilt.meow.server.GeneralManager;
import ew.sr.x1c.quilt.meow.server.Entry;
import ew.sr.x1c.quilt.meow.util.OSUtil;
import ew.sr.x1c.quilt.meow.util.SystemUtil;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class MainGUI extends JFrame {

    private final Image icon;
    private final GeneralManager entry;

    public MainGUI() {
        icon = new ImageIcon(getClass().getClassLoader().getResource("ew/sr/x1c/image/Icon.png")).getImage();
        initComponents();
        labelOS.setText("作業系統 : " + OSUtil.getOperatingSystemType().name());

        ScheduleTimer.getInstance().start();
        ScheduleTimer.getInstance().register(() -> {
            txtAreaStatus.setText(SystemUtil.getRuntimeData());
            if (GeneralManager.getInstance() != null && GeneralManager.getInstance().getClientStorage() != null) {
                labelClientCount.setText("客戶端數量 : " + GeneralManager.getInstance().getClientStorage().getOnlineCount());
            }
        }, Constant.GUI_STATUS_UPDATE_INTERVAL);

        File logFolder = new File("./log");
        logFolder.mkdir();

        Handler consoleHandler = new ConsoleHandler();
        Handler txtAreaHandler = new TextAreaLogHandler(new TextAreaOutputStream(txtAreaOutput));
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler("./log/" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".log");
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException ex) {
        }
        if (fileHandler == null) {
            entry = new Entry(consoleHandler, txtAreaHandler);
        } else {
            entry = new Entry(consoleHandler, txtAreaHandler, fileHandler);
        }
        entry.getLogger().log(Level.INFO, "伺服器名稱 : {0} 版本 : {1}", new Object[]{
            entry.getName(), entry.getVersion()
        });
        setTitle(entry.getName() + " 版本 : " + entry.getVersion());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnStartServer = new javax.swing.JButton();
        spStatus = new javax.swing.JScrollPane();
        txtAreaStatus = new javax.swing.JTextArea();
        labelStatus = new javax.swing.JLabel();
        btnDisconnectAll = new javax.swing.JButton();
        labelClientCount = new javax.swing.JLabel();
        btnStopServer = new javax.swing.JButton();
        spOutput = new javax.swing.JScrollPane();
        txtAreaOutput = new javax.swing.JTextArea();
        labelOutput = new javax.swing.JLabel();
        labelCommand = new javax.swing.JLabel();
        txtCommand = new javax.swing.JTextField();
        btnCommand = new javax.swing.JButton();
        labelOS = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("服務端");
        setIconImage(icon);
        setName("MainGUI"); // NOI18N
        setResizable(false);

        btnStartServer.setText("啟動伺服器");
        btnStartServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartServerActionPerformed(evt);
            }
        });

        txtAreaStatus.setEditable(false);
        txtAreaStatus.setColumns(20);
        txtAreaStatus.setLineWrap(true);
        txtAreaStatus.setRows(5);
        txtAreaStatus.setWrapStyleWord(true);
        spStatus.setViewportView(txtAreaStatus);

        labelStatus.setFont(new java.awt.Font("微軟正黑體", 0, 18)); // NOI18N
        labelStatus.setText("系統狀態");

        btnDisconnectAll.setText("斷線所有客戶端");
        btnDisconnectAll.setEnabled(false);
        btnDisconnectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectAllActionPerformed(evt);
            }
        });

        labelClientCount.setText("客戶端數量 : 0");

        btnStopServer.setText("關閉伺服器");
        btnStopServer.setEnabled(false);
        btnStopServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopServerActionPerformed(evt);
            }
        });

        txtAreaOutput.setEditable(false);
        txtAreaOutput.setColumns(20);
        txtAreaOutput.setLineWrap(true);
        txtAreaOutput.setRows(5);
        txtAreaOutput.setWrapStyleWord(true);
        spOutput.setViewportView(txtAreaOutput);

        labelOutput.setFont(new java.awt.Font("微軟正黑體", 0, 18)); // NOI18N
        labelOutput.setText("訊息輸出");

        labelCommand.setText("指令執行");

        txtCommand.setEnabled(false);

        btnCommand.setText("執行");
        btnCommand.setEnabled(false);
        btnCommand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommandActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelStatus)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(spStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelOutput)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(spOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelCommand)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCommand, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCommand)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(368, 368, 368)
                                .addComponent(labelClientCount))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnStartServer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnStopServer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDisconnectAll)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelOS)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelStatus)
                    .addComponent(labelOutput))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addComponent(spStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCommand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCommand)
                    .addComponent(btnCommand))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStartServer)
                    .addComponent(btnStopServer)
                    .addComponent(btnDisconnectAll)
                    .addComponent(labelClientCount)
                    .addComponent(labelOS))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartServerActionPerformed
        btnStartServer.setEnabled(false);
        Thread serverStartThread = new Thread(() -> {
            entry.start();
            btnStopServer.setEnabled(true);
            btnDisconnectAll.setEnabled(true);

            txtCommand.setEnabled(true);
            btnCommand.setEnabled(true);
        });
        serverStartThread.setUncaughtExceptionHandler((thread, th) -> {
            entry.getLogger().log(Level.SEVERE, "服務端初始化失敗", th);
            JOptionPane.showMessageDialog(this, "服務端初始化失敗");
            System.exit(Constant.EXIT_WITH_ERROR);
        });
        serverStartThread.start();
    }//GEN-LAST:event_btnStartServerActionPerformed

    private void btnDisconnectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectAllActionPerformed
        entry.getLogger().log(Level.INFO, "斷線所有客戶端");
        new Thread(() -> {
            entry.getClientStorage().disconnectAll();
        }).start();
    }//GEN-LAST:event_btnDisconnectAllActionPerformed

    private void btnStopServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopServerActionPerformed
        btnStopServer.setEnabled(false);
        entry.stop();
    }//GEN-LAST:event_btnStopServerActionPerformed

    public static boolean isNullOrBlank(String input) {
        return input == null || input.trim().length() == 0;
    }

    private void btnCommandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommandActionPerformed
        String command = txtCommand.getText();
        if (isNullOrBlank(command)) {
            JOptionPane.showMessageDialog(this, "請輸入執行指令");
            return;
        }

        entry.getLogger().log(Level.INFO, "後台指令執行 : {0}", command);
        new Thread(() -> {
            entry.getPluginManager().dispatchCommand(ConsoleCommandSender.getInstance(), command);
        }).start();
    }//GEN-LAST:event_btnCommandActionPerformed

    public static void initLookAndFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
    }

    public static void main(String args[]) {
        initLookAndFeel();
        EventQueue.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCommand;
    private javax.swing.JButton btnDisconnectAll;
    private javax.swing.JButton btnStartServer;
    private javax.swing.JButton btnStopServer;
    private javax.swing.JLabel labelClientCount;
    private javax.swing.JLabel labelCommand;
    private javax.swing.JLabel labelOS;
    private javax.swing.JLabel labelOutput;
    private javax.swing.JLabel labelStatus;
    private javax.swing.JScrollPane spOutput;
    private javax.swing.JScrollPane spStatus;
    private javax.swing.JTextArea txtAreaOutput;
    private javax.swing.JTextArea txtAreaStatus;
    private javax.swing.JTextField txtCommand;
    // End of variables declaration//GEN-END:variables
}
