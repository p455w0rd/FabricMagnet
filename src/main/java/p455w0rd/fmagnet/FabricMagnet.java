package p455w0rd.fmagnet;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.events.TickEvent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import p455w0rd.fmagnet.init.ModItems;
import p455w0rd.fmagnet.items.ItemMagnet;

import java.util.List;

public class FabricMagnet implements ModInitializer {

    public static final FabricMagnet INSTANCE = new FabricMagnet();

    @Override
    public void onInitialize() {
        ModItems.register();
        TickEvent.SERVER.register(minecraftServer -> {
            FabricMagnet.INSTANCE.doMagnet(minecraftServer);
        });
    }

    public void doMagnet(MinecraftServer server) {
        List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : playerList) {
            PlayerInventory inv = player.inventory;
            for (int i = 0; i < inv.getInvSize(); i++) {
                if (inv.getInvStack(i).getItem() == ModItems.MAGNET) {
                    ItemStack magnetStack = inv.getInvStack(i);
                    ItemMagnet magnet = (ItemMagnet) magnetStack.getItem();
                    if (magnet.isActive(magnetStack)) {
                        List<ItemEntity> entityItems = player.getServerWorld().getEntities(ItemEntity.class, player.getBoundingBox().expand(16.0D), EntityPredicates.VALID_ENTITY);
                        for (ItemEntity entityItemNearby : entityItems) {
                            if (!player.isSneaking()) {
                                entityItemNearby.method_5694(player);
                            }
                        }
                    }
                }
            }
        }
    }

}
