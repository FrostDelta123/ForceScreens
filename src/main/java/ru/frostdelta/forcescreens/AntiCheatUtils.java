package ru.frostdelta.forcescreens;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class AntiCheatUtils {

    static Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (mc.objectMouseOver != null) {
            MovingObjectPosition mouse = mc.objectMouseOver;
            if (mouse.entityHit != null && event.target != null) {
                if (!mouse.entityHit.equals(event.target)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (mc.objectMouseOver != null) {
                MovingObjectPosition mouse = mc.objectMouseOver;
                if (!((mouse.blockX == event.x) && (mouse.blockY == mouse.blockY) && (mouse.blockZ == event.z))) {
                    event.setCanceled(true);
                }
            }
        }
    }

}
