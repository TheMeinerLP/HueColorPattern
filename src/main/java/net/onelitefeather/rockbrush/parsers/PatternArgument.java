package net.onelitefeather.rockbrush.parsers;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.pattern.Pattern;
import org.apiguardian.api.API;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;

public final class PatternArgument<C> extends CommandArgument<C, Pattern> {
    public PatternArgument(final boolean required,
                           final @NonNull String name,
                           final @NonNull String defaultValue,
                           final @Nullable BiFunction<@NonNull CommandContext<C>, @NonNull String,
                                   @NonNull List<@NonNull String>> suggestionsProvider,
                           final @NonNull ArgumentDescription defaultDescription) {
        super(required, name, new PatternParser<>(), defaultValue, Pattern.class, suggestionsProvider, defaultDescription);
    }

    /**
     * Create a new {@link Builder}.
     *
     * @param name argument name
     * @param <C>  sender type
     * @return new {@link Builder}
     * @since 1.8.0
     */
    @API(status = API.Status.STABLE, since = "1.8.0")
    public static <C> @NonNull Builder<C> builder(final @NonNull String name) {
        return new Builder<>(name);
    }

    /**
     * Create a new builder
     *
     * @param name Name of the argument
     * @param <C>  Command sender type
     * @return Created builder
     * @deprecated prefer {@link #builder(String)}
     */
    @API(status = API.Status.DEPRECATED, since = "1.8.0")
    @Deprecated
    public static <C> @NonNull Builder<C> newBuilder(final @NonNull String name) {
        return builder(name);
    }

    /**
     * Create a new required command component
     *
     * @param name Component name
     * @param <C>  Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, Pattern> of(final @NonNull String name) {
        return PatternArgument.<C>builder(name).asRequired().build();
    }

    /**
     * Create a new optional command component
     *
     * @param name Component name
     * @param <C>  Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, Pattern> optional(final @NonNull String name) {
        return PatternArgument.<C>builder(name).asOptional().build();
    }

    /**
     * Create a new required command component with a default value
     *
     * @param name          Component name
     * @param defaultPlayer Default player
     * @param <C>           Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, Pattern> optional(
            final @NonNull String name,
            final @NonNull String defaultPlayer
    ) {
        return PatternArgument.<C>builder(name).asOptionalWithDefault(defaultPlayer).build();
    }


    public static final class Builder<C> extends CommandArgument.Builder<C, Pattern> {

        private Builder(final @NonNull String name) {
            super(Pattern.class, name);
        }

        /**
         * Builder a new boolean component
         *
         * @return Constructed component
         */
        @Override
        public @NonNull PatternArgument<C> build() {
            return new PatternArgument<>(
                    this.isRequired(),
                    this.getName(),
                    this.getDefaultValue(),
                    this.getSuggestionsProvider(),
                    this.getDefaultDescription()
            );
        }
    }

    public static final class PatternParser<C> implements ArgumentParser<C, Pattern> {

        @Override
        public @NonNull ArgumentParseResult<@NonNull Pattern> parse(@NonNull CommandContext<@NonNull C> commandContext, @NonNull Queue<@NonNull String> inputQueue) {
            final String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(PatternParser.class, commandContext));
            }
            if (commandContext.getSender() instanceof ConsoleCommandSender) {
                return ArgumentParseResult.failure(new IllegalArgumentException("Only for players"));
            }
            Pattern pattern;
            try {
                var context = new ParserContext();
                Player player = (Player) commandContext.getSender();
                Actor actor = BukkitAdapter.adapt(player);
                context.setActor(actor);
                context.setSession(actor.getSession());
                context.setWorld(actor instanceof com.sk89q.worldedit.entity.Player wePlayer ? wePlayer.getWorld() : null);
                context.setRestricted(false);
                pattern = WorldEdit.getInstance().getPatternFactory().parseFromInput(input, context);
            } catch (InputParseException e) {
                return ArgumentParseResult.failure(e);
            }
            inputQueue.remove();
            return ArgumentParseResult.success(pattern);
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(@NonNull CommandContext<C> commandContext, @NonNull String input) {
            return WorldEdit.getInstance().getPatternFactory().getSuggestions(input);
        }
    }
}
