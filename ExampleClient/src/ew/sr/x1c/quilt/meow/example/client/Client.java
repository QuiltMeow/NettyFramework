package ew.sr.x1c.quilt.meow.example.client;

import ew.sr.x1c.quilt.meow.example.client.netty.NettyClient;
import ew.sr.x1c.quilt.meow.example.client.packet.data.PacketLittleEndianWriter;
import ew.sr.x1c.quilt.meow.example.client.packet.header.SendPacketOPCode;
import java.awt.EventQueue;
import java.awt.Image;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import lombok.Getter;
import lombok.Setter;

public class Client extends JFrame {

    private final Image icon;

    @Getter
    private static Client instance;

    @Getter
    private final Logger logger;

    @Getter
    private NettyClient connection;

    @Getter
    @Setter
    private Session session;

    @Getter
    private String name;

    private void registerLogHandler(Handler... handlerList) {
        for (Handler handler : handlerList) {
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
        }
    }

    public void setClientName(String name) {
        this.name = name;
        labelClientIdentity.setText("客戶端 ID : " + name);
    }

    private Client() {
        icon = new ImageIcon(getClass().getClassLoader().getResource("ew/sr/x1c/image/Icon.png")).getImage();
        initComponents();
        instance = this;

        logger = Logger.getLogger(Client.class.getName());
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        registerLogHandler(new ConsoleHandler(), new TextAreaLogHandler(new TextAreaOutputStream(txtAreaOutput)));
    }

    public static boolean isNullOrBlank(String input) {
        return input == null || input.trim().length() == 0;
    }

