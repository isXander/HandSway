package dev.isxander.handsway.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class HandSwayConfig {
    private static final Logger logger = LoggerFactory.getLogger("Hand Sway");
    private static final Path path = FabricLoader.getInstance().getConfigDir().resolve("hand-sway.json");
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    private static HandSwayConfig instance = null;
    public static final HandSwayConfig DEFAULTS = new HandSwayConfig();

    @Expose public float swayIntensity = 0.25f;
    @Expose public float swaySpeed = 1f;

    @Expose public float armDeviation = -0.3f;
    @Expose public boolean invertOtherArm = true;

    @Expose public float xSpeedModifier = 0.85f;
    @Expose public int xIntensityDampener = 256;
    @Expose public float ySpeedModifier = 1.5f;
    @Expose public int yIntensityDampener = 64;

    public void save() {
        try {
            logger.info("Saving HandSway config...");
            Files.writeString(path, gson.toJson(this), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            if (Files.notExists(path)) {
                getInstance().save();
                return;
            }

            logger.info("Loading HandSway config...");
            instance = gson.fromJson(Files.readString(path), HandSwayConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HandSwayConfig getInstance() {
        if (instance == null)
            instance = new HandSwayConfig();

        return instance;
    }
}
