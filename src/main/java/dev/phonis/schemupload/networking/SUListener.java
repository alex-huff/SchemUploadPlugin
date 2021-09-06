package dev.phonis.schemupload.networking;

import dev.phonis.schemupload.schematica.Schem;
import dev.phonis.schemupload.schematica.SchemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class SUListener implements PluginMessageListener {

    public static final SUListener INSTANCE = new SUListener();

    private final Map<UUID, SchemBuilder> builderMap = new TreeMap<>();

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            byte packetId = dis.readByte();

            switch (packetId) {
                case Packet.Out.startUploadID:
                    this.handlePacket(s, player, StartUpload.fromBytes(dis));

                    break;
                case Packet.Out.uploadSegmentID:
                    this.handlePacket(s, player, UploadSegment.fromBytes(dis));

                    break;
                default:
                    System.out.println("Unrecognised packet.");

                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePacket(String s, Player player, SUPacket packet) {
        if (packet instanceof StartUpload) {
            StartUpload startUpload = (StartUpload) packet;
            SchemBuilder schemBuilder = new SchemBuilder(startUpload.length, startUpload.startX, startUpload.startY, startUpload.startZ);

            player.sendMessage("Uploading schem of size " + startUpload.length + " bytes.");
            this.builderMap.put(player.getUniqueId(), schemBuilder);
            player.sendMessage(schemBuilder.getProgress());
        } else if (packet instanceof UploadSegment) {
            UploadSegment uploadSegment = (UploadSegment) packet;
            SchemBuilder schemBuilder = this.builderMap.get(player.getUniqueId());

            if (schemBuilder == null) {
                return;
            }

            schemBuilder.addData(uploadSegment.payload);
            player.sendMessage(schemBuilder.getProgress());

            if (schemBuilder.isReady()) {
                Schem schem = schemBuilder.getSchem();

                schem.pasteInWorld(player);
            }
        }
    }

}
