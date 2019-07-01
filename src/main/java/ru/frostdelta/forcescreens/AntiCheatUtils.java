package ru.frostdelta.forcescreens;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class AntiCheatUtils {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onEnter(FMLNetworkEvent.ClientConnectedToServerEvent event){
        new Dump().start();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onAttack(AttackEntityEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
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
    @SideOnly(Side.CLIENT)
    public void onClick(PlayerInteractEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (mc.objectMouseOver != null) {
                MovingObjectPosition mouse = mc.objectMouseOver;
                if (!((mouse.blockX == event.x) && (mouse.blockZ == event.z))) {
                    event.setCanceled(true);
                }
            }
        }
    }

}
