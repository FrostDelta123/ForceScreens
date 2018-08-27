package ru.frostdelta.forcescreens;

import java.lang.reflect.*;

import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.network.play.client.C17PacketCustomPayload;


public class Utils {


    private static Minecraft mc;
    private static boolean isMCP;


    // Ебучий костыль (c) Qmaks
    public static void init() {
        mc = Minecraft.getMinecraft();
        isMCP = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    public static boolean checkClass(Class c) {

        return (c.getProtectionDomain() != null && c.getProtectionDomain().getCodeSource() != null)
                || Proxy.isProxyClass(c);
    }

    public static void killMinecraft() {
        try {
            Field theMinecraft = mc.getClass().getDeclaredField(isMCP ? "theMinecraft" : "field_71432_P");
            theMinecraft.setAccessible(true);
            theMinecraft.set(Minecraft.getMinecraft(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendPacket(ByteArrayDataOutput buffer){

        mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("AntiCheat", buffer.toByteArray()));

    }

    //P.S. 1.7.10 Хуйня
    /*public static void sendPacket(ByteArrayDataOutput buffer) {

        mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("AntiCheat", buffer.toByteArray()));
    }*/


}