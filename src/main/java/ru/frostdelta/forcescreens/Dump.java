package ru.frostdelta.forcescreens;


import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Date;


public class Dump {

    public static void dump(){
        try {
            Process p = null;
            GetHWID getHWID = new GetHWID();
            Utils utils = new Utils();
            File playerFolder = new File(Minecraft.getMinecraft().getSession().func_148256_e().getId().toString()+".txt");

            PrintWriter pw = new PrintWriter(playerFolder);
            String line;

            pw.println("-------------------------Процессы-------------------------");
            if(utils.isWindows()) {
                p = Runtime.getRuntime().exec
                        (System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            }else

            if(utils.isUnix() || utils.isMac()){
                    p = Runtime.getRuntime().exec("ps -e");
                }
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                pw.println(line);
            }
            getHWID.dumpHWID();
            pw.println("-------------------------Серийный номер материнской платы-------------------------");
            pw.println(getHWID.getMotherboardSN());
            pw.println("-------------------------Процессор-------------------------");
            pw.println(getHWID.getProcessorName());
            pw.println("-------------------------Имя ПК-------------------------");
            pw.println(getHWID.getPcName());
            pw.println("-------------------------Имя пользователя-------------------------");
            pw.println(getHWID.getUsername());
            pw.println("-------------------------Разрешение окна Minecraft-------------------------");
            pw.println(utils.getWidth() + "x" + utils.getHeight());
            pw.println("-------------------------Proxy-------------------------");
            pw.println(utils.getProxy().address());
            pw.println("-------------------------Время ПК-------------------------");
            pw.println(Date.from(Instant.ofEpochSecond(Sys.getTime())));
            pw.println("-------------------------Версия Java-------------------------");
            pw.println(Runtime.class.getPackage().getImplementationVersion() + " x64? " + Minecraft.getMinecraft().isJava64bit());
            pw.println("-------------------------Система-------------------------");
            pw.println(utils.getSystem() + ": " + utils.OS);
            pw.println("-------------------------Видеоадаптеры-------------------------");
            pw.println(utils.getRenderer());
            pw.println(utils.getOpenGLVer());
            pw.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
