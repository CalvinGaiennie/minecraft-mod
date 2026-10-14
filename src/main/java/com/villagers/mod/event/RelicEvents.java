package com.villagers.mod.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.villagers.mod.Config;
import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.relic.KingRelicItems;
import com.villagers.mod.relic.RelicCooldownSavedData;
import com.villagers.mod.relic.RelicItemUtils;
import com.villagers.mod.relic.RelicKind;
@EventBusSubscriber(modid = VillagersMod.MODID)
public class RelicEvents {
    @SubscribeEvent
    public static void onItemJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        if (event.getEntity() instanceof ItemEntity itemEntity && RelicItemUtils.isKingRelic(itemEntity.getItem())) {
            itemEntity.setUnlimitedLifetime();
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (!RelicItemUtils.isKingRelic(event.getTo())) {
            return;
        }
        if (RelicItemUtils.necromancerBlockedFromRelics(player)) {
            player.setItemSlot(event.getSlot(), event.getFrom());
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack main = event.getEntity().getMainHandItem();
        if (RelicItemUtils.kind(main) == RelicKind.STORMCALLER && event.getState().is(net.minecraft.tags.BlockTags.LOGS)) {
            event.setNewSpeed(event.getNewSpeed() * 2.0f);
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof LivingEntity living)) {
            return;
        }
        if (RelicItemUtils.kind(living.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET)) == RelicKind.STORMSTRIDERS) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        RelicKind head = RelicItemUtils.kind(player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD));
        if (head == RelicKind.THORNHEART_CIRCLET || head == RelicKind.IRONROOT_HELM) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, false, false, true));
        }
        if (RelicItemUtils.kind(player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS)) == RelicKind.GREAVES_LONG_MARCH) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1, false, false, true));
            if (player.getFoodData().needsFood()) {
                player.getFoodData().eat(1, 0.75f);
            }
        }
        if (RelicItemUtils.kind(player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET)) == RelicKind.STORMSTRIDERS
                && player.isInWater()) {
            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 40, 1, false, false, true));
        }
    }

    @SubscribeEvent
    public static void onDamagePre(LivingDamageEvent.Pre event) {
        if (!Config.RELICS_ENABLED.get()) {
            return;
        }
        LivingEntity victim = event.getEntity();
        float damage = event.getNewDamage();
        Entity attackerEntity = event.getSource().getEntity();

        if (attackerEntity instanceof LivingEntity attacker) {
            RelicKind weaponKind = relicWeaponKind(event.getSource(), attacker);
            if (weaponKind != null) {
                damage *= RelicItemUtils.RELIC_DAMAGE_MULTIPLIER;
                damage = applyWeaponEffect(weaponKind, attacker, victim, damage, event);
            }
        }

        if (victim instanceof Player player && RelicItemUtils.necromancerBlockedFromRelics(player)) {
            // Necromancers cannot benefit from relic armor while on kit
        } else {
            damage = applyArmorReduction(victim, damage);
            damage = applyOathkeeperAura(victim, damage);
        }

        if (victim instanceof Player player) {
            damage = tryLastMantleOrCrown(player, damage, event);
        }

        if (attackerEntity instanceof LivingEntity undeadAttacker
                && undeadAttacker.getType().is(EntityTypeTags.UNDEAD)
                && RelicItemUtils.kind(victim.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD)) == RelicKind.IRONROOT_HELM) {
            damage *= RelicItemUtils.IRONROOT_UNDEAD_DAMAGE_FACTOR;
        }

        event.setNewDamage(Math.max(0, damage));
    }

    @org.jetbrains.annotations.Nullable
    private static RelicKind relicWeaponKind(DamageSource source, LivingEntity attacker) {
        RelicKind main = RelicItemUtils.kind(attacker.getMainHandItem());
        if (main != null) {
            return main;
        }
        Entity direct = source.getDirectEntity();
        if (direct instanceof ThrownTrident trident) {
            return RelicItemUtils.kind(trident.getWeaponItem());
        }
        return null;
    }

    private static float applyWeaponEffect(RelicKind kind, LivingEntity attacker, LivingEntity victim, float damage, LivingDamageEvent.Pre event) {
        if (kind == RelicKind.DAWNBREAKER && victim.getType().is(EntityTypeTags.UNDEAD)) {
            victim.igniteForSeconds(4.0f);
            return damage * 1.35f;
        }
        if (kind == RelicKind.KINGSMAUL) {
            victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, RelicItemUtils.KINGSMAUL_SLOW_TICKS, 2, false, false, true));
            victim.knockback(
                    RelicItemUtils.KINGSMAUL_KNOCKBACK,
                    attacker.getX() - victim.getX(),
                    attacker.getZ() - victim.getZ());
        }
        if (kind == RelicKind.TIDESPEAR && victim != attacker) {
            applyTidespearPull(attacker, victim);
        }
        return damage;
    }

    private static float applyArmorReduction(LivingEntity victim, float damage) {
        int pieces = 0;
        for (ItemStack stack : victim.getArmorSlots()) {
            if (RelicItemUtils.isKingRelic(stack)) {
                pieces++;
            }
        }
        if (pieces == 0) {
            return damage;
        }
        float factor = (float) Math.pow(RelicItemUtils.ARMOR_DAMAGE_FACTOR_PER_PIECE, pieces);
        return damage * factor;
    }

    private static void applyTidespearPull(LivingEntity attacker, LivingEntity victim) {
        Vec3 toAttacker = attacker.position().subtract(victim.position());
        double len = toAttacker.length();
        if (len < 1.0E-4) {
            return;
        }
        Vec3 pull = toAttacker.scale(RelicItemUtils.TIDESPEAR_PULL_STRENGTH / len);
        victim.setDeltaMovement(victim.getDeltaMovement().add(pull));
        victim.hurtMarked = true;
    }

    private static float applyOathkeeperAura(LivingEntity victim, float damage) {
        AABB box = victim.getBoundingBox().inflate(8);
        for (Player ally : victim.level().getEntitiesOfClass(Player.class, box)) {
            if (ally == victim) {
                continue;
            }
            ItemStack main = ally.getMainHandItem();
            ItemStack off = ally.getOffhandItem();
            if (RelicItemUtils.kind(main) == RelicKind.OATHKEEPER || RelicItemUtils.kind(off) == RelicKind.OATHKEEPER) {
                return damage * RelicItemUtils.OATHKEEPER_ALLY_DAMAGE_FACTOR;
            }
        }
        return damage;
    }

    private static float tryLastMantleOrCrown(Player player, float damage, LivingDamageEvent.Pre event) {
        if (player.getHealth() - damage > 0) {
            return damage;
        }
        if (!(player.level() instanceof ServerLevel server)) {
            return damage;
        }
        int day = RelicCooldownSavedData.currentDay(server);
        RelicCooldownSavedData cd = RelicCooldownSavedData.get(server);

        if (RelicItemUtils.kind(player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST)) == RelicKind.LAST_MANTLE
                && cd.canUseLastMantle(player.getUUID(), day)) {
            cd.markLastMantle(player.getUUID(), day);
            player.setHealth(1.0f);
            server.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY() + 1, player.getZ(), 12, 0.3, 0.5, 0.3, 0.05);
            return 0;
        }
        if (RelicItemUtils.kind(player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD)) == RelicKind.CORRUPTED_KINGS_CROWN
                && cd.canUseCrownSave(player.getUUID(), day)) {
            cd.markCrownSave(player.getUUID(), day);
            player.setHealth(1.0f);
            player.knockback(1.5, 0, 0);
            return 0;
        }
        return damage;
    }

    @SubscribeEvent
    public static void onSoldierDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Villager villager) || villager.level().isClientSide) {
            return;
        }
        if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        if (!(villager.level() instanceof ServerLevel server)) {
            return;
        }
        Player crownBearer = null;
        for (Player player : server.getEntitiesOfClass(Player.class, villager.getBoundingBox().inflate(32))) {
            if (RelicItemUtils.kind(player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD)) == RelicKind.CORRUPTED_KINGS_CROWN) {
                crownBearer = player;
                break;
            }
        }
        if (crownBearer == null) {
            return;
        }
        var pos = villager.blockPosition();
        var name = villager.getCustomName();
        float rot = villager.getYRot();
        server.getServer().submit(() -> scheduleCrownRaise(server, pos, name, rot));
    }

    private static void scheduleCrownRaise(ServerLevel server, net.minecraft.core.BlockPos pos, net.minecraft.network.chat.Component name, float rot) {
        int tick = server.getServer().getTickCount() + 200;
        server.getServer().tell(new net.minecraft.server.TickTask(tick, () -> {
            Zombie risen = new Zombie(server);
            risen.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, rot, 0);
            risen.setCustomName(name);
            risen.setCustomNameVisible(false);
            risen.setBaby(false);
            risen.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400, 1));
            server.addFreshEntity(risen);
            server.getServer().tell(new net.minecraft.server.TickTask(server.getServer().getTickCount() + 2400, risen::discard));
        }));
    }
}
