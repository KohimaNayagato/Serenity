package me.kohimanayagato.serenity.impl.module.movement;

import me.kohimanayagato.serenity.api.module.Category;
import me.kohimanayagato.serenity.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint extends Module {
    public Sprint(String name, String description, Category category) {
        super(name, description, category);
    }

    @SubscribeEvent
    public void onUpdate(final TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (!mc.player.isSprinting()) {
            mc.player.setSprinting(true);
        }
    }
}
