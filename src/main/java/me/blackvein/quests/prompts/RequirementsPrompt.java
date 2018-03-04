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

package me.blackvein.quests.prompts;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.herocraftonline.heroes.characters.classes.HeroClass;

import me.blackvein.quests.CustomRequirement;
import me.blackvein.quests.Quest;
import me.blackvein.quests.QuestFactory;
import me.blackvein.quests.Quests;
import me.blackvein.quests.util.CK;
import me.blackvein.quests.util.ItemUtil;
import me.blackvein.quests.util.Lang;
import me.blackvein.quests.util.MiscUtil;

public class RequirementsPrompt extends FixedSetPrompt {

	Quests quests;
	final QuestFactory factory;

	public RequirementsPrompt(Quests plugin, QuestFactory qf) {
		super("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");
		quests = plugin;
		factory = qf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getPromptText(ConversationContext context) {
		String text;
		String lang = Lang.get("requirementsTitle");
		lang = lang.replaceAll("<quest>", ChatColor.AQUA + (String) context.getSessionData(CK.Q_NAME) + ChatColor.DARK_AQUA);
		text = ChatColor.DARK_AQUA + lang + "\n";
		if (context.getSessionData(CK.REQ_MONEY) == null) {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "1" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetMoney") + " " + ChatColor.GRAY + "(" + Lang.get("noneSet") + ")\n";
		} else {
			int moneyReq = (Integer) context.getSessionData(CK.REQ_MONEY);
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "1" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetMoney") + " (" + moneyReq + " " + (moneyReq > 1 ? Quests.getCurrency(true) : Quests.getCurrency(false)) + ")\n";
		}
		if (context.getSessionData(CK.REQ_QUEST_POINTS) == null) {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "2" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetQuestPoints") + " " + ChatColor.GRAY + "(" + Lang.get("noneSet") + ")\n";
		} else {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "2" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetQuestPoints") + " " + ChatColor.GRAY + "(" + ChatColor.AQUA + context.getSessionData(CK.REQ_QUEST_POINTS) + " " + Lang.get("questPoints") + ChatColor.GRAY + ")\n";
		}
		text += ChatColor.BLUE + "" + ChatColor.BOLD + "3" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetItem") + "\n";
		if (context.getSessionData(CK.REQ_PERMISSION) == null) {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "4" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetPerms") + " " + ChatColor.GRAY + " (" + Lang.get("noneSet") + ")\n";
		} else {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "4" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetPerms") + "\n";
			List<String> perms = (List<String>) context.getSessionData(CK.REQ_PERMISSION);
			for (String s : perms) {
				text += ChatColor.GRAY + "    - " + ChatColor.AQUA + s + "\n";
			}
		}
		if (context.getSessionData(CK.REQ_QUEST) == null) {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "5" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetQuest") + " " + ChatColor.GRAY + " (" + Lang.get("noneSet") + ")\n";
		} else {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "5" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetQuest") + "\n";
			List<String> qs = (List<String>) context.getSessionData(CK.REQ_QUEST);
			for (String s : qs) {
				text += ChatColor.GRAY + "    - " + ChatColor.AQUA + s + "\n";
			}
		}
		if (context.getSessionData(CK.REQ_QUEST_BLOCK) == null) {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "6" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetQuestBlocks") + " " + ChatColor.GRAY + " (" + Lang.get("noneSet") + ")\n";
		} else {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "6" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetQuestBlocks") + "\n";
			List<String> qs = (List<String>) context.getSessionData(CK.REQ_QUEST_BLOCK);
			for (String s : qs) {
				text += ChatColor.GRAY + "    - " + ChatColor.AQUA + s + "\n";
			}
		}
		if (Quests.mcmmo != null) {
			if (context.getSessionData(CK.REQ_MCMMO_SKILLS) == null) {
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "7" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetMcMMO") + " " + ChatColor.GRAY + " (" + Lang.get("noneSet") + ")\n";
			} else {
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "7" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetMcMMO") + "\n";
				List<String> skills = (List<String>) context.getSessionData(CK.REQ_MCMMO_SKILLS);
				List<Integer> amounts = (List<Integer>) context.getSessionData(CK.REQ_MCMMO_SKILL_AMOUNTS);
				for (String s : skills) {
					text += ChatColor.GRAY + "    - " + ChatColor.DARK_GREEN + s + ChatColor.RESET + ChatColor.YELLOW + " " + Lang.get("mcMMOLevel") + " " + ChatColor.GREEN + amounts.get(skills.indexOf(s)) + "\n";
				}
			}
		} else {
			text += ChatColor.GRAY + "6 - " + Lang.get("reqSetMcMMO") + " (" + Lang.get("reqNoMcMMO") + ")\n";
		}
		if (Quests.heroes != null) {
			if (context.getSessionData(CK.REQ_HEROES_PRIMARY_CLASS) == null && context.getSessionData(CK.REQ_HEROES_SECONDARY_CLASS) == null) {
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "8" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetHeroes") + " " + ChatColor.GRAY + " (" + Lang.get("noneSet") + ")\n";
			} else {
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "8" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetHeroes") + "\n";
				if (context.getSessionData(CK.REQ_HEROES_PRIMARY_CLASS) != null) {
					text += ChatColor.AQUA + "    " + Lang.get("reqHeroesPrimaryDisplay") + " " + ChatColor.BLUE + (String) context.getSessionData(CK.REQ_HEROES_PRIMARY_CLASS) + "\n";
				}
				if (context.getSessionData(CK.REQ_HEROES_SECONDARY_CLASS) != null) {
					text += ChatColor.AQUA + "    " + Lang.get("reqHeroesSecondaryDisplay") + " " + ChatColor.BLUE + (String) context.getSessionData(CK.REQ_HEROES_SECONDARY_CLASS) + "\n";
				}
			}
		} else {
			text += ChatColor.GRAY + "8 - " + Lang.get("reqSetHeroes") + " (" + Lang.get("reqNoHeroes") + ")\n";
		}
		if (context.getSessionData(CK.REQ_CUSTOM) == null) {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "9 - " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.DARK_PURPLE + Lang.get("reqSetCustom") + " (" + Lang.get("noneSet") + ")\n";
		} else {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "9 - " + ChatColor.RESET + ChatColor.ITALIC + ChatColor.DARK_PURPLE + Lang.get("reqSetCustom") + "\n";
			LinkedList<String> customReqs = (LinkedList<String>) context.getSessionData(CK.REQ_CUSTOM);
			for (String s : customReqs) {
				text += ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "  - " + ChatColor.LIGHT_PURPLE + s + "\n";
			}
		}
		if (context.getSessionData(CK.REQ_MONEY) == null && context.getSessionData(CK.REQ_QUEST_POINTS) == null && context.getSessionData(CK.REQ_QUEST_BLOCK) == null && context.getSessionData(CK.REQ_ITEMS) == null && context.getSessionData(CK.REQ_PERMISSION) == null && context.getSessionData(CK.REQ_QUEST) == null && context.getSessionData(CK.REQ_QUEST_BLOCK) == null && context.getSessionData(CK.REQ_MCMMO_SKILLS) == null && context.getSessionData(CK.REQ_HEROES_PRIMARY_CLASS) == null && context.getSessionData(CK.REQ_HEROES_SECONDARY_CLASS) == null && context.getSessionData(CK.REQ_CUSTOM) == null) {
			text += ChatColor.GRAY + "" + ChatColor.BOLD + "10 - " + ChatColor.RESET + ChatColor.GRAY + Lang.get("reqSetFail") + " (" + Lang.get("reqNone") + ")\n";
		} else if (context.getSessionData(CK.Q_FAIL_MESSAGE) == null) {
			text += ChatColor.RED + "" + ChatColor.BOLD + "10 - " + ChatColor.RESET + ChatColor.RED + Lang.get("reqSetFail") + " (" + Lang.get("questRequiredNoneSet") + ")\n";
		} else {
			text += ChatColor.BLUE + "" + ChatColor.BOLD + "10 - " + ChatColor.RESET + ChatColor.YELLOW + Lang.get("reqSetFail") + ChatColor.GRAY + "(" + ChatColor.AQUA + "\"" + context.getSessionData(CK.Q_FAIL_MESSAGE) + "\"" + ChatColor.GRAY + ")\n";
		}
		text += ChatColor.GREEN + "" + ChatColor.BOLD + "11" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("done");
		return text;
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		if (input.equalsIgnoreCase("1")) {
			return new MoneyPrompt();
		} else if (input.equalsIgnoreCase("2")) {
			return new QuestPointsPrompt();
		} else if (input.equalsIgnoreCase("3")) {
			return new ItemListPrompt();
		} else if (input.equalsIgnoreCase("4")) {
			return new PermissionsPrompt();
		} else if (input.equalsIgnoreCase("5")) {
			return new QuestListPrompt(true);
		} else if (input.equalsIgnoreCase("6")) {
			return new QuestListPrompt(false);
		} else if (input.equalsIgnoreCase("7")) {
			if (Quests.mcmmo != null) {
				return new mcMMOPrompt();
			} else {
				return new RequirementsPrompt(quests, factory);
			}
		} else if (input.equalsIgnoreCase("8")) {
			if (Quests.heroes != null) {
				return new HeroesPrompt();
			} else {
				return new RequirementsPrompt(quests, factory);
			}
		} else if (input.equalsIgnoreCase("9")) {
			return new CustomRequirementsPrompt();
		} else if (input.equalsIgnoreCase("10")) {
			return new FailMessagePrompt();
		} else if (input.equalsIgnoreCase("11")) {
			if (context.getSessionData(CK.REQ_MONEY) != null || context.getSessionData(CK.REQ_QUEST_POINTS) != null || context.getSessionData(CK.REQ_ITEMS) != null || context.getSessionData(CK.REQ_PERMISSION) != null || context.getSessionData(CK.REQ_QUEST) != null || context.getSessionData(CK.REQ_QUEST_BLOCK) != null || context.getSessionData(CK.REQ_MCMMO_SKILLS) != null || context.getSessionData(CK.REQ_HEROES_PRIMARY_CLASS) != null || context.getSessionData(CK.REQ_HEROES_SECONDARY_CLASS) != null || context.getSessionData(CK.REQ_CUSTOM) != null) {
				if (context.getSessionData(CK.Q_FAIL_MESSAGE) == null) {
					context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("reqNoMessage"));
					return new RequirementsPrompt(quests, factory);
				}
			}
			return factory.returnToMenu();
		}
		return null;
	}

