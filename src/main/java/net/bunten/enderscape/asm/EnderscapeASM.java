package net.bunten.enderscape.asm;

import com.chocohead.mm.api.ClassTinkerers;

import net.fabricmc.loader.api.FabricLoader;

public class EnderscapeASM implements Runnable {

    private String getIntermediaryClass(String path) {
        return FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", path);
    }
    
    @Override
    public void run() {
        ClassTinkerers.enumBuilder(getIntermediaryClass("net.minecraft.class_1886")).addEnumSubclass("ENDERSCAPE_MIRROR", "net.bunten.enderscape.asm.MirrorEnchantmentTarget").build();
    }
}