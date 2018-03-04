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

import java.util.LinkedList;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import net.milkbowl.vault.item.Items;

public class ItemUtil {

	/**
	 * Will compare stacks by name, amount, data, display name/lore and enchantments
	 *
	 *
	 * @param one
	 *            ItemStack to compare
	 * @param two
	 *            ItemStack to compare to
	 * @return 0 if stacks are equal, or the first inequality from the following values:<br>
	 * @return -1&nbsp;-> stack names are unequal<br>
	 * @return -2&nbsp;-> stack amounts are unequal<br>
	 * @return -3&nbsp;-> stack data is unequal<br>
	 * @return -4&nbsp;-> stack display name/lore is unequal<br>
	 * @return -5&nbsp;-> stack enchantments are unequal<br>
	 */
	public static int compareItems(ItemStack one, ItemStack two, boolean ignoreAmount) {
		if (one == null || two == null) {
			return 0;
		}
		if (one.getType().name().equals(two.getType().name()) == false) {
			return -1;
		} else if ((one.getAmount() != two.getAmount()) && ignoreAmount == false) {
			return -2;
		} else if (one.getData().equals(two.getData()) == false) {
			return -3;
		}
		if (one.hasItemMeta() || two.hasItemMeta()) {
			if (one.hasItemMeta() && two.hasItemMeta() == false) {
				return -4;
			} else if (one.hasItemMeta() == false && two.hasItemMeta()) {
				return -4;
			} else if (one.getItemMeta().hasDisplayName() && two.getItemMeta().hasDisplayName() == false) {
				return -4;
			} else if (one.getItemMeta().hasDisplayName() == false && two.getItemMeta().hasDisplayName()) {
				return -4;
			} else if (one.getItemMeta().hasLore() && two.getItemMeta().hasLore() == false) {
				return -4;
			} else if (one.getItemMeta().hasLore() == false && two.getItemMeta().hasLore()) {
				return -4;
			} else if (one.getItemMeta().hasDisplayName() && two.getItemMeta().hasDisplayName() && ChatColor.stripColor(one.getItemMeta().getDisplayName()).equals(ChatColor.stripColor(two.getItemMeta().getDisplayName())) == false) {
				return -4;
			} else if (one.getItemMeta().hasLore() && two.getItemMeta().hasLore() && one.getItemMeta().getLore().equals(two.getItemMeta().getLore()) == false) {
				return -4;
			}
		}
		if (one.getEnchantments().equals(two.getEnchantments()) == false) {
			return -5;
		} else {
			return 0;
		}
	}

	/**
	 * Get ItemStack from formatted string. See serialize() for reverse function.
	 * 
	 * <p>Supplied format = name-name:amount-amount:data-data:enchantment-enchantment level:displayname-displayname:lore-lore:
	 * 
	 * @param data formatted string
	 * @return ItemStack, or null if invalid format
	 */
	public static ItemStack readItemStack(String data) {
		if (data == null) {
			return null;
		}
		ItemStack stack = null;
		String[] args = data.split(":");
		ItemMeta meta = null;
		LinkedList<String> lore = new LinkedList<String>();
		for (String arg : args) {
			if (arg.startsWith("name-")) {
				try {
					stack = new ItemStack(Material.matchMaterial(arg.substring(5)));
				} catch (NullPointerException npe) {
					Bukkit.getLogger().severe("[Quests] The item name \'" + arg.substring(5) + "\' is invalid. Make sure quests.yml is UTF-8 encoded");
					return null;
				}
				meta = stack.getItemMeta();
			} else if (arg.startsWith("amount-")) {
				stack.setAmount(Integer.parseInt(arg.substring(7)));
			} else if (arg.startsWith("data-")) {
				stack.setDurability(Short.parseShort(arg.substring(5)));
			} else if (arg.startsWith("enchantment-")) {
				String[] enchs = arg.substring(12).split(" ");
				try {
					System.out.println("enchs[0] " + enchs[0]);
					Enchantment e = Quests.getEnchantment(enchs[0]);
					meta.addEnchant(e, Integer.parseInt(enchs[1]), true);
				} catch (IllegalArgumentException e) {
					Bukkit.getLogger().severe("[Quests] The enchantment name \'" + enchs[0] + "\' is invalid. Make sure quests.yml is UTF-8 encoded");
					return null;
				}
			} else if (arg.startsWith("displayname-")) {
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', arg.substring(12)));
			} else if (arg.startsWith("lore-")) {
				lore.add(ChatColor.translateAlternateColorCodes('&', arg.substring(5)));
			} else {
				return null;
			}
		}
		if (lore.isEmpty() == false) {
			meta.setLore(lore);
		}
		stack.setItemMeta(meta);
		return stack;
	}

