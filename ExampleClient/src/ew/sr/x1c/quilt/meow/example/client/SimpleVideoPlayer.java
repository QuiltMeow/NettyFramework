package ew.sr.x1c.quilt.meow.example.client;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import lombok.Getter;

public class SimpleVideoPlayer extends JFrame {

    private final Image icon;
    private MediaPlayer player;

    @Getter
    private final String file;

    public SimpleVideoPlayer(String file) {
        icon = new ImageIcon(getClass().getClassLoader().getResource("ew/sr/x1c/image/Icon.png")).getImage();
        initComponents();

        this.file = file;
        loadVideo();
    }

    private void loadVideo() {
        JFXPanel fxPanel = new JFXPanel();
        File videoFile = new File(file);
        Media media = new Media(videoFile.toURI().toString());
        player = new MediaPlayer(media);
        MediaView view = new MediaView(player);

        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        DoubleProperty width = view.fitWidthProperty();
        DoubleProperty height = view.fitHeightProperty();
        width.bind(Bindings.selectDouble(view.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(view.sceneProperty(), "height"));
        view.setPreserveRatio(true);

        root.getChildren().add(view);
        fxPanel.setScene(scene);
        videoPanel.setLayout(new BorderLayout());
        videoPanel.add(fxPanel, BorderLayout.CENTER);

        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        videoPanel = new javax.swing.JPanel();

        setTitle("簡易影片播放器");
        setIconImage(icon);
        setName("VideoFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout videoPanelLayout = new javax.swing.GroupLayout(videoPanel);
        videoPanel.setLayout(videoPanelLayout);
        videoPanelLayout.setHorizontalGroup(
            videoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
        videoPanelLayout.setVerticalGroup(
            videoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(videoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(videoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        player.stop();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel videoPanel;
    // End of variables declaration//GEN-END:variables
}
