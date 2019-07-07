package ru.frostdelta.forcescreens;

import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

public class Dump extends Thread{


    @Override
    public void run() {
        try {

            Thread.sleep(2000);
            //File playerFolder = new File(Minecraft.getMinecraft().getSession().func_148256_e().getId().toString() + ".txt");

            //PrintWriter pw = new PrintWriter(playerFolder);
            for (Class clazz : Objects.requireNonNull(Utils.getClasses())) {
                //Utils.sendDump(clazz.getName().getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dump(){
        try {
            File playerFolder = new File(Minecraft.getMinecraft().getSession().func_148256_e().getId().toString()+".txt");

            PrintWriter pw = new PrintWriter(playerFolder);
            pw.println("-------------------------Процессы-------------------------");
            pw.println("Disabled!");
            /*
            Process p = null;
            String line;
            if(Utils.getOperationSystem().getFamily().equalsIgnoreCase("Windows")) {
                p = Runtime.getRuntime().exec
                        (System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            }else p = Runtime.getRuntime().exec("ps -e");

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                pw.println(line);
            }*/
            pw.println("-------------------------Серийный номер материнской платы-------------------------");
            pw.println(Utils.getHardwareAbstractionLayer().getComputerSystem().getBaseboard().getSerialNumber());
            pw.println("-------------------------Процессор-------------------------");
            pw.println("Family: " + Utils.getHardwareAbstractionLayer().getProcessor().getFamily() + " ID: " + Utils.getHardwareAbstractionLayer().getProcessor().getIdentifier() + " Name: " +Utils.getHardwareAbstractionLayer().getProcessor().getName());
            pw.println("-------------------------Имя ПК-------------------------");
            pw.println(Utils.getPCName());
            pw.println("-------------------------Операционная система-------------------------");
            pw.println("Name: " + Utils.getOperationSystem().getFamily());
            pw.println("Version: " + Utils.getOperationSystem().getVersion().getVersion());
            pw.println("-------------------------Имя пользователя-------------------------");
            pw.println(Utils.getUsername());
            pw.println("-------------------------Разрешение-------------------------");
            pw.println("Minecraft: " + Utils.getWidth() + "x" + Utils.getHeight());
            pw.println("Max: " + Utils.getScreenWidth() + "x" + Utils.getScreenHeight());
            pw.println("-------------------------Proxy-------------------------");
            pw.println(Utils.getProxy().address());
            pw.println("-------------------------Время ПК-------------------------");
            pw.println(Utils.getSystemTime());
            pw.println("-------------------------Версия Java-------------------------");
            pw.println(Utils.getJavaVersion() + " x64? " + Utils.isJava64bit());
            pw.println("-------------------------Видеоадаптеры-------------------------");
            pw.println(Utils.getRenderer());
            pw.println(Utils.getOpenGLVer());
            pw.println("-------------------------Путь до Minecraft-------------------------");
            pw.println(Utils.getMinecraftPath());
            pw.println("-------------------------Список ресурс-паков-------------------------");
            for(File str : Utils.getResourcePacks()){
                pw.println(str.getName());
            }
            pw.println("-------------------------Размер диска-------------------------");
            pw.println(Utils.getDiskSpace() + " Bytes");
            pw.println("-------------------------Оперативная память-------------------------");
            pw.println(Utils.getMemory());
            pw.println("-------------------------IP и MAC адресс-------------------------");
            pw.println("Ip: " + Arrays.toString(Utils.getIP()));
            pw.println("MAC: " + Utils.getMacAdress());
            pw.println("-------------------------Использованная память-------------------------");
            pw.println(Utils.getUsedMemory() + " Bytes");
            pw.println("-------------------------Все классы-------------------------");
            pw.println("DEPRECATED! DISABLED!");
            /*for(Class clazz : Utils.getClasses()){
                pw.println(clazz.getName());
            }*/
            pw.close();
            //input.close();
            byte[] fileContent = Files.readAllBytes(playerFolder.toPath());
            Utils.sendDump(fileContent);
            playerFolder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
