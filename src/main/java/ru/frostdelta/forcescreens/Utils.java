package ru.frostdelta.forcescreens;

import java.lang.reflect.*;

import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatComponentText;


public class Utils {


    //private static Minecraft mc;
    private static boolean isMCP;


    // Ебучий костыль (c) Qmaks
    public static void init() {
        //mc = Minecraft.getMinecraft();
        isMCP = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    public static void sendPacket(ByteArrayDataOutput buffer){
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("AntiCheat", buffer.toByteArray()));
    }

    public static void sendMessage(String msg) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.addChatMessage(new ChatComponentText(("&f[&bAntiCheat&f] " + msg).replace("&", "\u00a7")));
    }

}