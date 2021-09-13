package dev.phonis.schemupload.schematica;

import dev.phonis.schemupload.networking.StartUpload;

public class SchemBuilder {

    private final int length;
    private final int startX;
    private final int startY;
    private final int startZ;
    private final byte[] schemData;
    private int current = 0;

    public SchemBuilder(int length, int startX, int startY, int startZ) {
        this.length = length;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.schemData = new byte[this.length];
    }

    public SchemBuilder(StartUpload startUpload) {
        this(startUpload.length, startUpload.startX, startUpload.startY, startUpload.startZ);
    }

    public void addData(byte[] data) {
        System.arraycopy(data, 0, this.schemData, this.current, data.length);

        this.current += data.length;
    }

    public boolean isReady() {
        return this.current == this.length;
    }

    public Schem getSchem() {
        return new Schem(this.startX, this.startY, this.startZ, this.schemData);
    }

    public String getProgress() {
        return "(" + this.current + "/" + this.length + ") bytes";
    }

}
