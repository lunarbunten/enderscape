package net.bunten.enderscape.entity.ai;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.schedule.Activity;

public class EnderscapeActivity extends Activity {
    public EnderscapeActivity() {
        super("");
    }
    
    protected static Activity register(String name) {
        ResourceLocation id = Enderscape.id(name);
        return Registry.register(BuiltInRegistries.ACTIVITY, id, new Activity(id.toString()));
    }
}