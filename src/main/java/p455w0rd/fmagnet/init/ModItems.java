package p455w0rd.fmagnet.init;

import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import p455w0rd.fmagnet.items.ItemMagnet;

public class ModItems {

    public static final ItemMagnet MAGNET = new ItemMagnet();

    public static void register() {
        Registry.ITEM.register(MAGNET.getRegistryName(),MAGNET);
    }

}
