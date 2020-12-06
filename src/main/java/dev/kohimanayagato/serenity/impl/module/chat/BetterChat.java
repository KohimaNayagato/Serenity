package dev.kohimanayagato.serenity.impl.module.chat;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class BetterChat extends Module
{
    private final Setting timestamp = new Setting("Timestamps", this, false);
    private final Setting bracketStyle = new Setting("BracketType", this, new ArrayList<>(Arrays.asList("<->", "[-]", "{-}", ":", "None")));
    private final Setting timestampColor = new Setting("TimestampColor", this, new ArrayList<>(Arrays.asList("Black", "Red", "Aqua", "Blue", "Gold", "Gray", "White", "Green", "Yellow", "Purple", "DRed", "DAqua", "DBlue", "DGray", "DGreen", "DPurple", "Reset")));
    private final Setting timestampBracket = new Setting("TBracketType", this, new ArrayList<>(Arrays.asList("<->", "[-]", "{-}", "None")));
    private final Setting timestampBracketColor = new Setting("TBracketColor", this, new ArrayList<>(Arrays.asList("Black", "Red", "Aqua", "Blue", "Gold", "Gray", "White", "Green", "Yellow", "Purple", "DRed", "DAqua", "DBlue", "DGray", "DGreen", "DPurple", "Reset")));
    private final Setting bracketColor = new Setting("BracketColor", this, new ArrayList<>(Arrays.asList("Black", "Red", "Aqua", "Blue", "Gold", "Gray", "White", "Green", "Yellow", "Purple", "DRed", "DAqua", "DBlue", "DGray", "DGreen", "DPurple", "Reset")));
    private final Setting nameColor = new Setting("NameColor", this, new ArrayList<>(Arrays.asList("Black", "Red", "Aqua", "Blue", "Gold", "Gray", "White", "Green", "Yellow", "Purple", "DRed", "DAqua", "DBlue", "DGray", "DGreen", "DPurple", "Reset")));
    private final Setting messageColor = new Setting("MessageColor", this, new ArrayList<>(Arrays.asList("Black", "Red", "Aqua", "Blue", "Gold", "Gray", "White", "Green", "Yellow", "Purple", "DRed", "DAqua", "DBlue", "DGray", "DGreen", "DPurple", "Reset")));

    public BetterChat(String name, String description, Category category)
    {
        super(name, description, category);
    }


    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event)
    {
        if (mc.player == null || mc.world == null) return;

        String fullMessage = event.getMessage().getUnformattedText();
        String firstPart = fullMessage.split(" ")[0];
        String name = firstPart.replace("<", "").replace(">", "");
        String messageContent = fullMessage.replace(firstPart, "");
        String time = new SimpleDateFormat("k:mm:ss").format(new Date());

        StringBuilder message = new StringBuilder();

        if (!fullMessage.startsWith("<")) return;

        if (timestamp.getBooleanValue())
        {
            add(time, message, timestampBracketColor, timestampBracket, timestampColor);

            switch (timestampBracket.getEnumValue())
            {
                case "<->":
                    message.append(">");
                    break;
                case "[-]":
                    message.append("]");
                    break;
                case "{-}":
                    message.append("}");
                    break;
                default:
                    break;
            }

            message.append(" ");
        }

        add(name, message, bracketColor, bracketStyle, nameColor);

        switch (bracketStyle.getEnumValue())
        {
            case "<->":
                message.append(">");
                break;
            case "[-]":
                message.append("]");
                break;
            case "{-}":
                message.append("}");
                break;
            case ":":
                message.append(":");
                break;
            default:
                break;
        }

        message.append(getColor(messageColor.getEnumValue()));
        message.append(messageContent);

        event.setMessage(new TextComponentString(message.toString()));
    }

    private void add(String name, StringBuilder message, Setting bracketColor, Setting bracketStyle, Setting nameColor)
    {
        message.append(getColor(bracketColor.getEnumValue()));

        switch (bracketStyle.getEnumValue())
        {
            case "<->":
                message.append("<");
                break;
            case "[-]":
                message.append("[");
                break;
            case "{-}":
                message.append("{");
                break;
            default:
                break;
        }

        message.append(getColor(nameColor.getEnumValue()));
        message.append(name);
        message.append(getColor(bracketColor.getEnumValue()));
    }

    public String getColor(String color)
    {
        StringBuilder text = new StringBuilder("\u00A7");

        switch (color)
        {
            case "Black":
                text.append("0");
                break;
            case "DBlue":
                text.append("1");
                break;
            case "DGreen":
                text.append("2");
                break;
            case "DAqua":
                text.append("3");
                break;
            case "DRed":
                text.append("4");
                break;
            case "DPurple":
                text.append("5");
                break;
            case "Gold":
                text.append("6");
                break;
            case "Gray":
                text.append("7");
                break;
            case "DGray":
                text.append("8");
                break;
            case "Blue":
                text.append("9");
                break;
            case "Green":
                text.append("a");
                break;
            case "Aqua":
                text.append("b");
                break;
            case "Red":
                text.append("c");
                break;
            case "Purple":
                text.append("d");
                break;
            case "Yellow":
                text.append("e");
                break;
            case "White":
                text.append("f");
                break;
            case "Reset":
                text.append("r");
                break;
            default:
                break;

        }

        return text.toString();
    }
}