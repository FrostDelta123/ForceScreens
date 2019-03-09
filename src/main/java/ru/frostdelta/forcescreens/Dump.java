package ru.frostdelta.forcescreens;

import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;

public class Dump extends Thread{


    @Override
    public void run() {
        try {

            Thread.sleep(2000);
            //File playerFolder = new File(Minecraft.getMinecraft().getSession().func_148256_e().getId().toString() + ".txt");

            //PrintWriter pw = new PrintWriter(playerFolder);
            for (Class clazz : Utils.getClasses()) {
                Utils.sendDump(clazz.getName().getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dump(){
        try {
            Process p = null;
            File playerFolder = new File(Minecraft.getMinecraft().getSession().func_148256_e().getId().toString()+".txt");

            PrintWriter pw = new PrintWriter(playerFolder);
            String line;

            pw.println("-------------------------Процессы-------------------------");
            if(Utils.isWindows()) {
                p = Runtime.getRuntime().exec
                        (System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            }else

            if(Utils.isUnix() || Utils.isMac()){
                    p = Runtime.getRuntime().exec("ps -e");
                }

            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                pw.println(line);
            }

            //Костылёк
            GetHWID.dumpHWID();
            pw.println("-------------------------Серийный номер материнской платы-------------------------");
            pw.println(GetHWID.getMotherboardSN());
            pw.println("-------------------------Процессор-------------------------");
            pw.println(Utils.getProcessorInfo());
            pw.println("-------------------------Имя ПК-------------------------");
            pw.println(Utils.getPCName());
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
            pw.println("-------------------------Система-------------------------");
            pw.println(Utils.getSystem() + ": " + Utils.OS);
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
            pw.println(Utils.getMemory() + " Bytes");
            pw.println("Alternative " + Utils.getAlternativeMemory() + " Bytes");
            pw.println("-------------------------IP и MAC адресс-------------------------");
            pw.println("Ip: " + Utils.getIP());
            pw.println("MAC: " + Utils.getMacAdress());
            pw.println("-------------------------Использованная память-------------------------");
            pw.println(Utils.getUsedMemory() + " Bytes");
            pw.println("-------------------------Все классы-------------------------");
            for(Class clazz : Utils.getClasses()){
                pw.println(clazz.getName());
            }
            pw.close();
            input.close();
            byte[] fileContent = Files.readAllBytes(playerFolder.toPath());
            Utils.sendDump(fileContent);
            playerFolder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
