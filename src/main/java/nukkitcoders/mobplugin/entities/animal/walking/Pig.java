package nukkitcoders.mobplugin.entities.animal.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.animal.WalkingAnimal;
import nukkitcoders.mobplugin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Pig extends WalkingAnimal implements EntityRideable {

    public static final int NETWORK_ID = 12;

    public Pig(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public boolean isBaby() {
        return getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    public void initEntity() {
        super.initEntity();
        fireProof = false;
        setMaxHealth(10);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed
                    && (player.getInventory().getItemInHand().getId() == Item.CARROT
                    || player.getInventory().getItemInHand().getId() == Item.POTATO
                    || player.getInventory().getItemInHand().getId() == Item.BEETROOT)
                    && distance <= 49;
        }
        return false;
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (item.equals(Item.get(Item.CARROT,0)) && !isBaby()) {
            player.getInventory().removeItem(Item.get(Item.CARROT,0,1));
            level.addSound(this, Sound.RANDOM_EAT);
            level.addParticle(new ItemBreakParticle(add(0,getMountedYOffset(),0),Item.get(Item.CARROT)));
            setInLove();
            return true;
        }else if (item.equals(Item.get(Item.POTATO,0)) && !isBaby()) {
            player.getInventory().removeItem(Item.get(Item.POTATO,0,1));
            level.addSound(this,Sound.RANDOM_EAT);
            level.addParticle(new ItemBreakParticle(add(0,getMountedYOffset(),0),Item.get(Item.POTATO)));
            setInLove();
            return true;
        }else if (item.equals(Item.get(Item.BEETROOT,0)) && !isBaby()) {
            player.getInventory().removeItem(Item.get(Item.BEETROOT,0,1));
            level.addSound(this,Sound.RANDOM_EAT);
            level.addParticle(new ItemBreakParticle(add(0,getMountedYOffset(),0),Item.get(Item.BEETROOT)));
            setInLove();
            return true;
        }
        return false;
    }

    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (lastDamageCause instanceof EntityDamageByEntityEvent) {
            int drop = Utils.rand(1, 4); // drops 1-3 raw porkchop / cooked porkchop when on fire
            for (int i = 0; i < drop; i++) {
                drops.add(Item.get(isOnFire() ? Item.COOKED_PORKCHOP : Item.RAW_PORKCHOP, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    public int getKillExperience() {
        return Utils.rand(1, 4); // gain 1-3 experience
    }

    @Override
    public boolean mountEntity(Entity entity) {
        return false;
    }

}