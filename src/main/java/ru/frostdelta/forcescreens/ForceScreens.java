package ru.frostdelta.forcescreens;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraftforge.common.MinecraftForge;
import ru.frostdelta.forcescreens.network.Dumps;
import ru.frostdelta.forcescreens.network.Network;


@Mod(
        modid = "ForceScreens",
        name = "ForceScreens",
        version = "4.0",
        dependencies = "after:Minecraft Forge",
        canBeDeactivated = false
)
public class ForceScreens {

    private static FMLEventChannel channel;
    private static FMLEventChannel dumpChannel;


    @Mod.Instance("ForceScreens")
    public static ForceScreens MOD;


    @SuppressWarnings("unchecked")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){

        Utils.loadSystemInfo();
       /* try {

            Field classes = ClassLoader.class.getDeclaredField("classes");
            classes.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(classes, classes.getModifiers() & ~Modifier.FINAL);

            java.util.Vector<Class> loadedClasses = (java.util.Vector<Class>) classes.get(Launch.classLoader);
            EboboVector vec = new EboboVector();
            vec.addAll(loadedClasses);
            classes.set(Launch.classLoader, vec);

            // Проверяем ранее загруженные классы
            for (Class clazz : loadedClasses) {
                JOptionPane.showMessageDialog(null, clazz.getName());
                System.out.println(clazz.getName());
                if (!Utils.checkClass(clazz)) {
                    Utils.killMinecraft();
                }
            }

        }catch (IllegalAccessException ex){
            ex.printStackTrace();
        }catch (NoSuchFieldException e1){
            e1.printStackTrace();
        }*/

        Network network = new Network();
        Dumps dumps = new Dumps();
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("AntiCheat");
        dumpChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel("Dump");
        dumpChannel.register(dumps);
        channel.register(network);
        //MinecraftForge.EVENT_BUS.register(new AntiCheatUtils());
        MinecraftForge.EVENT_BUS.register(network);
        MinecraftForge.EVENT_BUS.register(dumps);
        FMLCommonHandler.instance().bus().register(new AntiCheatUtils());
        Dump.dump();
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
