/*******************************************************************************************************
 * Continued by FlyingPikachu/HappyPikachu with permission from _Blackvein_. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************************************/

package me.blackvein.quests.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import me.blackvein.quests.Quests;

public class QuestMob {

	private String name = null;
	private EntityType entityType = null;
	private Location spawnLocation = null;
	private Integer spawnAmounts = null;
	public ItemStack[] inventory = new ItemStack[5];
	public Float[] dropChances = new Float[5];

	public QuestMob(EntityType entityType, Location spawnLocation, int spawnAmounts) {
		this.entityType = entityType;
		this.spawnLocation = spawnLocation;
		this.spawnAmounts = spawnAmounts;
	}

	public QuestMob() {
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setType(EntityType entityType) {
		this.entityType = entityType;
	}

	public EntityType getType() {
		return entityType;
	}

	public void setSpawnAmounts(int spawnAmounts) {
		this.spawnAmounts = spawnAmounts;
	}

	public Integer getSpawnAmounts() {
		return spawnAmounts;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setHelmet(ItemStack helmet, float dropChance) {
		inventory[4] = helmet;
		dropChances[4] = dropChance;
	}

	public void setChest(ItemStack chest, float dropChance) {
		inventory[3] = chest;
		dropChances[3] = dropChance;
	}

	public void setLeggings(ItemStack leggings, float dropChance) {
		inventory[2] = leggings;
		dropChances[2] = dropChance;
	}

	public void setBoots(ItemStack boots, float dropChance) {
		inventory[1] = boots;
		dropChances[1] = dropChance;
	}

	public void setHeldItem(ItemStack heldItem, float dropChance) {
		inventory[0] = heldItem;
		dropChances[0] = dropChance;
	}

	@SuppressWarnings("deprecation")
	public void spawn() {
		World world = spawnLocation.getWorld();
		for (int i = 0; i < spawnAmounts; i++) {
			LivingEntity entity = (LivingEntity) world.spawnEntity(spawnLocation, entityType);
			if (name != null) {
				entity.setCustomName(name);
				entity.setCustomNameVisible(true);
			}
			EntityEquipment eq = entity.getEquipment();
			try {
				eq.setItemInHand(inventory[0]);
				eq.setBoots(inventory[1]);
				eq.setLeggings(inventory[2]);
				eq.setChestplate(inventory[3]);
				eq.setHelmet(inventory[4]);
			} catch (Exception e) {
				Bukkit.getLogger().severe("Entity NMS is invalid for this version of CraftBukkit. Please notify the developer");
				e.printStackTrace();
			}
			if (dropChances[0] != null) {
				eq.setItemInHandDropChance(dropChances[0]);
			}
			if (dropChances[1] != null) {
				eq.setBootsDropChance(dropChances[1]);
			}
			if (dropChances[2] != null) {
				eq.setLeggingsDropChance(dropChances[2]);
			}
			if (dropChances[3] != null) {
				eq.setChestplateDropChance(dropChances[3]);
			}
			if (dropChances[4] != null) {
				eq.setHelmetDropChance(dropChances[4]);
			}
		}
	}

	public String serialize() {
		String string = "";
		string += "type-" + entityType.name();
		if (name != null) {
			string += "::name-" + name;
		}
		if (spawnLocation != null) {
			string += "::spawn-" + Quests.getLocationInfo(spawnLocation);
		}
		if (spawnAmounts != null) {
			string += "::amounts-" + spawnAmounts;
		}
		if (inventory[0] != null) {
			string += "::hand-" + ItemUtil.serialize(inventory[0]);
			string += "::hand_drop-" + dropChances[0];
		}
		if (inventory[1] != null) {
			string += "::boots-" + ItemUtil.serialize(inventory[1]);
			string += "::boots_drop-" + dropChances[1];
		}
		if (inventory[2] != null) {
			string += "::leggings-" + ItemUtil.serialize(inventory[2]);
			string += "::leggings_drop-" + dropChances[2];
		}
		if (inventory[3] != null) {
			string += "::chest-" + ItemUtil.serialize(inventory[3]);
			string += "::chest_drop-" + dropChances[3];
		}
		if (inventory[4] != null) {
			string += "::helmet-" + ItemUtil.serialize(inventory[4]);
			string += "::helmet_drop-" + dropChances[4];
		}
		return string;
	}

	public static QuestMob fromString(String str) {
		String name = null;
		EntityType entityType = null;
		Location loc = null;
		Integer amounts = null;
		ItemStack[] inventory = new ItemStack[5];
		Float[] dropChances = new Float[5];
		String[] args = str.split("::");
		for (String string : args) {
			if (string.startsWith("type-")) {
				entityType = Quests.getMobType(string.substring(5));
			} else if (string.startsWith("name-")) {
				name = string.substring(5);
			} else if (string.startsWith("spawn-")) {
				loc = Quests.getLocation(string.substring(6));
			} else if (string.startsWith("amounts-")) {
				amounts = Integer.parseInt(string.substring(8));
			} else if (string.startsWith("hand-")) {
				inventory[0] = ItemUtil.readItemStack(string.substring(5));
			} else if (string.startsWith("hand_drop-")) {
				dropChances[0] = Float.parseFloat(string.substring(10));
			} else if (string.startsWith("boots-")) {
				inventory[1] = ItemUtil.readItemStack(string.substring(6));
			} else if (string.startsWith("boots_drop-")) {
				dropChances[1] = Float.parseFloat(string.substring(11));
			} else if (string.startsWith("leggings-")) {
				inventory[2] = ItemUtil.readItemStack(string.substring(9));
			} else if (string.startsWith("leggings_drop-")) {
				dropChances[2] = Float.parseFloat(string.substring(14));
			} else if (string.startsWith("chest-")) {
				inventory[3] = ItemUtil.readItemStack(string.substring(6));
			} else if (string.startsWith("chest_drop-")) {
				dropChances[3] = Float.parseFloat(string.substring(11));
			} else if (string.startsWith("helmet-")) {
				inventory[4] = ItemUtil.readItemStack(string.substring(7));
			} else if (string.startsWith("helmet_drop-")) {
				dropChances[4] = Float.parseFloat(string.substring(12));
			}
		}
		QuestMob qm = new QuestMob(entityType, loc, amounts);
		qm.setName(name);
		qm.inventory = inventory;
		qm.dropChances = dropChances;
		return qm;
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}

	@Override
	public boolean equals(Object o) {
		if ((o instanceof QuestMob) == false) {
			return false;
		}
		QuestMob other = (QuestMob) o;
		if (name != null && other.name != null) {
			if (name.equalsIgnoreCase(other.name) == false) {
				return false;
			}
		} else if (name == null && other.name == null) {
		} else {
			return false;
		}
		if (!entityType.equals(other.entityType)) {
			return false;
		}
		if (dropChances != other.dropChances) {
			return false;
		}
		if (inventory.length == other.inventory.length) {
			for (int i = 0; i < inventory.length; i++) {
				if (ItemUtil.compareItems(inventory[i], other.inventory[i], false) != 0) {
					return false;
				}
			}
		} else {
			return false;
		}
		if (!spawnAmounts.equals(other.spawnAmounts)) {
			return false;
		}
		if (!spawnLocation.equals(other.spawnLocation)) {
			return false;
		}
		return true;
	}
}