package dev.kohimanayagato.serenity.api.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class HoleUtil extends Vec3i
{
    private final BlockPos blockPos;
    private final HoleTypes holeTypes;
    private boolean tall;

    public HoleUtil(int x, int y, int z, final BlockPos pos, HoleTypes type)
    {
        super(x, y, z);
        blockPos = pos;
        this.holeTypes = type;
    }

    public HoleUtil(int x, int y, int z, final BlockPos pos, HoleTypes type, boolean tall)
    {
        super(x, y, z);
        blockPos = pos;
        this.tall = tall;
        this.holeTypes = type;
    }

    public boolean isTall()
    {
        return tall;
    }

    public BlockPos getBlockPos()
    {
        return blockPos;
    }

    public HoleTypes getHoleTypes()
    {
        return holeTypes;
    }

    public enum HoleTypes
    {
        None,
        Normal,
        Obsidian,
        Bedrock,
    }
}