package net.onelitefeather.gradientpattern.pattern;

import com.fastasyncworldedit.core.util.TextureHolder;
import com.fastasyncworldedit.core.util.TextureUtil;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.pattern.AbstractPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HueColorPattern extends AbstractPattern {
    private final TextureHolder holder;
    private final Extent extent;

    private final Integer steps;

    private final Color color1;
    private final Color color2;

    private final List<Integer> colorSteps = new ArrayList<>();

    public HueColorPattern(final Extent extent,final TextureHolder holder, final int steps, final Color color1, final Color color2) {
        this.holder = holder;
        this.extent = extent;
        this.steps = steps;
        this.color1 = color1;
        this.color2 = color2;
        updateSteps();
    }

    private void updateSteps() {
        colorSteps.add(color1.getRGB());
        // Für jeden Schritt zwischen den beiden Farben
        for (int bsCnt = 1; bsCnt < steps - 1; bsCnt++) {
            float ratio = (float) bsCnt / (steps - 1);

            int red = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
            int green = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
            int blue = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);
            var newColor = new Color(red, green, blue);
            colorSteps.add(newColor.getRGB());
        }
        colorSteps.add(color2.getRGB());
    }


    @Override
    public BaseBlock applyBlock(final BlockVector3 position) {
        BlockType type = extent.getBlock(position).getBlockType();
        TextureUtil util = holder.getTextureUtil();
        int currentColor;
        if (type == BlockTypes.GRASS_BLOCK) {
            currentColor = holder.getTextureUtil().getColor(extent.getBiome(position));
        } else {
            currentColor = holder.getTextureUtil().getColor(type);
        }
        var newColor = findClosestColor(currentColor);
        return Optional.ofNullable(util.getNearestBlock(newColor)).orElse(type).getDefaultState().toBaseBlock();
    }

    public int findClosestColor(int targetColor) {
        int closestColor = colorSteps.get(0);
        double minDistance = Double.MAX_VALUE;

        for (int color : colorSteps) {
            double distance = colorDistance(new Color(color), new Color(targetColor));
            if (distance < minDistance) {
                minDistance = distance;
                closestColor = color;
            }
        }

        return closestColor;
    }

    public double colorDistance(Color c1, Color c2) {
        double rDiff = c1.getRed() - c2.getRed();
        double gDiff = c1.getGreen() - c2.getGreen();
        double bDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
}
