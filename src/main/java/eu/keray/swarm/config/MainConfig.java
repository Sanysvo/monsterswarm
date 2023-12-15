package eu.keray.swarm.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MainConfig {
    public static ForgeConfigSpec.BooleanValue ENABLE_DIGGING;
    public static ForgeConfigSpec.BooleanValue ENABLE_BUILDING;
    public static ForgeConfigSpec.BooleanValue ENABLE_SOUNDS;
    //	public static boolean SHOOT_ROCKET = true;
//	public static double  ROCKET_FREQUENCY = 0.05f;
    public static ForgeConfigSpec.BooleanValue KILL_MOBS_DAYTIME;
    public static ForgeConfigSpec.BooleanValue ATTACK_ANIMALS;
    public static ForgeConfigSpec.BooleanValue ENABLE_SPRINTING;
    public static ForgeConfigSpec.BooleanValue UNDERGROUND;
    public static ForgeConfigSpec.ConfigValue<Integer> AGGRO_RANGE;
    public static ForgeConfigSpec.ConfigValue<Integer> MAX_RESISTANCE;
    public static ForgeConfigSpec.ConfigValue<Integer> MAX_MONSTERS_IN_WORLD;
    public static ForgeConfigSpec.ConfigValue<Double> RESISTANCE_MULTIPLIER;


    public MainConfig(ForgeConfigSpec.Builder builder) {
        UNDERGROUND = builder.comment("Если установлено значение True, монстры всегда будут охотиться на цели, которые находятся под землей (ниже 40 уровня)").define("underground", true);
        ENABLE_DIGGING = builder.comment("Если установлено значение True, монстры смогут копать сквозь стены").define("enable_digging", true);
        ENABLE_BUILDING  = builder.comment("Если установить значение True, монстры смогут строить мосты и лестницы, чтобы подняться выше").define("enable_building", true);
        ENABLE_SPRINTING  = builder.comment("Если установлено значение True, монстры будут спринтовать, если их цель спринтует").define("enable_sprinting", false);
        KILL_MOBS_DAYTIME  = builder.comment("Если установлено значение True, все роящиеся мобы будут гореть при дневном свете. (полезно избавиться от лиан после ночи)").define("KILL_MOBS_DAYTIME", true);
        ATTACK_ANIMALS = builder.comment("Если установлено значение True, мобы будут нацелены на животных.").define("attack_animals", true);
        ENABLE_SOUNDS  = builder.comment("Если установлено значение True, копание будет воспроизводить указывающий звук (это конфигурация на стороне сервера)").define("enable_sound", true);
        AGGRO_RANGE = builder.comment("С какого расстояния монстры могут видеть вас (Рой под землей игнорирует это значение!)").define("aggro_range", 120);
        RESISTANCE_MULTIPLIER = builder.comment("Множитель для блока сопротивления против Роя. Установите его на 2,0, и стены будут в два раза устойчивее к рою. Установите его на 0 и все упадет на один удар").define("RESISTANCE_MULTIPLIER", 0.8);
        MAX_RESISTANCE = builder.comment("Сопротивление блока будет ограничено до этого значения, когда монстры копнут его.").define("MAX_RESISTANCE", 90);
        MAX_MONSTERS_IN_WORLD = builder.comment("Максимальное число монстров в мире.").define("MAX_MONSTERS_IN_WORLD", 200);

    }
}
