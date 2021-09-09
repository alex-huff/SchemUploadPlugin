package dev.phonis.schemupload.schematica;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.LegacyWorldData;
import dev.phonis.schemupload.SchemUpload;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Schem {

    private final int startX;
    private final int startY;
    private final int startZ;
    private final byte[] schemData;

    public Schem(int startX, int startY, int startZ, byte[] schemData) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.schemData = schemData;
    }

    public void pasteInWorld(Player player) {
        try {
            org.bukkit.World world = player.getWorld();

            player.sendMessage(SchemUpload.prefix + "Pasting schematica in world '" + world.getName() + ".'");
            this.pasteSchem(world);
            player.sendMessage(SchemUpload.prefix + "Done pasting schematica.");
        } catch (Throwable e) {
            e.printStackTrace();
            player.sendMessage(SchemUpload.prefix + "Failed to paste schematica.");
        }
    }

    private void pasteSchem(org.bukkit.World world) throws Throwable {
        if (!(Bukkit.getPluginManager().getPlugin("WorldEdit") instanceof WorldEditPlugin)) {
            throw new Exception("Invalid WorldEdit.");
        }

        ClipboardFormat format = ClipboardFormat.SCHEMATIC;
        ClipboardReader reader;
        ByteArrayInputStream bais = new ByteArrayInputStream(schemData);

        try {
            reader = format.getReader(bais);
        } catch (IOException e) {
            throw new Exception("IOException during loading of schematic.");
        }

        Clipboard clipboard;

        try {
            clipboard = reader.read(LegacyWorldData.getInstance());

            bais.close();
        } catch (IOException e) {
            throw new Exception("IOException during load to clipboard.");
        }

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((World) new BukkitWorld(world), Integer.MAX_VALUE);
        Operation operation = new ClipboardHolder(
            clipboard,
            LegacyWorldData.getInstance()
        ).createPaste(editSession, LegacyWorldData.getInstance()).to(new Vector(this.startX, this.startY, this.startZ)).build();

        editSession.enableQueue();
        Operations.complete(operation);
        Operations.complete(editSession.commit());
        editSession.flushQueue();
    }

}
