package dev.isxander.handsway.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.handsway.config.HandSwayConfig;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.slider.FloatSliderController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.util.function.Function;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            HandSwayConfig config = HandSwayConfig.getInstance();
            HandSwayConfig defaults = HandSwayConfig.DEFAULTS;

            Function<Float, Text> twoDecFormat = value -> Text.of(String.format("%,.2f", value));
            Function<Float, Text> twoDecMultFormat = value -> Text.of(String.format("%,.2fx", value));
            return YetAnotherConfigLib.createBuilder()
                    .title(Text.of("Hand Sway"))
                    .category(ConfigCategory.createBuilder()
                            .name(Text.translatable("hand-sway.title"))
                            .group(OptionGroup.createBuilder()
                                    .name(Text.translatable("hand-sway.group.general"))
                                    .option(Option.createBuilder(float.class)
                                            .name(Text.translatable("hand-sway.option.swayIntensity"))
                                            .tooltip(Text.translatable("hand-sway.option.swayIntensity.tooltip"))
                                            .binding(
                                                    defaults.swayIntensity,
                                                    () -> config.swayIntensity,
                                                    value -> config.swayIntensity = value
                                            )
                                            .controller(opt -> new FloatSliderController(opt, 0f, 5f, 0.05f, twoDecFormat))
                                            .build())
                                    .option(Option.createBuilder(float.class)
                                            .name(Text.translatable("hand-sway.option.swaySpeed"))
                                            .tooltip(Text.translatable("hand-sway.option.swaySpeed.tooltip"))
                                            .binding(
                                                    defaults.swaySpeed,
                                                    () -> config.swaySpeed,
                                                    value -> config.swaySpeed = value
                                            )
                                            .controller(opt -> new FloatSliderController(opt, 0f, 5f, 0.05f, twoDecMultFormat))
                                            .build())
                                    .build())
                            .group(OptionGroup.createBuilder()
                                    .name(Text.of("Arms"))
                                    .option(Option.createBuilder(float.class)
                                            .name(Text.translatable("hand-sway.option.armDeviation"))
                                            .tooltip(Text.translatable("hand-sway.option.armDeviation.tooltip"))
                                            .binding(
                                                    defaults.armDeviation,
                                                    () -> config.armDeviation,
                                                    value -> config.armDeviation = value
                                            )
                                            .controller(opt -> new FloatSliderController(opt, -5f, 5f, 0.05f, twoDecFormat))
                                            .build())
                                    .option(Option.createBuilder(boolean.class)
                                            .name(Text.translatable("hand-sway.option.invertOtherArm"))
                                            .tooltip(Text.translatable("hand-sway.option.invertOtherArm.tooltip"))
                                            .binding(
                                                    defaults.invertOtherArm,
                                                    () -> config.invertOtherArm,
                                                    value -> config.invertOtherArm = value
                                            )
                                            .controller(TickBoxController::new)
                                            .build())
                                    .build())
                            .group(OptionGroup.createBuilder()
                                    .name(Text.of("Advanced"))
                                    .collapsed(true)
                                    .option(Option.createBuilder(float.class)
                                            .name(Text.translatable("hand-sway.option.xSpeedModifier"))
                                            .tooltip(Text.translatable("hand-sway.option.xSpeedModifier.tooltip"))
                                            .binding(
                                                    defaults.xSpeedModifier,
                                                    () -> config.xSpeedModifier,
                                                    value -> config.xSpeedModifier = value
                                            )
                                            .controller(opt -> new FloatSliderController(opt, 0f, 5f, 0.05f, twoDecMultFormat))
                                            .build())
                                    .option(Option.createBuilder(int.class)
                                            .name(Text.translatable("hand-sway.option.xIntensityDampener"))
                                            .tooltip(Text.translatable("hand-sway.option.xIntensityDampener.tooltip"))
                                            .binding(
                                                    defaults.xIntensityDampener,
                                                    () -> config.xIntensityDampener,
                                                    value -> config.xIntensityDampener = value
                                            )
                                            .controller(opt -> new IntegerSliderController(opt, 1, 512, 1))
                                            .build())
                                    .option(Option.createBuilder(float.class)
                                            .name(Text.translatable("hand-sway.option.ySpeedModifier"))
                                            .tooltip(Text.translatable("hand-sway.option.ySpeedModifier.tooltip"))
                                            .binding(
                                                    defaults.ySpeedModifier,
                                                    () -> config.ySpeedModifier,
                                                    value -> config.ySpeedModifier = value
                                            )
                                            .controller(opt -> new FloatSliderController(opt, 0f, 5f, 0.05f, twoDecMultFormat))
                                            .build())
                                    .option(Option.createBuilder(int.class)
                                            .name(Text.translatable("hand-sway.option.yIntensityDampener"))
                                            .tooltip(Text.translatable("hand-sway.option.yIntensityDampener.tooltip"))
                                            .binding(
                                                    defaults.yIntensityDampener,
                                                    () -> config.yIntensityDampener,
                                                    value -> config.yIntensityDampener = value
                                            )
                                            .controller(opt -> new IntegerSliderController(opt, 1, 512, 1))
                                            .build())
                                    .build())
                            .build())
                    .save(config::save)
                    .build().generateScreen(parent);
        };
    }
}
