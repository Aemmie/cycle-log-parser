package ru.aemmie.cycle.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.aemmie.cycle.objects.Rarity.*;

public enum Weapon {

    K_28_SCRAPPY("K_28 (Scrappy)", COMMON, "WP_E_Pistol_Bullet_01_scrappy"),
    K_28("K_28", COMMON, "WP_E_Pistol_Bullet_01"),
    B9_TRENCHGUN_SCRAPPY("B9_Trenchgun (Scrappy)", COMMON, "WP_E_SGun_Bullet_01_scrappy"),
    B9_TRENCHGUN("B9_Trenchgun", COMMON, "WP_E_SGun_Bullet_01"),
    S_576_PDW_SCRAPPY("S_576 (Scrappy)", COMMON, "WP_E_SMG_Bullet_01_scrappy"),
    S_576_MK1("S_576", COMMON, "WP_E_SMG_Bullet_01"),
    S_576_MK2("S_576", UNCOMMON, "WP_E_SMG_Bullet_02"),
    AR_55_SCRAPPY("AR_55 (Scrappy)", COMMON, "WP_E_AR_Energy_01_scrappy"),
    AR_55_MK1("AR_55", COMMON, "WP_E_AR_Energy_01"),
    AR_55_MK2("AR_55", UNCOMMON, "WP_E_AR_Energy_02"),
    C_32_BOLT_MK1("C_32_Bolt", COMMON, "WP_E_Sniper_Bullet_01"),
    C_32_BOLT_MK2("C_32_Bolt", UNCOMMON, "WP_E_Sniper_Bullet_02"),

    BULLDOG("Bulldog", UNCOMMON, "WP_D_Pistol_Bullet_01"),
    GUARANTEE_MK1("Guarantee", UNCOMMON, "WP_D_LMG_Energy_02"),
    GUARANTEE_MK2("Guarantee", RARE, "WP_D_LMG_Energy_01"),
    LACERATOR("Lacerator", RARE, "WP_D_BR_Shard_01"),
    SHATTERGUN("Shattergun", EPIC, "WP_D_SGun_Shard_01"),
    ADVOCATE("Advocate", EPIC, "WP_D_AR_Bullet_01"),
    VOLTAIC_BRUTE("Voltaic_brute", EXOTIC, "WP_D_SMG_Energy_01"),
    KINETIC_ARBITER("Kinetic_arbiter", EXOTIC, "WP_D_Sniper_Gauss_01"),

    SCRAPPER("Scrapper", UNCOMMON, "WP_A_SMG_Shard_01"),
    MAELSTORM("Maelstorm", RARE, "WP_A_SGun_Energy_01"),
    LONGSHOT_MK1("Longshot", RARE, "WP_A_BR_Bullet_02"),
    LONGSHOT_MK2("Longshot", EPIC, "WP_A_BR_Bullet_01"),
    HAMMER_MK1("Hammer", RARE, "WP_A_Pistol_Bullet_02"),
    HAMMER_MK2("Hammer", EXOTIC, "WP_A_Pistol_Bullet_01"),
    KOR("KOR", EXOTIC, "WP_A_AR_Bullet_01"),

    SCARAB_MK1("Scarab", UNCOMMON, "WP_G_Pistol_Energy_01"),
    SCARAB_MK2("Scarab", RARE, "WP_G_Pistol_Energy_02"),
    MANTICORE_MK1("Manticore", UNCOMMON, "WP_G_AR_Needle_01"),
    MANTICORE_MK2("Manticore", RARE, "WP_G_AR_Needle_02"),
    PHASIC_LANCER("Phasic Lancer", RARE, "WP_G_AR_Energy_01"),
    FLECHETTE_GUN_MK1("Flechette Gun", RARE, "WP_G_SMG_Needle_02"),
    FLECHETTE_GUN_MK2("Flechette Gun", EPIC, "WP_G_SMG_Needle_01"),
    GORGON("Gorgon", EPIC, "WP_G_AR_Beam_01"),
    BASILISK("Basilisk", EXOTIC, "WP_G_Sniper_Energy_01"),

    KARMA_MK1("KARMA", EPIC, "WP_A_Sniper_Gauss_02"),
    KARMA_MK2("KARMA", LEGENDARY, "WP_A_Sniper_Gauss_01"),
    KOMRAD("KOMRAD", LEGENDARY, "WP_A_Launch_MSL_01"),
    ZEUS_MK1("ZEUS", EPIC, "WP_G_HVY_Beam_02"),
    ZEUS_MK2("ZEUS", LEGENDARY, "WP_G_HVY_Beam_01"),

    KNIFE("Knife", RAINBOW, "Melee_Knife_01"),

    SHOCK_GRENADE_1("Shock Grenade", COMMON, "ShockGrenade_01"),
    SHOCK_GRENADE_2("Shock Grenade", UNCOMMON, "ShockGrenade_02"),
    SHOCK_GRENADE_3("Shock Grenade", RARE, "ShockGrenade_03"),
    SHOCK_GRENADE_4("Shock Grenade", EPIC, "ShockGrenade_04"),   //?
    SHOCK_GRENADE_5("Shock Grenade", EXOTIC, "ShockGrenade_05"), //?
    GAS_GRENADE("Gas Grenade", UNCOMMON, "Consumable_GasGrenade_01"),

    NONE("None?", COMMON, "None"),
    SUICIDE("Suicide", COMMON),
    HEIGHT("Fall", UNCOMMON),
    LIGHTNING_STRIKE("Lightning Strike", RARE, "LightningStrike_BP"),

    PLAYER("Player", COMMON, "PRO_PlayerCharacter"),
    STRIDER("Strider", COMMON, "AIChar_Strider_BP"),
    RATTLER("Rattler", UNCOMMON, "AIChar_Rattler_BP"),
    CRUSHER("Crusher", EPIC, "AIChar_Crusher_BP"),
    WEREMOLE("Jeff", RAINBOW, "AIChar_Weremole_BP"),
    HOWLER("Howler", RAINBOW, "AIChar_Howler_BP"),


    VOLUME_2_4("Volume (Outside)", EXOTIC, "YDamageVolume2_4"),
    VOLUME_3_6("Volume (idk where)", EXOTIC, "YDamageVolume3_6"),   //TODO: where?
    VOLUME_4("Volume (Square Staircase)", EXOTIC, "YDamageVolume4"),
    VOLUME_5("Volume (Crystals)", EXOTIC, "YDamageVolume5"),
    VOLUME_6("Volume (Forge)", EXOTIC, "YDamageVolume6"),



    ;


    public final String name;
    public final Rarity rarity;
    private final List<String> logNames;

    Weapon(String name, Rarity rarity, String ... logNames) {
        this.name = name;
        this.rarity = rarity;
        this.logNames = List.of(logNames);
    }

    private static final Map<String, Weapon> bulletMap = new HashMap<>();

    static {
        for (var weapon : Weapon.values()) {
            for (var ln : weapon.logNames) {
                bulletMap.put(ln.toLowerCase(), weapon);
            }
        }
    }

    public static Weapon parse(String weapon) {
        return bulletMap.get(weapon.toLowerCase());
    }

    public String toColorfulString() {
        return rarity.colorize(name);
    }
}
