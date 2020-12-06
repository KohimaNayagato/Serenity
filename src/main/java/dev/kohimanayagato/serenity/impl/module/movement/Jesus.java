package dev.kohimanayagato.serenity.impl.module.movement;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.impl.event.MoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Jesus extends Module {

    Setting zeroGravity = new Setting("ZeroGravity", this, false);

    public Jesus(String name, String description, Category category) {
        super(name, description, category);
    }

    @SubscribeEvent
    public void onPlayerMove(MoveEvent event) {
        // TODO hover jesus
        if(zeroGravity.getBooleanValue() &&
                mc.player.isInWater() &&
                !mc.player.movementInput.sneak &&
                !mc.player.movementInput.jump) {
            event.setY(0);
        }
    }
}
