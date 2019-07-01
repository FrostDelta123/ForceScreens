package ru.frostdelta.forcescreens;


import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class Utils {
    static SystemInfo systemInfo;
    static CentralProcessor centralProcessor;
    static HardwareAbstractionLayer hardwareAbstractionLayer;

    static void loadSystemInfo(){
        systemInfo = new SystemInfo();
        hardwareAbstractionLayer = systemInfo.getHardware();
        centralProcessor = hardwareAbstractionLayer.getProcessor();
    }


    static CentralProcessor getCentralProcessor(){
        return centralProcessor;
    }

    static HardwareAbstractionLayer getHardwareAbstractionLayer(){
        return hardwareAbstractionLayer;
    }
    static java.util.Vector<Class> getClasses(){
        try {
            Field classes = ClassLoader.class.getDeclaredField("classes");
            classes.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(classes, classes.getModifiers() & ~Modifier.FINAL);

            @SuppressWarnings("unchecked")
            java.util.Vector<Class> loadedClasses = (java.util.Vector<Class>) classes.get(Launch.classLoader);
            return loadedClasses;

        }catch (IllegalAccessException ex){
            ex.printStackTrace();
        }catch (NoSuchFieldException e1){
            e1.printStackTrace();
        }
        return null;
    }

    public static boolean checkClass(Class c) {

        return (c.getProtectionDomain() != null && c.getProtectionDomain().getCodeSource() != null) || Proxy.isProxyClass(c);
    }

    public static void killMinecraft() {
        try {
            boolean isMCP = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

            Minecraft mc = Minecraft.getMinecraft();
            Field theMinecraft = mc.getClass().getDeclaredField(isMCP ? "theMinecraft" : "field_71432_P");
            theMinecraft.setAccessible(true);
            theMinecraft.set(Minecraft.getMinecraft(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static OperatingSystem getOperationSystem(){
        return systemInfo.getOperatingSystem();
    }


    Logger logger = Logger.getLogger(Utils.class.getName());
    static String OS = System.getProperty("os.name").toLowerCase();
    static int width = Minecraft.getMinecraft().displayWidth;
    static int height = Minecraft.getMinecraft().displayHeight;
    static java.net.Proxy proxy = Minecraft.getMinecraft().getProxy();
    static long systemTime = Minecraft.getSystemTime();

    static void sendDump(byte[] stream){
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("Dump", stream));
    }

    static List<String> settings(){
        List<String> settingList = new ArrayList<String>();
        Minecraft minecraft = Minecraft.getMinecraft();
        settingList.add("Debug: " + minecraft.debug);
        return settingList;
    }

    static void sendPacket(ByteArrayDataOutput buffer){
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("AntiCheat", buffer.toByteArray()));
    }

    public static void sendMessage(String msg) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.addChatMessage(new ChatComponentText(("&f[&AC&f] " + msg).replace("&", "\u00a7")));
    }

    double JAVA_VERSION = getVersion();

    static String getProcessorInfo(){
        return System.getenv("PROCESSOR_IDENTIFIER").trim();
    }

    static String getUsername(){
        return System.getProperty("user.name").trim();
    }

    static String getPCName(){

        return System.getenv("COMPUTERNAME").trim();
    }

    static double getVersion () {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos+1);
        return Double.parseDouble (version.substring (0, pos));
    }

    static Integer getScreenWidth(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.width;
    }

    static Integer getScreenHeight(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.height;
    }

    static Boolean isJava64bit(){
        return Minecraft.getMinecraft().isJava64bit();

    }

    static String getJavaVersion(){

        return Runtime.class.getPackage().getImplementationVersion();
    }


    @SideOnly(Side.CLIENT)
    static String getRenderer(){
        return "Renderer: {0} " + GL11.glGetString(GL11.GL_RENDERER);
    }

    @SideOnly(Side.CLIENT)
    static String getOpenGLVer(){
        return "OpenGL driver version " + GL11.glGetString(GL11.GL_VERSION);
    }

    @SideOnly(Side.CLIENT)
    static String getDriver(){
        return "Driver Version: {0} " + Display.getVersion();
    }

    static String getAdapter(){

        return "Driver Version: {0} " + Display.getAdapter();
    }

    static String[] getIP(){
        systemInfo.getHardware().getNetworkIFs()[0].updateNetworkStats();
        return systemInfo.getHardware().getNetworkIFs()[0].getIPv4addr();
    }

    static Long getUsedMemory(){
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    static String getMacAdress(){
        systemInfo.getHardware().getNetworkIFs()[0].updateNetworkStats();
        return systemInfo.getHardware().getNetworkIFs()[0].getMacaddr();
    }

    static Long getMemory(){
        return systemInfo.getHardware().getMemory().getTotal();
    }

    static Long getDiskSpace(){
        return new File("/").getTotalSpace();
    }

    static List<File> getResourcePacks(){

        List<File> list = new ArrayList<>();
        File folder = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()+"/resourcepacks");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                list.add(file);
            }
        }
        return list;
    }

    static String getMinecraftPath(){
        return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    }


    static long getSystemTime() {
        return systemTime;
    }

    static java.net.Proxy getProxy() {
        return proxy;
    }

    static Integer getWidth(){
        return width;
    }

    static Integer getHeight(){
        return height;
    }

}