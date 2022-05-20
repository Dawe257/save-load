package com.dzhenetl;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        GameProgress game1 = new GameProgress(15, 2,3 ,2.5);
        GameProgress game2 = new GameProgress(34, 4,6 ,5.8);
        GameProgress game3 = new GameProgress(78, 6,11 ,11.2);
        System.out.println(game2);

        String savePath = "/Users/dzhenetl/Games/savegames/";
        saveGame(savePath + "save1.dat", game1);
        saveGame(savePath + "save2.dat", game2);
        saveGame(savePath + "save3.dat", game3);
        zipFiles(savePath + "save.zip", savePath + "save1.dat", savePath + "save2.dat");
        openZip(savePath + "save.zip", savePath);
        GameProgress newGame = openProgress(savePath + "save2.dat");
        System.out.println(newGame);
    }

    public static void saveGame(String path, GameProgress game) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(game);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String destPath, String... files) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(destPath))) {
            for (String path : files) {
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(file.getName());
                zout.putNextEntry(entry);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                zout.write(buffer);
                zout.flush();
                zout.closeEntry();
                inputStream.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String sourcePath, String unpackPath) {

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourcePath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File unpackedFile = new File(unpackPath, entry.getName());
                FileOutputStream outputStream = new FileOutputStream(unpackedFile);
                while (zipInputStream.available() > 0) {
                    outputStream.write(zipInputStream.read());
                }
                outputStream.flush();
                zipInputStream.closeEntry();
                outputStream.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static GameProgress openProgress(String saveFilePath) {
        
        GameProgress result = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(saveFilePath))) {
            result = (GameProgress) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
