package ru.frostdelta.forcescreens;

import net.minecraft.launchwrapper.Launch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassPathChecker extends Thread {

    @Override
    public void run() {
        try {
            List<URL> sources = Launch.classLoader.getSources();
            for (URL u : sources) {
                if (u.toURI().toString().startsWith("file")) {
                    File file = new File(u.toURI());
                    if (file.isFile()) {
                        if (file.getName().endsWith("zip") || file.getName().endsWith("jar")) {
                            if (getZipFileEntriesSize(file) != getZipInputStreamEntriesSize(file)) {
                                Utils.killMinecraft();
                            }
                        }
                    }
                } else {
                    Utils.killMinecraft();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private long getZipFileEntriesSize(File file) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(file);
        Enumeration e = zip.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            InputStream is = zip.getInputStream(entry);
            for (int c = is.read(); c != -1; c = is.read()) {
                out.write(c);
            }
        }
        return out.size();
    }

    private long getZipInputStreamEntriesSize(File file) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            for (int c = zis.read(); c != -1; c = zis.read()) {
                out.write(c);
            }
        }
        return out.size();
    }

}
