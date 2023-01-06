package ru.aemmie.cycle.objects;

import java.util.HashMap;
import java.util.Map;

import static ru.aemmie.cycle.objects.Rarity.*;

public enum Weapons {

    K_28(COMMON, "WP_E_Pistol_Bullet_01"),
    K_28_SCRAPPY(COMMON, "WP_E_Pistol_Bullet_01_scrappy"),
    B9_TRENCHGUN(COMMON, "WP_E_SGun_Bullet_01"),
    B9_TRENCHGUN_SCRAPPY(COMMON, "WP_E_SGun_Bullet_01_scrappy"),
    S_576_PDW(COMMON, "WP_E_SMG_Bullet_01"),
    S_576_PDW_SCRAPPY(COMMON, "WP_E_SMG_Bullet_01_scrappy"),
    AR_55(COMMON, "WP_E_AR_Energy_01"),
    AR_55_SCRAPPY(COMMON, "WP_E_AR_Energy_01_scrappy"),
    C_32_BOLT(COMMON, "WP_E_Sniper_Bullet_01"),

    BULLDOG(UNCOMMON, "WP_D_Pistol_Bullet_01"), // ?
    GUARANTEE(RARE, "WP_D_LMG_Energy_01"),
    LACERATOR(RARE, "WP_D_BR_Shard_01"),
    SHATTERGUN(EPIC, "WP_D_SGun_Shard_01"),
    ADVOCATE(EPIC, "WP_D_AR_Bullet_01"),
    VOLTAIC_BRUTE(EXOTIC, "WP_D_SMG_Energy_01"),
    KINETIC_ARBITER(EXOTIC, "WP_D_Sniper_Gauss_01"),

    SCRAPPER(UNCOMMON, "WP_A_SMG_Shard_01"),
    MAELSTORM(RARE, "WP_A_SGun_Energy_01"),
    LONGSHOT(EPIC, "WP_A_BR_Bullet_01"),
    HAMMER(EXOTIC, "WP_A_Pistol_Bullet_01"),
    KOR(EXOTIC, "WP_A_AR_Bullet_01"),

    SCARAB(UNCOMMON, "WP_G_Pistol_Energy_01"),
    MANTICORE(UNCOMMON, "WP_G_AR_Needle_01"),
    PHASIC_LANCER(RARE, "WP_G_AR_Energy_01"),
    FLECHETTE_GUN(EPIC, "WP_G_SMG_Needle_01"),
    GORGON(EPIC, "WP_G_AR_Beam_01"),
    BASILISK(EXOTIC, "WP_G_Sniper_Energy_01"),

    KARMA(LEGENDARY, "WP_A_HVY_Shell_01"), //?
    KOMRAD(LEGENDARY, "WP_A_Launch_MSL_01"),
    ZEUS(LEGENDARY, "WP_G_HVY_Beam_01"),

    KNIFE(RAINBOW, "Melee_Knife_01"),

    SHOCK_GRENADE_1(COMMON, "ShockGrenade_01"),
    SHOCK_GRENADE_2(UNCOMMON, "ShockGrenade_02"),
    SHOCK_GRENADE_3(RARE, "ShockGrenade_03"),
    SHOCK_GRENADE_4(EPIC, "ShockGrenade_04"),   //?
    SHOCK_GRENADE_5(EXOTIC, "ShockGrenade_05"), //?
    GAS_GRENADE(UNCOMMON, "Consumable_GasGrenade_01"),

    SUICIDE(COMMON, "suicide"),
    HEIGHT(COMMON, "height"),
    STRIDER(COMMON, "strider"),
    RATTLER(COMMON, "rattler"),
    WEREMOLE(RAINBOW, "weremole"),
    UNKNOWN(COMMON, "");

    public final Rarity rarity;
    private final String bullet;

    Weapons(Rarity rarity, String bullet) {
        this.rarity = rarity;
        this.bullet = bullet;
    }

    private static final Map<String, Weapons> bulletMap = new HashMap<>();

    static {
        for (var weapon : Weapons.values()) {
            if (!weapon.bullet.equals("")) {
                bulletMap.put(weapon.bullet.toLowerCase(), weapon);
            }
        }
    }

    public static Weapons parse(String weapon) {
        return bulletMap.getOrDefault(weapon.toLowerCase(), Weapons.UNKNOWN);
    }

    public String toColorfulString() {
        return rarity.colorize(toString());
    }
}
