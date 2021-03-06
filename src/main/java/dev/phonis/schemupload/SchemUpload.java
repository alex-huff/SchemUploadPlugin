package dev.phonis.schemupload;

import dev.phonis.schemupload.networking.SUListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SchemUpload extends JavaPlugin {

    public static SchemUpload instance;
    public static final String suChannel = "schemupload:main";
    public static final String prefix = ChatColor.GRAY + "(" + ChatColor.AQUA + "SchemUpload" + ChatColor.GRAY + ") " + ChatColor.RESET;

    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerIncomingPluginChannel(this, SchemUpload.suChannel, SUListener.INSTANCE);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, SchemUpload.suChannel);
    }

    @Override
    public void onDisable() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, SchemUpload.suChannel, SUListener.INSTANCE);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, SchemUpload.suChannel);
    }

}