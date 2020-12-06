package dev.kohimanayagato.serenity.impl.module.chat;

import dev.kohimanayagato.serenity.impl.event.PacketEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class AutoEZ extends Module
{
    private final Setting timeoutTicks = new Setting("Delay", this, 30, 0, 200);
    private final Setting message = new Setting("Message", this, new ArrayList<>(Arrays.asList("GG", "EZ")));
    private final Setting zCounter = new Setting("zCount", this, 3, 1, 10);
    private final Setting watermark = new Setting("Watermark", this, true);
    private final Setting naked = new Setting("Naked", this, false);

    private ConcurrentHashMap<String, Integer> targetedPlayers = new ConcurrentHashMap<>();

    public AutoEZ(String name, String description, Category category)
    {
        super(name, description, category);
    }

    @Override
    public void onEnable()
    {
        targetedPlayers = new ConcurrentHashMap<>();
    }

    @Override
    public void onDisable()
    {
        targetedPlayers = null;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (mc.player == null || mc.world == null) return;

        if (targetedPlayers == null)
        {
            targetedPlayers = new ConcurrentHashMap<>();
        }

        for (EntityPlayer player : mc.world.playerEntities)
        {
            if (player.getHealth() > 0)
            {
                continue;
            }

            if (!isNaked(mc.player) || naked.getBooleanValue())
            {
                String name = player.getName();

                if (targetedPlayers.containsKey(name))
                {
                    sendMessage(name);
                    break;
                }
            }
        }

        targetedPlayers.forEach((name, timeout) ->
        {
            if (timeout <= 0)
            {
                targetedPlayers.remove(name);
            }
            else
            {
                targetedPlayers.put(name, timeout - 1);
            }
        });

    }

    private boolean isNaked(EntityPlayerSP player)
    {
        return isEmpty(0, player) && isEmpty(1, player) && isEmpty(2, player) && isEmpty(3, player);
    }

    public boolean isEmpty(int slotNumber, EntityPlayer player)
    {
        return player.inventory.armorInventory.get(slotNumber).isEmpty();
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event)
    {

        if (mc.player == null || mc.world == null || !(event.getPacket() instanceof CPacketUseEntity)) return;

        CPacketUseEntity cPacketUseEntity = ((CPacketUseEntity) event.getPacket());

        if (!(cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK))) return;

        Entity targetEntity = cPacketUseEntity.getEntityFromWorld(mc.world);

        if (targetEntity instanceof EntityPlayer) addTargetedPlayer(targetEntity.getName());

    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event)
    {
        if (mc.player == null || mc.world == null) return;

        EntityLivingBase entity = event.getEntityLiving();

        if (!(entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) entity;

        if (player.getHealth() > 0) return;

        String name = player.getName();
        if (targetedPlayers.containsKey(name)) sendMessage(name);
    }

    private void sendMessage(String name)
    {
        targetedPlayers.remove(name);

        StringBuilder text = new StringBuilder();

        if (message.getEnumValue().equals("GG"))
        {
            text.append("GG ").append(name);
        }
        else
        {
            text.append("E");

            for (int i = 0; i < zCounter.getIntegerValue(); i++)
            {
                text.append("Z");
            }

            text.append(" ").append(name);
        }

        if (watermark.getBooleanValue()) text.append("! Serenity owns me and all");
        text.append("!");

        mc.player.connection.sendPacket(new CPacketChatMessage(text.toString()));
    }

    public void addTargetedPlayer(String name)
    {
        if (name.equals(mc.player.getName())) return;
        targetedPlayers.put(name, timeoutTicks.getIntegerValue());
    }
}