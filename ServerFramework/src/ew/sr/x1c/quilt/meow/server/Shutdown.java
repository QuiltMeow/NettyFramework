package ew.sr.x1c.quilt.meow.server;

import com.google.common.collect.Lists;
import ew.sr.x1c.quilt.meow.constant.Constant;
import ew.sr.x1c.quilt.meow.database.DatabaseConnection;
import ew.sr.x1c.quilt.meow.schedule.TaskScheduler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;

public class Shutdown implements Runnable {

    private void disableAllPlugin() {
        Lists.reverse(new ArrayList<>(GeneralManager.getInstance().getPluginManager().getPluginList())).forEach((plugin) -> {
            try {
                plugin.onDisable();
                for (Handler handler : plugin.getLogger().getHandlers()) {
                    handler.close();
                }
            } catch (Throwable th) {
                GeneralManager.getInstance().getLogger().log(Level.SEVERE, "關閉插件時發生例外狀況 : " + plugin.getDescription().getName(), th);
            }
        });
    }

    @Override
    public void run() {
        GeneralManager.getInstance().getClientStorage().disconnectAll();
        disableAllPlugin();
        if (DatabaseConnection.hasConnection()) {
            try {
                DatabaseConnection.closeAll();
            } catch (SQLException ex) {
                GeneralManager.getInstance().getLogger().log(Level.WARNING, "關閉資料庫連線時發生例外狀況", ex);
            }
        }

        GeneralManager.getInstance().getServer().stop();
        for (Handler handler : GeneralManager.getInstance().getLogger().getHandlers()) {
            handler.close();
        }
        TaskScheduler.ScheduleTimer.getInstance().stop();
        System.exit(Constant.EXIT_SUCCESS);
    }
}
