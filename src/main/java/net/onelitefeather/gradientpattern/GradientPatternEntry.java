package net.onelitefeather.gradientpattern;

import com.sk89q.worldedit.WorldEdit;
import net.onelitefeather.gradientpattern.parsers.HueColorParser;
import org.bukkit.plugin.java.JavaPlugin;

public class GradientPatternEntry extends JavaPlugin {

    @Override
    public void onEnable() {
        WorldEdit.getInstance().getPatternFactory().register(new HueColorParser(WorldEdit.getInstance()));
    }
}
