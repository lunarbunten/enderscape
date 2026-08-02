package net.penumbra.enderscape.entity.ai;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.schedule.Activity;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeActivity extends Activity {

    public static final Activity ITEM_CONVERSION = register("item_conversion");

    public EnderscapeActivity() {
        super("");
    }
    
    protected static Activity register(String name) {
        Identifier id = Enderscape.id(name);
        return Registry.register(BuiltInRegistries.ACTIVITY, id, new Activity(id.toString()));
    }
}