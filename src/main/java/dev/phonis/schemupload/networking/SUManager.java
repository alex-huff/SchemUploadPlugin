package dev.phonis.schemupload.networking;

import dev.phonis.schemupload.SchemUpload;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SUManager {

    public static void sendToPlayer(Player player, SUPacket packet) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeByte(packet.packetID());
            packet.toBytes(dos);

            byte[] bytes = baos.toByteArray();

            player.sendPluginMessage(SchemUpload.instance, SchemUpload.suChannel, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
