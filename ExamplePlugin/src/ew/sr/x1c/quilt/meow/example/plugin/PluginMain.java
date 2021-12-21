package ew.sr.x1c.quilt.meow.example.plugin;

import ew.sr.x1c.quilt.meow.example.plugin.listener.PacketListener;
import ew.sr.x1c.quilt.meow.plugin.Plugin;

public class PluginMain extends Plugin {

    @Override
    public void onEnable() {
        getPluginManager().registerListener(this, new PacketListener());
    }
}
