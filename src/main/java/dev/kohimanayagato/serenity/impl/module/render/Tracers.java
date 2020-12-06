package dev.kohimanayagato.serenity.impl.module.render;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Tracers extends Module {
    public Tracers(String name, String description, Category category) {
        super(name, description, category);
    }

    Setting animals = new Setting("Animals", this, false);
    Setting animalsColour = new Setting("AnimalsColour", this, new ArrayList<>(Arrays.asList("Black", "Red",  "Blue",  "Gray", "White", "Green", "Yellow", "Pink")));
    Setting monsters = new Setting("Monsters", this, false);
    Setting monstersColour = new Setting("MonstersColour", this, new ArrayList<>(Arrays.asList("Black", "Red",  "Blue",  "Gray", "White", "Green", "Yellow", "Pink")));
    Setting players = new Setting("Players", this, false);
    Setting playersColour = new Setting("PlayersColour", this, new ArrayList<>(Arrays.asList("Black", "Red",  "Blue",  "Gray", "White", "Green", "Yellow", "Pink")));

    @SubscribeEvent
    public void renderWorldEvent(RenderWorldLastEvent event) {
        if (mc.player == null || mc.world == null) return;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(1.5f);

        Vec3d eyesPos = mc.player.getPositionEyes(event.getPartialTicks());
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity != mc.getRenderViewEntity()) {
                // monsters
                if ((entity instanceof IMob) && monsters.getBooleanValue()) {
                    if(mc.gameSettings.thirdPersonView != 0) {
                        drawLine(eyesPos, entity.getPositionEyes(event.getPartialTicks()), getColor(monstersColour.getEnumValue()));
                    } else {
                        drawLine(eyesPos.add(mc.player.getLookVec()), entity.getPositionEyes(event.getPartialTicks()), getColor(monstersColour.getEnumValue()));
                    }
                // animals
                } else if (entity instanceof IAnimals && animals.getBooleanValue()) {
                    if(mc.gameSettings.thirdPersonView != 0) {
                        drawLine(eyesPos, entity.getPositionEyes(event.getPartialTicks()), getColor(animalsColour.getEnumValue()));
                    } else {
                        drawLine(eyesPos.add(mc.player.getLookVec()), entity.getPositionEyes(event.getPartialTicks()), getColor(animalsColour.getEnumValue()));
                    }
                }
            }
        }
        // players
        if (players.getBooleanValue()) {
            for (EntityPlayer playerEntity : mc.world.playerEntities) {
                if (playerEntity != mc.getRenderViewEntity()) {
                    if(mc.gameSettings.thirdPersonView != 0) {
                        drawLine(eyesPos, playerEntity.getPositionEyes(event.getPartialTicks()), getColor(playersColour.getEnumValue()));
                    } else {
                        drawLine(eyesPos.add(mc.player.getLookVec()), playerEntity.getPositionEyes(event.getPartialTicks()), getColor(playersColour.getEnumValue()));
                    }
                }
            }
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private Color getColor(String s) {
        switch (s) {
            case "Black":
                return Color.BLACK;
            case "Red":
                return Color.RED;
            case "Blue":
                return Color.BLUE;
            case "Gray":
                return Color.GRAY;
            case "White":
                return Color.WHITE;
            case "Green":
                return Color.GREEN;
            case "Yellow":
                return Color.YELLOW;
            case "Pink":
                return Color.PINK;
            default:
                return Color.BLACK;
        }
    }

    /**
     * Pasted from Canopy
     * Pasted by cats
     * @author not cats or Nirvana
     * From some spot in some client
     */
    private void drawLine(Vec3d startPos, Vec3d endPos, Color color) {

        double posX = mc.getRenderManager().renderPosX;
        double posY = mc.getRenderManager().renderPosY;
        double posZ = mc.getRenderManager().renderPosZ;

        final Vec3d modifiedStartPos = startPos.add(-posX, -posY, -posZ);
        final Vec3d modifiedEndPos = endPos.add(-posX, -posY, -posZ);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(modifiedStartPos.x, modifiedStartPos.y, modifiedStartPos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(modifiedEndPos.x, modifiedEndPos.y, modifiedEndPos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();
    }

}
