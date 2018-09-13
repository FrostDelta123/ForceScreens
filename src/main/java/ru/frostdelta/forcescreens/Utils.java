package ru.frostdelta.forcescreens;


import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.Proxy;


public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());
    public String OS = System.getProperty("os.name").toLowerCase();
    private int width = Minecraft.getMinecraft().displayWidth;
    private int height = Minecraft.getMinecraft().displayHeight;
    private Proxy proxy = Minecraft.getMinecraft().getProxy();
    private long systemTime = Minecraft.getSystemTime();

    public static void sendPacket(ByteArrayDataOutput buffer){
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("AntiCheat", buffer.toByteArray()));
    }

    public static void sendMessage(String msg) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.addChatMessage(new ChatComponentText(("&f[&AC&f] " + msg).replace("&", "\u00a7")));
    }

    public double JAVA_VERSION = getVersion();

    static double getVersion () {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos+1);
        return Double.parseDouble (version.substring (0, pos));
    }

    public String getRenderer(){
        return "Renderer: {0} " + GL11.glGetString(GL11.GL_RENDERER);
    }

    public String getOpenGLVer(){
        return "OpenGL version " + GL11.glGetString(GL11.GL_VERSION);
    }

    public String getDriver(){
        return "Driver Version: {0} " + Display.getVersion();
    }

    public String getAdapter(){
        return "Driver Version: {0} " + Display.getVersion();
    }


    public String getSystem(){
        if (isWindows()) {
            return "Windows";
        } else if (isMac()) {
            return "MacOS";
        } else if (isUnix()) {
            return "Unix";
        } else if (isSolaris()) {
            return "Solaris";
        } else {
            return "Unknown OS. Custom build?";
        }
    }

    public boolean isWindows() {

        return (OS.contains("win"));

    }

    public boolean isMac() {

        return (OS.contains("mac"));

    }

    public boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );

    }

    public boolean isSolaris() {

        return (OS.contains("sunos"));

    }

    public long getSystemTime() {
        return systemTime;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public Integer getWidth(){
        return width;
    }

    public Integer getHeight(){
        return height;
    }

}