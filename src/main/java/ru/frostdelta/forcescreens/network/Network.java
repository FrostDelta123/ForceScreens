package ru.frostdelta.forcescreens.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import ru.frostdelta.forcescreens.Screenshot;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import net.minecraft.client.Minecraft;

import javax.swing.*;


public class Network {

    File playerFolder;
    String screenshots;
    String player;

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent e){

        JOptionPane.showMessageDialog(null, "test");
        ByteArrayDataInput buffer = ByteStreams.newDataInput(e.packet.payload().array());
        Action action = Action.getAction(buffer.readUTF());
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        switch (action) {
            case SCREENSHOT:
                new Screenshot(buffer.readUTF()).start();
                break;
            case SCREENSHOTS:
                player = buffer.readUTF();
                screenshots = buffer.readUTF();

                playerFolder = new File(Minecraft.getMinecraft().mcDataDir, "//AntiCheat//screenshots//" + player);
                if (!playerFolder.exists()) {
                    playerFolder.mkdirs();
                }

                Thread downloadAndSave = new Thread() {
                    @Override
                    public void run() {
                        for (String screenID : screenshots.split(";")) {
                            if (!screenID.isEmpty()) {
                                File target = new File(playerFolder, screenID + ".jpg");
                                if (!target.exists()) {
                                    try {
                                        Files.copy(new URL("http://i.imgur.com/" + screenID + ".jpg").openStream(),
                                                target.toPath());
                                    } catch (Exception ex) {
                                        System.out.println("&cОшибка при сохранении скриншота.");
                                        System.out.println(ex.getMessage());
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                        System.out.println("&aСкриншоты игрока " + player + " сохранены!");
                    }
                };
                downloadAndSave.start();
                break;
            default:
                break;
        }

    }

}
