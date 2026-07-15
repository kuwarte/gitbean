package com.kuwarte.gitbean.core;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ObjectStore {
    private static final Path OBJECTS_DIR = Paths.get(".gitbean", "objects");

    public static byte[] buildObjectContent(String type, byte[] content) {
        String header = type + " " + content.length + "\0";
        byte[] headerBytes = header.getBytes();

        byte[] full = new byte[headerBytes.length + content.length];
        System.arraycopy(headerBytes, 0, full, 0, headerBytes.length);
        System.arraycopy(content, 0, full, headerBytes.length, content.length);

        return full;
    }

    public static String sha1Hex(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(data);

            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 not available", e);
        }
    }

    public static String writeObject(byte[] fullContent) throws IOException {
        String hash = sha1Hex(fullContent);

        String dirName = hash.substring(0, 2);
        String fileName = hash.substring(2);

        Path objectDir = OBJECTS_DIR.resolve(dirName);
        Path objectFile = objectDir.resolve(fileName);

        Files.createDirectories(objectDir);

        if (Files.exists(objectFile)) {
            return hash;
        }

        try (
                OutputStream fos = Files.newOutputStream(objectFile);
                DeflaterOutputStream dos = new DeflaterOutputStream(fos, new Deflater(Deflater.BEST_COMPRESSION))) {

            dos.write(fullContent);
        }

        return hash;
    }

    public static byte[] readObject(String hash) throws IOException {
        String dirName = hash.substring(0, 2);
        String fileName = hash.substring(2);

        Path objectFile = OBJECTS_DIR.resolve(dirName).resolve(fileName);

        if (!Files.exists(objectFile)) {
            throw new IOException("object not found: " + hash);
        }

        try (InputStream fis = Files.newInputStream(objectFile)) {
            InflaterInputStream iis = new InflaterInputStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = iis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            return baos.toByteArray();
        }
    }

}
