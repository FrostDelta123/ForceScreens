package ru.frostdelta.forcescreens.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import ru.frostdelta.forcescreens.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Locker {

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {

        ByteArrayDataInput buffer = ByteStreams.newDataInput(event.packet.payload().array());

        Action action = Action.getAction(buffer.readUTF());
        switch (action) {
            case CLASSES:
                ByteArrayDataOutput outClasses = ByteStreams.newDataOutput();
                outClasses.writeUTF(Action.CLASSES.getActionName());
                List<Class> list = Collections.list(Objects.requireNonNull(Utils.getClasses()).elements());
                try {
                    outClasses.writeUTF(Utils.sendToHaste(list));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Utils.sendPacketLocker(outClasses);
                break;
            case BAN:
                ByteArrayDataOutput outBan = ByteStreams.newDataOutput();
                outBan.writeUTF(Action.BAN.getActionName());
                outBan.writeUTF(Utils.encryptedHWID());

                Utils.sendPacketLocker(outBan);
                break;

            case HWID:
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(Action.HWID.getActionName());
                out.writeUTF(Utils.encryptedHWID());

                Utils.sendPacketLocker(out);
                break;

            default:
                break;
        }
    }

}
