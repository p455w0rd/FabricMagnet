package p455w0rd.fmagnet.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipOptions;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import p455w0rd.fmagnet.init.ModGlobals;

import java.util.List;

public class ItemMagnet extends Item {

    private static final String NBT_MAGNET_MODE = "MagnetMode";

    public ItemMagnet() {
        super(new Settings().stackSize(1).itemGroup(ItemGroup.TOOLS));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack magnet = player.getStackInHand(hand);
        if (!world.isClient && player.isSneaking()) {
            cycleMode(magnet);
            return new TypedActionResult<>(ActionResult.SUCCESS, magnet);
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, magnet);
    }

    @Environment(EnvType.CLIENT)
    public void buildTooltip(ItemStack magnet, World world, List<TextComponent> lines, TooltipOptions mode) {
        String localizedLine = I18n.translate("tooltip.fmagnet.mode") + ": " + getMagnetMode(magnet).getLocalizedMode();
        lines.add(new StringTextComponent(localizedLine));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean hasEnchantmentGlow(ItemStack magnet) {
        return isActive(magnet);
    }

    public Identifier getRegistryName() {
        return new Identifier(ModGlobals.MODID, "magnet");
    }

    public boolean isActive(ItemStack magnet) {
        return getMagnetMode(magnet).getBoolean();
    }

    private void setMagnetMode(ItemStack magnet, MagnetMode mode) {
        checkTag(magnet);
        magnet.getTag().putBoolean(NBT_MAGNET_MODE, mode.getBoolean());
    }

    private MagnetMode getMagnetMode(ItemStack magnet) {
        if (!magnet.isEmpty()) {
            checkTag(magnet);
            return magnet.getTag().getBoolean(NBT_MAGNET_MODE) ? MagnetMode.ACTIVE : MagnetMode.INACTIVE;
        }
        return MagnetMode.INACTIVE;
    }

    private void cycleMode(ItemStack magnet) {
        MagnetMode currentMode = getMagnetMode(magnet);
        if (currentMode.getBoolean()) {
            setMagnetMode(magnet, MagnetMode.INACTIVE);
            return;
        }
        setMagnetMode(magnet, MagnetMode.ACTIVE);
    }

    private void checkTag(ItemStack magnet) {
        if (!magnet.isEmpty()) {
            if (!magnet.hasTag()) {
                magnet.setTag(new CompoundTag());
            }
            CompoundTag nbt = magnet.getTag();
            if (!nbt.containsKey(NBT_MAGNET_MODE)) {
                nbt.putBoolean(NBT_MAGNET_MODE, false);
            }
        }
    }

    public enum MagnetMode {

        ACTIVE(true), INACTIVE(false);

        final boolean state;

        MagnetMode(boolean state) {
            this.state = state;
        }

        public boolean getBoolean() {
            return state;
        }

        public String getUnlocalizedMode() {
            return "tooltip.fmagnet." + (getBoolean() ? "" : "in") + "active";
        }

        public String getLocalizedMode() {
            return (getBoolean() ? TextFormat.GREEN.toString() : TextFormat.RED.toString()) + I18n.translate(getUnlocalizedMode());
        }

    }

}
