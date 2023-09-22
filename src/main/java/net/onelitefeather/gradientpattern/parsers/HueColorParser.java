package net.onelitefeather.gradientpattern.parsers;

import com.fastasyncworldedit.core.configuration.Caption;
import com.fastasyncworldedit.core.extension.factory.parser.RichParser;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.command.util.SuggestionHelper;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import java.awt.Color;
import java.util.stream.Stream;
import net.onelitefeather.gradientpattern.pattern.HueColorPattern;
import org.jetbrains.annotations.NotNull;

public class HueColorParser extends RichParser<Pattern> {

    /**
     * Create a new rich parser with a defined prefix for the result, e.g. {@code #simplex}.
     *
     * @param worldEdit the worldedit instance.
     */
    public HueColorParser(final WorldEdit worldEdit) {
        super(worldEdit, "#huecolor");
    }

    @Override
    protected Stream<String> getSuggestions(final String argumentInput, final int index) {
        if (index > 2) {
            return Stream.empty();
        }
        return SuggestionHelper.suggestPositiveIntegers(argumentInput);
    }

    @Override
    protected Pattern parseFromInput(@NotNull final String[] input, final ParserContext context) throws
            InputParseException {
        if (input.length != 3) {
            throw new InputParseException(Caption.of(
                    "fawe.error.command.syntax",
                    TextComponent.of(getPrefix() + "[steps][hex1][hex2] (e.g. " + getPrefix() + "[9][#FF5733][FF5733])")
            ));
        }
        final var steps = Integer.parseInt(input[0]);
        final Color color1 = hexToColor(input[1]);

        final Color color2 = hexToColor(input[2]);
        return new HueColorPattern(
                context.requireExtent(),
                context.requireSession(),
                steps,
                color1,
                color2
        );
    }

    private Color hexToColor(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        final int intValue = Integer.parseInt(hex, 16);
        return new Color((intValue >> 16) & 0xFF, (intValue >> 8) & 0xFF, intValue & 0xFF, 255);
    }
}
