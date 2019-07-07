package ru.frostdelta.forcescreens;


import com.google.common.io.ByteArrayDataOutput;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.GL11;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Utils {

    static HardwareAbstractionLayer getHardwareAbstractionLayer(){
        return new SystemInfo().getHardware();
    }
    public static java.util.Vector<Class> getClasses(){
        try {
            Field classes = ClassLoader.class.getDeclaredField("classes");
            classes.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(classes, classes.getModifiers() & ~Modifier.FINAL);

            @SuppressWarnings("unchecked")
            java.util.Vector<Class> loadedClasses = (java.util.Vector<Class>) classes.get(Launch.classLoader);
            return loadedClasses;

        }catch (IllegalAccessException | NoSuchFieldException ex){
            ex.printStackTrace();
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
        return new SystemInfo().getOperatingSystem();
    }

    static void sendDump(byte[] stream){
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("Dump", stream));
    }

    public static void sendPacketLocker(ByteArrayDataOutput buffer){
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("Locker", buffer.toByteArray()));
    }

    public static void sendPacket(ByteArrayDataOutput buffer){
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("AntiCheat", buffer.toByteArray()));
    }

    public static void sendMessage(String msg) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.addChatMessage(new ChatComponentText(("&f[&AC&f] " + msg).replace("&", "\u00a7")));
    }

    static String getUsername(){
        return System.getProperty("user.name").trim();
    }

    static String getPCName(){

        return System.getenv("COMPUTERNAME").trim();
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

    static String[] getIP(){
        new SystemInfo().getHardware().getNetworkIFs()[0].updateNetworkStats();
        return new SystemInfo().getHardware().getNetworkIFs()[0].getIPv4addr();
    }

    static Long getUsedMemory(){
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    static String getMacAdress(){
        new SystemInfo().getHardware().getNetworkIFs()[0].updateNetworkStats();
        return new SystemInfo().getHardware().getNetworkIFs()[0].getMacaddr();
    }

    static Long getMemory(){
        return new SystemInfo().getHardware().getMemory().getTotal();
    }

    static Long getDiskSpace(){
        return new File("/").getTotalSpace();
    }

    static List<File> getResourcePacks(){

        List<File> list = new ArrayList<>();
        File folder = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()+"/resourcepacks");
        File[] listOfFiles = folder.listFiles();
        for (File file : Objects.requireNonNull(listOfFiles)) {
            if (file.isFile()) {
                list.add(file);
            }
        }
        return list;
    }

    public static String encryptedHWID(){
        String input = getRenderer();
        input += "Family: " + Utils.getHardwareAbstractionLayer().getProcessor().getFamily() + " ID: " + Utils.getHardwareAbstractionLayer().getProcessor().getIdentifier() + " Name: " + Utils.getHardwareAbstractionLayer().getProcessor().getName();
        input += Utils.getHardwareAbstractionLayer().getComputerSystem().getBaseboard().getSerialNumber();

        return md5(input);
    }

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sendToHaste(List<? extends Class> input) throws IOException {

        Gson gson = new Gson();
        HttpURLConnection connection = (HttpURLConnection) new URL("https://paste.divinecode.org/documents").openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod( "POST" );
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty( "Charset", "UTF-8" );
        connection.setRequestProperty( "Content-Type", "text/plain" );
        connection.connect();
        try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())){

            for(Class in : input) {
                String className = in.getName() + "\n";
                dos.write(className.getBytes(StandardCharsets.UTF_8));
            }
            dos.flush();
        }

        String result;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
        {
            result = br.lines().collect(Collectors.joining());
        }
        connection.disconnect();

        return "https://paste.divinecode.org/" +  gson.fromJson(result, JsonObject.class).get("key").getAsString();
    }

    static String getMinecraftPath(){
        return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    }

    static long getSystemTime() {
        return System.currentTimeMillis();
    }

    static java.net.Proxy getProxy() {
        return Minecraft.getMinecraft().getProxy();
    }

    static Integer getWidth(){
        return Minecraft.getMinecraft().displayWidth;
    }

    static Integer getHeight(){
        return Minecraft.getMinecraft().displayHeight;
    }

}