    private void enableFunction(boolean enable) {
        btnDisconnect.setEnabled(enable);
        btnEcho.setEnabled(enable);
        btnRequestRandom.setEnabled(enable);
        btnFileDownload.setEnabled(enable);
        txtMessage.setEnabled(enable);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnFileDownload = new javax.swing.JButton();
        btnRequestRandom = new javax.swing.JButton();
        btnEcho = new javax.swing.JButton();
        labelOutput = new javax.swing.JLabel();
        spOutput = new javax.swing.JScrollPane();
        txtAreaOutput = new javax.swing.JTextArea();
        txtMessage = new javax.swing.JTextField();
        labelMessage = new javax.swing.JLabel();
        labelConnectInfo = new javax.swing.JLabel();
        labelHost = new javax.swing.JLabel();
        txtHost = new javax.swing.JTextField();
        labelPort = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        btnConnect = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();
        labelTestExample = new javax.swing.JLabel();
        labelClientIdentity = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("範例客戶端");
        setIconImage(icon);
        setName("MainGUI"); // NOI18N
        setResizable(false);

        btnFileDownload.setText("File Download");
        btnFileDownload.setEnabled(false);
        btnFileDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileDownloadActionPerformed(evt);
            }
        });

        btnRequestRandom.setText("Random");
        btnRequestRandom.setEnabled(false);
        btnRequestRandom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRequestRandomActionPerformed(evt);
            }
        });

        btnEcho.setText("Echo");
        btnEcho.setEnabled(false);
        btnEcho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEchoActionPerformed(evt);
            }
        });

        labelOutput.setFont(new java.awt.Font("微軟正黑體", 0, 18)); // NOI18N
        labelOutput.setText("訊息輸出");

        txtAreaOutput.setEditable(false);
        txtAreaOutput.setColumns(20);
        txtAreaOutput.setLineWrap(true);
        txtAreaOutput.setRows(5);
        txtAreaOutput.setWrapStyleWord(true);
        spOutput.setViewportView(txtAreaOutput);

        txtMessage.setEnabled(false);

        labelMessage.setText("訊息輸入");

        labelConnectInfo.setFont(new java.awt.Font("微軟正黑體", 0, 18)); // NOI18N
        labelConnectInfo.setText("連線資訊");

        labelHost.setText("主機");

        txtHost.setText("127.0.0.1");

        labelPort.setText("端口");

        txtPort.setText("8093");

        btnConnect.setText("連線");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        btnDisconnect.setText("中斷連線");
        btnDisconnect.setEnabled(false);
        btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectActionPerformed(evt);
            }
        });

        labelTestExample.setFont(new java.awt.Font("微軟正黑體", 0, 18)); // NOI18N
        labelTestExample.setText("測試範例");

        labelClientIdentity.setText("客戶端 ID : 連線後取得");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelMessage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelOutput)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(spOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelConnectInfo)
                            .addComponent(labelTestExample)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(labelHost)
                                            .addComponent(labelPort))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPort)
                                            .addComponent(txtHost)))
                                    .addComponent(btnRequestRandom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnFileDownload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEcho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDisconnect))))))
                    .addComponent(labelClientIdentity))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelOutput)
                    .addComponent(labelConnectInfo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(spOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelMessage)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelHost))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelPort))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnConnect)
                            .addComponent(btnDisconnect))
                        .addGap(18, 18, 18)
                        .addComponent(labelTestExample)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEcho)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRequestRandom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFileDownload)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelClientIdentity)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFileDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileDownloadActionPerformed
        logger.log(Level.INFO, "發送檔案下載請求");
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(SendPacketOPCode.FILE_DOWNLOAD.getHeader());
        session.write(mplew.getPacket());
    }//GEN-LAST:event_btnFileDownloadActionPerformed

    private void btnRequestRandomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRequestRandomActionPerformed
        int max;
        try {
            max = Integer.parseInt(txtMessage.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "數值輸入錯誤");
            return;
        }
        if (max <= 0) {
            JOptionPane.showMessageDialog(this, "最大值必須為正數");
            return;
        }

        logger.log(Level.INFO, "發送 Random 請求");
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(SendPacketOPCode.RANDOM_REQUEST.getHeader());
        mplew.writeInt(max);
        session.write(mplew.getPacket());
    }//GEN-LAST:event_btnRequestRandomActionPerformed

    private void btnEchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEchoActionPerformed
        String message = txtMessage.getText();
        if (isNullOrBlank(message)) {
            JOptionPane.showMessageDialog(this, "請輸入訊息");
            return;
        }

        logger.log(Level.INFO, "發送 Echo 請求");
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(SendPacketOPCode.ECHO_MESSAGE.getHeader());
        mplew.writeLengthAsciiString(message);
        session.write(mplew.getPacket());
    }//GEN-LAST:event_btnEchoActionPerformed

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        String host = txtHost.getText();
        if (isNullOrBlank(host)) {
            JOptionPane.showMessageDialog(this, "請輸入連線主機");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(txtPort.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "目標端口輸入數值錯誤");
            return;
        }
        if (port <= 0 || port > 65535) {
            JOptionPane.showMessageDialog(this, "目標端口輸入範圍錯誤");
            return;
        }

        txtHost.setEnabled(false);
        txtPort.setEnabled(false);
        btnConnect.setEnabled(false);
        logger.log(Level.INFO, "正在連線到伺服器");
        new Thread(() -> {
            connection = new NettyClient(host, port);
            connection.start();

            logger.log(Level.INFO, "連線建立完成");
            enableFunction(true);
        }).start();
    }//GEN-LAST:event_btnConnectActionPerformed

    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        enableFunction(false);
        logger.log(Level.INFO, "正在中斷連線");
        new Thread(() -> connection.stop()).start();
    }//GEN-LAST:event_btnDisconnectActionPerformed

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
            new Client().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnEcho;
    private javax.swing.JButton btnFileDownload;
    private javax.swing.JButton btnRequestRandom;
    private javax.swing.JLabel labelClientIdentity;
    private javax.swing.JLabel labelConnectInfo;
    private javax.swing.JLabel labelHost;
    private javax.swing.JLabel labelMessage;
    private javax.swing.JLabel labelOutput;
    private javax.swing.JLabel labelPort;
    private javax.swing.JLabel labelTestExample;
    private javax.swing.JScrollPane spOutput;
    private javax.swing.JTextArea txtAreaOutput;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextField txtMessage;
    private javax.swing.JTextField txtPort;
    // End of variables declaration//GEN-END:variables
}
