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
import java.io.File;
import java.lang.management.ManagementFactory;
import java.awt.Dimension;
import java.lang.reflect.Field;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.net.SocketException;


public interface Utils {


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
            /*classes.set(Launch.classLoader, new Vector());

            for (Class clazz : loadedClasses) {
                if (!Utils.checkClass(clazz)) {
                    Utils.killMinecraft();
                }
            }*/

        }catch (IllegalAccessException ex){
            ex.printStackTrace();
        }catch (NoSuchFieldException e1){
            e1.printStackTrace();
        }
        return null;
    }

    static boolean checkClass(Class c) {

        return (c.getProtectionDomain() != null && c.getProtectionDomain().getCodeSource() != null)
                || Proxy.isProxyClass(c);
    }

    static void killMinecraft() {
        try {
            boolean isMCP = isMCP = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

            Minecraft mc = Minecraft.getMinecraft();
            Field theMinecraft = mc.getClass().getDeclaredField(isMCP ? "theMinecraft" : "field_71432_P");
            theMinecraft.setAccessible(true);
            theMinecraft.set(Minecraft.getMinecraft(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    Logger logger = Logger.getLogger(Utils.class.getName());
    String OS = System.getProperty("os.name").toLowerCase();
    int width = Minecraft.getMinecraft().displayWidth;
    int height = Minecraft.getMinecraft().displayHeight;
    java.net.Proxy proxy = Minecraft.getMinecraft().getProxy();
    long systemTime = Minecraft.getSystemTime();

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

    static void sendMessage(String msg) {
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

    static String getIP(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Long getUsedMemory(){
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    static String getMacAdress(){

        try {
            byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Long getMemory(){
        return Runtime.getRuntime().maxMemory();
    }

    static Long getAlternativeMemory(){
        return ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
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


    static String getSystem(){
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

    static boolean isWindows() {

        return (OS.contains("win"));

    }

    static boolean isMac() {

        return (OS.contains("mac"));

    }

    static boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );

    }

    static boolean isSolaris() {

        return (OS.contains("sunos"));

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