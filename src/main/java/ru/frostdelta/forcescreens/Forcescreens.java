package ru.frostdelta.forcescreens;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import ru.frostdelta.forcescreens.network.Dumps;
import ru.frostdelta.forcescreens.network.Network;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


@Mod(
        modid = "ForceScreens",
        name = "ForceScreens",
        version = "2.1",
        dependencies = "after:Minecraft Forge",
        canBeDeactivated = false
)
public class Forcescreens {

    private static FMLEventChannel channel;
    private static FMLEventChannel dumpChannel;


    @Mod.Instance("ForceScreens")
    public static Forcescreens MOD;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){


        try {
            Field classes = ClassLoader.class.getDeclaredField("classes");
            classes.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(classes, classes.getModifiers() & ~Modifier.FINAL);


            @SuppressWarnings("unchecked")
            java.util.Vector<Class> loadedClasses = (java.util.Vector<Class>) classes.get(Launch.classLoader);
            classes.set(Launch.classLoader, new Vector());

            for (Class clazz : loadedClasses) {
                if (!Utils.checkClass(clazz)) {
                    Utils.killMinecraft();
                }
            }

        }catch (IllegalAccessException ex){
            ex.printStackTrace();
        }catch (NoSuchFieldException e1){
            e1.printStackTrace();
        }


        Network network = new Network();
        Dumps dumps = new Dumps();
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("AntiCheat");
        dumpChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel("Dump");
        dumpChannel.register(dumps);
        channel.register(network);
        MinecraftForge.EVENT_BUS.register(network);
        MinecraftForge.EVENT_BUS.register(dumps);

    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent e) {
        new ClassPathChecker().start();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
    }


}
