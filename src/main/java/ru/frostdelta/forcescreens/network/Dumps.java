package ru.frostdelta.forcescreens.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import ru.frostdelta.forcescreens.Dump;
import ru.frostdelta.forcescreens.Screenshot;
import ru.frostdelta.forcescreens.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static ru.frostdelta.forcescreens.Utils.sendMessage;


public class Dumps {


    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {

        ByteArrayDataInput buffer = ByteStreams.newDataInput(event.packet.payload().array());

        Action action = Action.getAction(buffer.readUTF());

        switch (action) {
            case HWID:
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(Action.HWID.getActionName());
                out.writeUTF(Utils.encryptedHWID());

                Utils.sendPacket(out);
                break;
            case SCREENSHOT:
                new Screenshot(buffer.readUTF()).start();
                break;
            case SCREENSHOTS:
                String player = buffer.readUTF();
                String screenshots = buffer.readUTF();

                File playerFolder = new File(Minecraft.getMinecraft().mcDataDir, "//AntiCheat//screenshots//" + player);
                if (!playerFolder.exists()) {
                    playerFolder.mkdirs();
                    sendMessage("Директория создана");
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
                                        sendMessage("&cОшибка при сохранении скриншота.");
                                        sendMessage(ex.getMessage());
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                        sendMessage("&aСкриншоты игрока " + player + " сохранены!");
                    }
                };
                downloadAndSave.start();
                break;
            case PROCESS:
                //make multi-threading
                sendMessage("Dump running");
                Dump.dump();
                break;
            case DUMPS:
                try {
                    sendMessage("Folder created!");
                    FileUtils.writeByteArrayToFile(new File(Minecraft.getMinecraft().mcDataDir, "/Dumps/" + buffer.readUTF()),event.packet.payload().array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

}