	/**
	 * Get formatted string from ItemStack. See readItemStack() for reverse function.
	 * 
	 * <p>Returned format = name-name:amount-amount:data-data:enchantment-enchantment level:displayname-displayname:lore-lore:
	 * 
	 * @param is ItemStack
	 * @return formatted string, or null if invalid stack
	 */
	public static String serialize(ItemStack is) {
		String serial;
		if (is == null) {
			return null;
		}
		serial = "name-" + is.getType().name();
		serial += ":amount-" + is.getAmount();
		if (is.getDurability() != 0) {
			serial += ":data-" + is.getDurability();
		}
		if (is.getEnchantments().isEmpty() == false) {
			for (Entry<Enchantment, Integer> e : is.getEnchantments().entrySet()) {
				serial += ":enchantment-" + Quester.enchantmentString(e.getKey()) + " " + e.getValue();
			}
		}
		if (is.hasItemMeta()) {
			ItemMeta meta = is.getItemMeta();
			if (meta.hasDisplayName()) {
				serial += ":displayname-" + meta.getDisplayName();
			}
			if (meta.hasLore()) {
				for (String s : meta.getLore()) {
					serial += ":lore-" + s;
				}
			}
		}
		return serial;
	}

	public static String getDisplayString(ItemStack is) {
		String text;
		if (is == null) {
			return null;
		}
		if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
			text = "" + ChatColor.DARK_AQUA + ChatColor.ITALIC + is.getItemMeta().getDisplayName() + ChatColor.RESET + ChatColor.AQUA + " x " + is.getAmount();
		} else {
			text = ChatColor.AQUA + getName(is);
			if (is.getDurability() != 0) {
				text += ChatColor.AQUA + ":" + is.getDurability();
			}
			if (is.getEnchantments().isEmpty() == false) {
				text += " " + ChatColor.GRAY + Lang.get("with") + ChatColor.DARK_PURPLE;
				for (Entry<Enchantment, Integer> e : is.getEnchantments().entrySet()) {
					text += " " + Quester.prettyEnchantmentString(e.getKey()) + ":" + e.getValue();
				}
			}
			text += ChatColor.AQUA + " x " + is.getAmount();
		}
		return text;
	}

	public static String getString(ItemStack is) {
		String text;
		if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
			text = "" + ChatColor.DARK_AQUA + ChatColor.ITALIC + is.getItemMeta().getDisplayName() + ChatColor.RESET + ChatColor.AQUA + " x " + is.getAmount();
		} else {
			text = ChatColor.AQUA + getName(is);
			if (is.getDurability() != 0) {
				text += ChatColor.AQUA + ":" + is.getDurability();
			}
			text += ChatColor.AQUA + " x " + is.getAmount();
		}
		return text;
	}

	@SuppressWarnings("deprecation")
	public static String getName(ItemStack is) {
		String text = "";
		if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
			text = "" + ChatColor.DARK_AQUA + ChatColor.ITALIC + is.getItemMeta().getDisplayName();
		} else {
			try {
				text = ChatColor.AQUA + Items.itemByType(is.getType()).getName();
			} catch (Exception ne) {
				text = ChatColor.AQUA + Quester.prettyItemString(is.getType().name());
				Bukkit.getLogger().severe("This error is likely caused by an incompatible version of Vault. Please consider updating.");
				ne.printStackTrace();
			}
		}
		return text;
	}

	/**
	 * Ensures that an ItemStack if a valid, non-AIR material
	 * 
	 * @param is ItemStack to check
	 * @return true if stack is not null or Material.AIR
	 */
	public static boolean isItem(ItemStack is) {
		if (is == null)
			return false;
		if (is.getType().equals(Material.AIR))
			return false;
		return true;
	}

	/**
	 * Checks whether an ItemStack is a Quest Journal based on book title
	 * 
	 * @param is IemsStack to check
	 * @return true if display name equals colored journal title
	 */
	public static boolean isJournal(ItemStack is) {
		if (is == null)
			return false;
		if (is.hasItemMeta() == false)
			return false;
		if (is.getItemMeta().hasDisplayName() == false)
			return false;
		return is.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + Lang.get("journalTitle"));
	}
}