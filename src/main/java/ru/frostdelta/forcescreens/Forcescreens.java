package ru.frostdelta.forcescreens;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;


@Mod(
        modid = "ForceScreens",
        name = "ForceScreens",
        version = "1.0-SNAPSHOT",
        dependencies = "after:Minecraft Forge",
        canBeDeactivated = false
)
public class Forcescreens {

    public static FMLEventChannel channel;

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("AntiCheat");

    @Mod.Instance("ForceScreens")
    public static Forcescreens MOD;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){

        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("AntiCheat");

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
    }


}
