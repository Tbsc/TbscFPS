package tbsc.fps;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * @author tbsc on 09/11/2021
 */
public class Config {
    private static final ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec CONFIG_SPEC;

    public static ForgeConfigSpec.EnumValue<CounterPosition> POSITION;
    public static ForgeConfigSpec.ConfigValue<String> COUNTER_COLOR;

    static {
        POSITION = CONFIG_BUILDER.comment("Position of the FPS counter on screen")
                .defineEnum("counterPosition", CounterPosition.TOP_LEFT);

        COUNTER_COLOR = CONFIG_BUILDER.comment("Hex color code for the FPS counter")
                .define("counterColor", "#FF0000", it -> {
                    if (it == null) {
                        return false;
                    }
                    return Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", Pattern.CASE_INSENSITIVE)
                            .matcher(it.toString())
                            .matches();
                });

        CONFIG_SPEC = CONFIG_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .autoreload()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }
}