	private class MoneyPrompt extends NumericPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			String text = Lang.get("reqMoneyPrompt");
			if (Quests.economy != null) {
				text = text.replaceAll("<money>", ChatColor.DARK_PURPLE + ((Quests.economy.currencyNamePlural().isEmpty() ? Lang.get("money") : Quests.economy.currencyNamePlural())) + ChatColor.YELLOW);
			} else {
				text = text.replaceAll("<money>", ChatColor.DARK_PURPLE + Lang.get("money") + ChatColor.YELLOW);
			}
			return ChatColor.YELLOW + text;
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
			if (input.intValue() < -1) {
				context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("reqGreaterThanZero"));
				return new MoneyPrompt();
			} else if (input.intValue() == -1) {
				return new RequirementsPrompt(quests, factory);
			} else if (input.intValue() == 0) {
				context.setSessionData(CK.REQ_MONEY, null);
				return new RequirementsPrompt(quests, factory);
			}
			context.setSessionData(CK.REQ_MONEY, input.intValue());
			return new RequirementsPrompt(quests, factory);
		}
	}

	private class QuestPointsPrompt extends NumericPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return ChatColor.YELLOW + Lang.get("reqQuestPointsPrompt");
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
			if (input.intValue() < -1) {
				context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("reqGreaterThanZero"));
				return new QuestPointsPrompt();
			} else if (input.intValue() == -1) {
				return new RequirementsPrompt(quests, factory);
			} else if (input.intValue() == 0) {
				context.setSessionData(CK.REQ_QUEST_POINTS, null);
				return new RequirementsPrompt(quests, factory);
			}
			context.setSessionData(CK.REQ_QUEST_POINTS, input.intValue());
			return new RequirementsPrompt(quests, factory);
		}
	}

	private class QuestListPrompt extends StringPrompt {

		private final boolean isRequiredQuest;

		public QuestListPrompt(boolean isRequired) {
			this.isRequiredQuest = isRequired;
		}

		@Override
		public String getPromptText(ConversationContext context) {
			String text = ChatColor.LIGHT_PURPLE + Lang.get("reqQuestListTitle") + "\n" + ChatColor.DARK_PURPLE;
			boolean none = true;
			for (Quest q : quests.getQuests()) {
				text += q.getName() + ", ";
				none = false;
			}
			if (none) {
				text += "(" + Lang.get("none") + ")\n";
			} else {
				text = text.substring(0, (text.length() - 2));
				text += "\n";
			}
			text += ChatColor.YELLOW + Lang.get("reqQuestPrompt");
			return text;
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			if (input.equalsIgnoreCase(Lang.get("cmdCancel")) == false && input.equalsIgnoreCase(Lang.get("cmdClear")) == false) {
				String[] args = input.split(Lang.get("charSemi"));
				LinkedList<String> questNames = new LinkedList<String>();
				for (String s : args) {
					if (quests.getQuest(s) == null) {
						String text = Lang.get("reqNotAQuestName");
						text = text.replaceAll("<quest>", ChatColor.LIGHT_PURPLE + s + ChatColor.RED);
						context.getForWhom().sendRawMessage(text);
						return new QuestListPrompt(isRequiredQuest);
					}
					if (questNames.contains(s)) {
						context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("listDuplicate"));
						return new QuestListPrompt(isRequiredQuest);
					}
					questNames.add(quests.getQuest(s).name);
				}
				Collections.sort(questNames, new Comparator<String>() {

					@Override
					public int compare(String one, String two) {
						return one.compareTo(two);
					}
				});
				if (isRequiredQuest) {
					context.setSessionData(CK.REQ_QUEST, questNames);
				} else {
					context.setSessionData(CK.REQ_QUEST_BLOCK, questNames);
				}
			} else if (input.equalsIgnoreCase(Lang.get("cmdClear"))) {
				if (isRequiredQuest) {
					context.setSessionData(CK.REQ_QUEST, null);
				} else {
					context.setSessionData(CK.REQ_QUEST_BLOCK, null);
				}
			}
			return new RequirementsPrompt(quests, factory);
		}
	}

	private class ItemListPrompt extends FixedSetPrompt {

		public ItemListPrompt() {
			super("1", "2", "3", "4");
		}

		@Override
		public String getPromptText(ConversationContext context) {
			// Check/add newly made item
			if (context.getSessionData("newItem") != null) {
				if (context.getSessionData(CK.REQ_ITEMS) != null) {
					List<ItemStack> itemRews = getItems(context);
					itemRews.add((ItemStack) context.getSessionData("tempStack"));
					context.setSessionData(CK.REQ_ITEMS, itemRews);
				} else {
					LinkedList<ItemStack> itemRews = new LinkedList<ItemStack>();
					itemRews.add((ItemStack) context.getSessionData("tempStack"));
					context.setSessionData(CK.REQ_ITEMS, itemRews);
				}
				context.setSessionData("newItem", null);
				context.setSessionData("tempStack", null);
			}
			String text = ChatColor.GOLD + Lang.get("itemRequirementsTitle") + "\n";
			if (context.getSessionData(CK.REQ_ITEMS) == null) {
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "1" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqAddItem") + "\n";
				text += ChatColor.GRAY + "2 - " + Lang.get("reqSetRemoveItems") + " (" + Lang.get("reqNoItemsSet") + ")\n";
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "3" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("clear") + "\n";
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "4" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("done");
			} else {
				for (ItemStack is : getItems(context)) {
					text += ChatColor.GRAY + "    - " + ItemUtil.getDisplayString(is) + "\n";
				}
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "1" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqAddItem") + "\n";
				if (context.getSessionData(CK.REQ_ITEMS_REMOVE) == null) {
					text += ChatColor.BLUE + "" + ChatColor.BOLD + "2" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetRemoveItems") + " (" + Lang.get("reqNoValuesSet") + ")\n";
				} else {
					text += ChatColor.BLUE + "" + ChatColor.BOLD + "2" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("reqSetRemoveItems") + "\n";
					for (Boolean b : getRemoveItems(context)) {
						text += ChatColor.GRAY + "    - " + ChatColor.AQUA + (b.equals(Boolean.TRUE) ? Lang.get("yesWord") : Lang.get("noWord")) + "\n";
					}
				}
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "3" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("clear") + "\n";
				text += ChatColor.BLUE + "" + ChatColor.BOLD + "4" + ChatColor.RESET + ChatColor.YELLOW + " - " + Lang.get("done");
			}
			return text;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String input) {
			if (input.equalsIgnoreCase("1")) {
				return new ItemStackPrompt(ItemListPrompt.this);
			} else if (input.equalsIgnoreCase("2")) {
				if (context.getSessionData(CK.REQ_ITEMS) == null) {
					context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("reqMustAddItem"));
					return new ItemListPrompt();
				} else {
					return new RemoveItemsPrompt();
				}
			} else if (input.equalsIgnoreCase("3")) {
				context.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("reqItemCleared"));
				context.setSessionData(CK.REQ_ITEMS, null);
				context.setSessionData(CK.REQ_ITEMS_REMOVE, null);
				return new ItemListPrompt();
			} else if (input.equalsIgnoreCase("4")) {
				int one;
				int two;
				if (context.getSessionData(CK.REQ_ITEMS) != null) {
					one = ((List<ItemStack>) context.getSessionData(CK.REQ_ITEMS)).size();
				} else {
					one = 0;
				}
				if (context.getSessionData(CK.REQ_ITEMS_REMOVE) != null) {
					two = ((List<Boolean>) context.getSessionData(CK.REQ_ITEMS_REMOVE)).size();
				} else {
					two = 0;
				}
				if (one == two) {
					return new RequirementsPrompt(quests, factory);
				} else {
					context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("reqListsNotSameSize"));
					return new ItemListPrompt();
				}
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		private List<ItemStack> getItems(ConversationContext context) {
			return (List<ItemStack>) context.getSessionData(CK.REQ_ITEMS);
		}

		@SuppressWarnings("unchecked")
		private List<Boolean> getRemoveItems(ConversationContext context) {
			return (List<Boolean>) context.getSessionData(CK.REQ_ITEMS_REMOVE);
		}
	}

	private class RemoveItemsPrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return ChatColor.YELLOW + Lang.get("reqRemoveItemsPrompt");
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			if (input.equalsIgnoreCase(Lang.get("cmdCancel")) == false) {
				String[] args = input.split(" ");
				LinkedList<Boolean> booleans = new LinkedList<Boolean>();
				for (String s : args) {
					if (s.equalsIgnoreCase(Lang.get("true")) || s.equalsIgnoreCase(Lang.get("yesWord"))) {
						booleans.add(true);
					} else if (s.equalsIgnoreCase(Lang.get("false")) || s.equalsIgnoreCase(Lang.get("noWord"))) {
						booleans.add(false);
					} else {
						String text = Lang.get("reqTrueFalseError");
						text = text.replaceAll("<input>", ChatColor.LIGHT_PURPLE + s + ChatColor.RED);
						context.getForWhom().sendRawMessage(ChatColor.RED + text);
						return new RemoveItemsPrompt();
					}
				}
				context.setSessionData(CK.REQ_ITEMS_REMOVE, booleans);
			}
			return new ItemListPrompt();
		}
	}

	private class PermissionsPrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return ChatColor.YELLOW + Lang.get("reqPermissionsPrompt");
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			if (input.equalsIgnoreCase(Lang.get("cmdCancel")) == false && input.equalsIgnoreCase(Lang.get("cmdClear")) == false) {
				String[] args = input.split(" ");
				LinkedList<String> permissions = new LinkedList<String>();
				permissions.addAll(Arrays.asList(args));
				context.setSessionData(CK.REQ_PERMISSION, permissions);
			} else if (input.equalsIgnoreCase(Lang.get("cmdClear"))) {
				context.setSessionData(CK.REQ_PERMISSION, null);
			}
			return new RequirementsPrompt(quests, factory);
		}
	}

	private class CustomRequirementsPrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			String text = ChatColor.LIGHT_PURPLE + Lang.get("customRequirementsTitle") + "\n";
			if (quests.customRequirements.isEmpty()) {
				text += ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "(" + Lang.get("stageEditorNoModules") + ") ";
			} else {
				for (CustomRequirement cr : quests.customRequirements) {
					text += ChatColor.DARK_PURPLE + " - " + cr.getName() + "\n";
				}
			}
			return text + ChatColor.YELLOW + Lang.get("reqCustomPrompt");
		}

		@SuppressWarnings("unchecked")
		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			if (input.equalsIgnoreCase(Lang.get("cmdCancel")) == false && input.equalsIgnoreCase(Lang.get("cmdClear")) == false) {
				CustomRequirement found = null;
				for (CustomRequirement cr : quests.customRequirements) {
					if (cr.getName().equalsIgnoreCase(input)) {
						found = cr;
						break;
					}
				}
				if (found == null) {
					for (CustomRequirement cr : quests.customRequirements) {
						if (cr.getName().toLowerCase().contains(input.toLowerCase())) {
							found = cr;
							break;
						}
					}
				}
				if (found != null) {
					if (context.getSessionData(CK.REQ_CUSTOM) != null) {
						LinkedList<String> list = (LinkedList<String>) context.getSessionData(CK.REQ_CUSTOM);
						LinkedList<Map<String, Object>> datamapList = (LinkedList<Map<String, Object>>) context.getSessionData(CK.REQ_CUSTOM_DATA);
						if (list.contains(found.getName()) == false) {
							list.add(found.getName());
							datamapList.add(found.datamap);
							context.setSessionData(CK.REQ_CUSTOM, list);
							context.setSessionData(CK.REQ_CUSTOM_DATA, datamapList);
						} else {
							context.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("reqCustomAlreadyAdded"));
							return new CustomRequirementsPrompt();
						}
					} else {
						LinkedList<Map<String, Object>> datamapList = new LinkedList<Map<String, Object>>();
						datamapList.add(found.datamap);
						LinkedList<String> list = new LinkedList<String>();
						list.add(found.getName());
						context.setSessionData(CK.REQ_CUSTOM, list);
						context.setSessionData(CK.REQ_CUSTOM_DATA, datamapList);
					}
					// Send user to the custom data prompt if there is any needed
					if (found.datamap.isEmpty() == false) {
						context.setSessionData(CK.REQ_CUSTOM_DATA_DESCRIPTIONS, found.descriptions);
						return new RequirementCustomDataListPrompt();
					}
					//
				} else {
					context.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("reqCustomNotFound"));
					return new CustomRequirementsPrompt();
				}
			} else if (input.equalsIgnoreCase(Lang.get("cmdClear"))) {
				context.setSessionData(CK.REQ_CUSTOM, null);
				context.setSessionData(CK.REQ_CUSTOM_DATA, null);
				context.setSessionData(CK.REQ_CUSTOM_DATA_TEMP, null);
				context.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("reqCustomCleared"));
			}
			return new RequirementsPrompt(quests, factory);
		}
	}

	private class RequirementCustomDataListPrompt extends StringPrompt {

		@SuppressWarnings("unchecked")
		@Override
		public String getPromptText(ConversationContext context) {
			String text = ChatColor.BOLD + "" + ChatColor.AQUA + "- ";
			LinkedList<String> list = (LinkedList<String>) context.getSessionData(CK.REQ_CUSTOM);
			LinkedList<Map<String, Object>> datamapList = (LinkedList<Map<String, Object>>) context.getSessionData(CK.REQ_CUSTOM_DATA);
			String reqName = list.getLast();
			Map<String, Object> datamap = datamapList.getLast();
			text += reqName + " -\n";
			int index = 1;
			LinkedList<String> datamapKeys = new LinkedList<String>();
			for (String key : datamap.keySet()) {
				datamapKeys.add(key);
			}
			Collections.sort(datamapKeys);
			for (String dataKey : datamapKeys) {
				text += ChatColor.BOLD + "" + ChatColor.DARK_BLUE + index + " - " + ChatColor.RESET + ChatColor.BLUE + dataKey;
				if (datamap.get(dataKey) != null) {
					text += ChatColor.GREEN + " (" + (String) datamap.get(dataKey) + ")\n";
				} else {
					text += ChatColor.RED + " (" + Lang.get("valRequired") + ")\n";
				}
				index++;
			}
			text += ChatColor.BOLD + "" + ChatColor.DARK_BLUE + index + " - " + ChatColor.AQUA + Lang.get("finish");
			return text;
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			@SuppressWarnings("unchecked")
			LinkedList<Map<String, Object>> datamapList = (LinkedList<Map<String, Object>>) context.getSessionData(CK.REQ_CUSTOM_DATA);
			Map<String, Object> datamap = datamapList.getLast();
			int numInput;
			try {
				numInput = Integer.parseInt(input);
			} catch (NumberFormatException nfe) {
				return new RequirementCustomDataListPrompt();
			}
			if (numInput < 1 || numInput > datamap.size() + 1) {
				return new RequirementCustomDataListPrompt();
			}
			if (numInput < datamap.size() + 1) {
				LinkedList<String> datamapKeys = new LinkedList<String>();
				for (String key : datamap.keySet()) {
					datamapKeys.add(key);
				}
				Collections.sort(datamapKeys);
				String selectedKey = datamapKeys.get(numInput - 1);
				context.setSessionData(CK.REQ_CUSTOM_DATA_TEMP, selectedKey);
				return new RequirementCustomDataPrompt();
			} else {
				if (datamap.containsValue(null)) {
					return new RequirementCustomDataListPrompt();
				} else {
					context.setSessionData(CK.REQ_CUSTOM_DATA_DESCRIPTIONS, null);
					return new RequirementsPrompt(quests, factory);
				}
			}
		}
	}

	private class RequirementCustomDataPrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			String text = "";
			String temp = (String) context.getSessionData(CK.REQ_CUSTOM_DATA_TEMP);
			@SuppressWarnings("unchecked")
			Map<String, String> descriptions = (Map<String, String>) context.getSessionData(CK.REQ_CUSTOM_DATA_DESCRIPTIONS);
			if (descriptions.get(temp) != null) {
				text += ChatColor.GOLD + descriptions.get(temp) + "\n";
			}
			String lang = Lang.get("stageEditorCustomDataPrompt");
			lang = lang.replaceAll("<data>", temp);
			text += ChatColor.YELLOW + lang;
			return text;
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			@SuppressWarnings("unchecked")
			LinkedList<Map<String, Object>> datamapList = (LinkedList<Map<String, Object>>) context.getSessionData(CK.REQ_CUSTOM_DATA);
			Map<String, Object> datamap = datamapList.getLast();
			datamap.put((String) context.getSessionData(CK.REQ_CUSTOM_DATA_TEMP), input);
			context.setSessionData(CK.REQ_CUSTOM_DATA_TEMP, null);
			return new RequirementCustomDataListPrompt();
		}
	}

	private class mcMMOPrompt extends FixedSetPrompt {

		public mcMMOPrompt() {
			super("1", "2", "3");
		}

		@Override
		public String getPromptText(ConversationContext cc) {
			String text = ChatColor.DARK_GREEN + Lang.get("mcMMORequirementsTitle") + "\n";
			if (cc.getSessionData(CK.REQ_MCMMO_SKILLS) == null) {
				text += ChatColor.BOLD + "" + ChatColor.GREEN + "1" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("reqSetSkills") + "(" + Lang.get("noneSet") + ")\n";
			} else {
				text += ChatColor.BOLD + "" + ChatColor.GREEN + "1" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("reqSetSkills") + "\n";
				@SuppressWarnings("unchecked")
				LinkedList<String> skills = (LinkedList<String>) cc.getSessionData(CK.REQ_MCMMO_SKILLS);
				for (String skill : skills) {
					text += ChatColor.GRAY + "    - " + ChatColor.AQUA + skill + "\n";
				}
			}
			if (cc.getSessionData(CK.REQ_MCMMO_SKILL_AMOUNTS) == null) {
				text += ChatColor.BOLD + "" + ChatColor.GREEN + "2" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("reqSetSkillAmounts") + " (" + Lang.get("noneSet") + ")\n";
			} else {
				text += ChatColor.BOLD + "" + ChatColor.GREEN + "2" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("reqSetSkillAmounts") + "\n";
				@SuppressWarnings("unchecked")
				LinkedList<Integer> amounts = (LinkedList<Integer>) cc.getSessionData(CK.REQ_MCMMO_SKILL_AMOUNTS);
				for (int i : amounts) {
					text += ChatColor.GRAY + "    - " + ChatColor.AQUA + i + "\n";
				}
			}
			text += ChatColor.BOLD + "" + ChatColor.GREEN + "3" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("done");
			return text;
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext cc, String input) {
			if (input.equalsIgnoreCase("1")) {
				return new mcMMOSkillsPrompt();
			} else if (input.equalsIgnoreCase("2")) {
				return new mcMMOAmountsPrompt();
			} else if (input.equalsIgnoreCase("3")) {
				return new RequirementsPrompt(quests, factory);
			}
			return null;
		}
	}

	private class mcMMOSkillsPrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			String skillList = ChatColor.DARK_GREEN + Lang.get("skillListTitle") + "\n";
			SkillType[] skills = SkillType.values();
			for (int i = 0; i < skills.length; i++) {
				if (i == (skills.length - 1)) {
					skillList += ChatColor.GREEN + skills[i].getName() + "\n";
				} else {
					skillList += ChatColor.GREEN + skills[i].getName() + "\n\n";
				}
			}
			return skillList + ChatColor.YELLOW + Lang.get("reqMcMMOPrompt");
		}

		@Override
		public Prompt acceptInput(ConversationContext cc, String input) {
			if (input.equalsIgnoreCase(Lang.get("cmdCancel")) == false && input.equalsIgnoreCase(Lang.get("cmdClear")) == false) {
				LinkedList<String> skills = new LinkedList<String>();
				for (String s : input.split(" ")) {
					String formatted = MiscUtil.getCapitalized(s);
					if (Quests.getMcMMOSkill(formatted) != null) {
						skills.add(formatted);
					} else if (skills.contains(formatted)) {
						cc.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("listDuplicate"));
						return new mcMMOSkillsPrompt();
					} else {
						String text = Lang.get("reqMcMMOError");
						text = text.replaceAll("<input>", ChatColor.RED + s + ChatColor.YELLOW);
						cc.getForWhom().sendRawMessage(ChatColor.YELLOW + text);
						return new mcMMOSkillsPrompt();
					}
				}
				cc.setSessionData(CK.REQ_MCMMO_SKILLS, skills);
				return new mcMMOPrompt();
			} else if (input.equalsIgnoreCase(Lang.get("cmdClear"))) {
				cc.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("reqMcMMOCleared"));
				cc.setSessionData(CK.REQ_MCMMO_SKILLS, null);
				return new mcMMOPrompt();
			} else if (input.equalsIgnoreCase(Lang.get("cmdCancel"))) {
				return new mcMMOPrompt();
			}
			return new mcMMOSkillsPrompt();
		}
	}

	private class mcMMOAmountsPrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return ChatColor.YELLOW + Lang.get("reqMcMMOAmountsPrompt");
		}

		@Override
		public Prompt acceptInput(ConversationContext cc, String input) {
			if (input.equalsIgnoreCase(Lang.get("cmdCancel")) == false && input.equalsIgnoreCase(Lang.get("cmdClear")) == false) {
				LinkedList<Integer> amounts = new LinkedList<Integer>();
				for (String s : input.split(" ")) {
					try {
						int i = Integer.parseInt(s);
						amounts.add(i);
					} catch (NumberFormatException nfe) {
						String text = Lang.get("reqNotANumber");
						text = text.replaceAll("<input>", ChatColor.RED + s + ChatColor.YELLOW);
						cc.getForWhom().sendRawMessage(ChatColor.YELLOW + text);
						return new mcMMOAmountsPrompt();
					}
				}
				cc.setSessionData(CK.REQ_MCMMO_SKILL_AMOUNTS, amounts);
				return new mcMMOPrompt();
			} else if (input.equalsIgnoreCase(Lang.get("cmdClear"))) {
				cc.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("reqMcMMOAmountsCleared"));
				cc.setSessionData(CK.REQ_MCMMO_SKILL_AMOUNTS, null);
				return new mcMMOPrompt();
			} else if (input.equalsIgnoreCase(Lang.get("cmdCancel"))) {
				return new mcMMOPrompt();
			}
			return new mcMMOAmountsPrompt();
		}
	}

	private class HeroesPrompt extends FixedSetPrompt {

		public HeroesPrompt() {
			super("1", "2", "3");
		}

		@Override
		public String getPromptText(ConversationContext cc) {
			String text = ChatColor.DARK_GREEN + Lang.get("heroesRequirementsTitle") + "\n";
			if (cc.getSessionData(CK.REQ_HEROES_PRIMARY_CLASS) == null) {
				text += ChatColor.BOLD + "" + ChatColor.GREEN + "1" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("reqHeroesSetPrimary") + " (" + Lang.get("noneSet") + ")\n";
			} else {
				text += ChatColor.BOLD + "" + ChatColor.GREEN + "1" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("reqHeroesSetPrimary") + " (" + ChatColor.AQUA + (String) cc.getSessionData(CK.REQ_HEROES_PRIMARY_CLASS) + ChatColor.GREEN + ")\n";
			}
			if (cc.getSessionData(CK.REQ_HEROES_SECONDARY_CLASS) == null) {
				text += ChatColor.BOLD + "" + ChatColor.GREEN + "2" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("reqHeroesSetSecondary") + " (" + Lang.get("noneSet") + ")\n";
			} else {
				text += ChatColor.BOLD + "" + ChatColor.GREEN + "2" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("reqHeroesSetSecondary") + " (" + ChatColor.AQUA + (String) cc.getSessionData(CK.REQ_HEROES_SECONDARY_CLASS) + ChatColor.GREEN + ")\n";
			}
			text += ChatColor.BOLD + "" + ChatColor.GREEN + "3" + ChatColor.RESET + ChatColor.GREEN + " - " + Lang.get("done");
			return text;
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext cc, String input) {
			if (input.equalsIgnoreCase("1")) {
				return new HeroesPrimaryPrompt();
			} else if (input.equalsIgnoreCase("2")) {
				return new HeroesSecondaryPrompt();
			} else if (input.equalsIgnoreCase("3")) {
				return new RequirementsPrompt(quests, factory);
			}
			return null;
		}
	}

	private class HeroesPrimaryPrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext cc) {
			String text = ChatColor.DARK_PURPLE + Lang.get("heroesPrimaryTitle") + "\n";
			LinkedList<String> list = new LinkedList<String>();
			for (HeroClass hc : Quests.heroes.getClassManager().getClasses()) {
				if (hc.isPrimary()) {
					list.add(hc.getName());
				}
			}
			if (list.isEmpty()) {
				text += ChatColor.GRAY + "(" + Lang.get("none") + ")\n";
			} else {
				Collections.sort(list);
				for (String s : list) {
					text += ChatColor.DARK_PURPLE + "- " + ChatColor.LIGHT_PURPLE + s + "\n";
				}
			}
			text += ChatColor.YELLOW + Lang.get("reqHeroesPrimaryPrompt");
			return text;
		}

		@Override
		public Prompt acceptInput(ConversationContext cc, String input) {
			if (input.equalsIgnoreCase(Lang.get("cmdClear")) == false && input.equalsIgnoreCase(Lang.get("cmdCancel")) == false) {
				HeroClass hc = Quests.heroes.getClassManager().getClass(input);
				if (hc != null) {
					if (hc.isPrimary()) {
						cc.setSessionData(CK.REQ_HEROES_PRIMARY_CLASS, hc.getName());
						return new HeroesPrompt();
					} else {
						String text = Lang.get("reqHeroesNotPrimary");
						text = text.replaceAll("<class>", ChatColor.LIGHT_PURPLE + hc.getName() + ChatColor.RED);
						cc.getForWhom().sendRawMessage(ChatColor.RED + text);
						return new HeroesPrimaryPrompt();
					}
				} else {
					cc.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("reqHeroesClassNotFound"));
					return new HeroesPrimaryPrompt();
				}
			} else if (input.equalsIgnoreCase(Lang.get("cmdClear"))) {
				cc.setSessionData(CK.REQ_HEROES_PRIMARY_CLASS, null);
				cc.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("reqHeroesPrimaryCleared"));
				return new HeroesPrompt();
			} else {
				return new HeroesPrompt();
			}
		}
	}

	private class HeroesSecondaryPrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext cc) {
			String text = ChatColor.DARK_PURPLE + Lang.get("heroesSecondaryTitle") + "\n";
			LinkedList<String> list = new LinkedList<String>();
			for (HeroClass hc : Quests.heroes.getClassManager().getClasses()) {
				if (hc.isSecondary()) {
					list.add(hc.getName());
				}
			}
			if (list.isEmpty()) {
				text += ChatColor.GRAY + "(" + Lang.get("none") + ")\n";
			} else {
				Collections.sort(list);
				for (String s : list) {
					text += ChatColor.DARK_PURPLE + "- " + ChatColor.LIGHT_PURPLE + s + "\n";
				}
			}
			text += ChatColor.YELLOW + Lang.get("reqHeroesSecondaryPrompt");
			return text;
		}

		@Override
		public Prompt acceptInput(ConversationContext cc, String input) {
			if (input.equalsIgnoreCase(Lang.get("cmdClear")) == false && input.equalsIgnoreCase(Lang.get("cmdCancel")) == false) {
				HeroClass hc = Quests.heroes.getClassManager().getClass(input);
				if (hc != null) {
					if (hc.isSecondary()) {
						cc.setSessionData(CK.REQ_HEROES_SECONDARY_CLASS, hc.getName());
						return new HeroesPrompt();
					} else {
						String text = Lang.get("reqHeroesNotSecondary");
						text = text.replaceAll("<class>", ChatColor.LIGHT_PURPLE + hc.getName() + ChatColor.RED);
						cc.getForWhom().sendRawMessage(ChatColor.RED + text);
						return new HeroesSecondaryPrompt();
					}
				} else {
					cc.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("reqHeroesClassNotFound"));
					return new HeroesSecondaryPrompt();
				}
			} else if (input.equalsIgnoreCase(Lang.get("clear"))) {
				cc.setSessionData(CK.REQ_HEROES_SECONDARY_CLASS, null);
				cc.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("reqHeroesSecondaryCleared"));
				return new HeroesPrompt();
			} else {
				return new HeroesPrompt();
			}
		}
	}

	private class FailMessagePrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return ChatColor.YELLOW + Lang.get("reqFailMessagePrompt");
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			if (input.equalsIgnoreCase(Lang.get(Lang.get("cancel"))) == false) {
				context.setSessionData(CK.Q_FAIL_MESSAGE, input);
			}
			return new RequirementsPrompt(quests, factory);
		}
	}
}