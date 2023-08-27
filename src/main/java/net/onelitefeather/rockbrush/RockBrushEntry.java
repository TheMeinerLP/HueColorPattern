package net.onelitefeather.rockbrush;

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.function.pattern.Pattern;
import io.leangen.geantyref.TypeToken;
import net.onelitefeather.rockbrush.commands.RockCommand;
import net.onelitefeather.rockbrush.parsers.PatternArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Function;

public class RockBrushEntry extends JavaPlugin {

    private final Function<ParserParameters, CommandMeta> commandMetaFunction = (p) -> CommandMeta
            .simple()
            .with(CommandMeta.DESCRIPTION, p.get(StandardParameters.DESCRIPTION, "No description"))
            .build();

    @Override
    public void onEnable() {

        try {
            PaperCommandManager<CommandSender> paperCommandManager = new PaperCommandManager<>(
                    this,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );
            if (paperCommandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
                paperCommandManager.registerBrigadier();
                getLogger().info("Brigadier support enabled");
            }
            if (paperCommandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                paperCommandManager.registerAsynchronousCompletions();
                getLogger().info("Asynchronous completions enabled");
            }
            paperCommandManager.parserRegistry().registerParserSupplier(TypeToken.get(Pattern.class), parserParameters -> new PatternArgument.PatternParser<>());
            var annotationParser = new AnnotationParser<>(paperCommandManager, CommandSender.class, commandMetaFunction);
            annotationParser.parse(new RockCommand(WorldEdit.getInstance()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
