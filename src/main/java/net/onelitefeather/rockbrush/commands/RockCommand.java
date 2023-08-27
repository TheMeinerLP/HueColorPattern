package net.onelitefeather.rockbrush.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import net.onelitefeather.rockbrush.brush.DiamonSquareBrush;
import org.bukkit.entity.Player;


public class RockCommand {


    private final WorldEdit worldEdit;

    public RockCommand(WorldEdit worldEdit) {
        this.worldEdit = worldEdit;
    }


    @CommandMethod("rock diamond <size> <strong> <pattern>")
    public void brush(Player player,
                      @Argument("size") Double size,
                      @Argument("strong") Double strong,
                      @Argument("pattern") Pattern fill) throws WorldEditException {
        var bukkitPlayer = BukkitAdapter.adapt(player);
        var session = bukkitPlayer.getSession();
        worldEdit.checkMaxBrushRadius(size);

        var tool = session.getBrushTool(bukkitPlayer);
        tool.setBrush(new DiamonSquareBrush(strong), "rockbrush.brush.diamond");
        tool.setFill(fill);
        tool.setSize(size);
    }

}
