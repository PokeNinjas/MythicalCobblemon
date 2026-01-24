# Changelog
## [1.7.2 (JANUARY Xth, YEAR)](#1-7-2)

### Additions
- Added Saccharine wood furniture for Adorn.
- Added the Partner Mark, which party Pokémon have a very small chance to earn as the player walks, provided they have enough friendship.
- Added ride style labels to ride stats in Pokédex.
- Added ride style icons to ride stats in summary.
- Added decorative block models for existing items: Potion, Super Potion, Hyper Potion, Max Potion, Full Restore, Antidote, Awakening, Burn Heal, Ice Heal, Paralyze Heal, Full Heal, Ether, Elixir, Max Ether, Max Elixir, X Accuracy, X Attack, X Defence, X Special Attack, X Special Defence, X Speed, Dire Hit, Guard Spec, Weakness Policy, Blunder Policy, Cleanse Tag, and Spell Tag.
- Added decorative sign blocks, Plaques.
- Added more seasonings for sinister tea
  - Mental Herb - Mental Restoration I for 10 seconds, which staves off Insomnia
  - White Herb - Removes negative effects
  - Milk, Moomoo Milk - Removes effects
- Added dispenser support for:
  - Applying a Honey Bottle to Saccharine Logs.
  - washing honey off Saccharine Slathered Logs.
  - applying & removing honey on Saccharine Leaves.
- Added riding statistics: `Distance by Pokémon on Land`, `Distance by Pokémon in Air` and `Distance by Pokémon in Liquid`.
- Added `label` as an alternate key to the `tag` property.

### New rideable Pokémon
- Honchkrow 
- Mantine

### Pokémon Added

#### Gen 
- Throh
- Sawk

#### Gen 6
- Litleo
- Pyroar
- Spritzee
- Aromatisse
- Swirlix
- Slurpuff

### Model updates for the following Pokémon
- Falinks
- Cottonee
- Whimsicott
- Ferroseed
- Ferrothorn
- Chingling
- Chimecho

### Animation updates for the following Pokémon
- Mantine (ride animations)
- Flygon (ride jump animation)
- Honchkrow (ride animations)
- Croagunk
- Toxicroak


### Changes
- Added a new config option, `Enable In-Flight Dismounting` (default: off), which lets you dismount while riding a Pokémon in the air.
- Ride sounds have been separated into stereo for passengers and mono for other players. Riding will now sound more spacious.
- Battle AI now uses a smarter threshold for switching, reducing unnecessary switches.
- AI will always use the most damaging move when at low HP and when it is not switching, improving endgame decision-making.
- Items given from interacting with Saccharine Leaves are placed in the active hotbar slot if possible.
- Added support for optional message variants in battle activate instructions for more context-specific battle text.
  - Example: `this.add('-activate', pokemon, 'ability: example', '[msg]message1');` will parse to lang key `cobblemon.battle.activate.example.message1`

### Fixes
- Fixed Furfrou not being trimmable on NeoForge.
- Fixed all Pokémon being saved to chunks and never despawning even when they should be.
- Fixed Poké Snacks crashing if there was no available spawn.
- Fixed honey from Saccharine Leaves being harvestable at less than the max age.
- Fixed possible error on world generation when Combee are placed in naturally generated bee nests.
- Fixed crashing when viewing another player in spectator mode.
- Fixed Galarian Weezing crashing the world if it decides to blink the wrong way.
- Fixed Galarian Mr. Mime incorrectly being rideable.
- Fixed missing apricorn textures for Adorn blocks.
- Fixed Apricorn block model rotation for counters/sinks/cupboards/drawers to match that of most recent version of Adorn.
- Fixed mochi items consuming two items at a time.
- Fix item interaction sometimes not working properly when playing on servers.
- Fixed Soothe Bell not being properly tagged as a held item.
- Fixed Mint Leaves not being usable for filling the Resurrection Machine.
- Fixed Wishiwashi not schooling properly.
- Fixed battle item duplication issue.
- Fixed Super Potion recipes incorrectly using Hondew Berry instead of Aguav Berry.
- Fixed PokeSnack/Bait effects so EV Yield, Type, and Egg Group filters now all apply together when weighting spawns.
- Fixed the movesets of certain Pokémon being incorrectly sanitized upon reload, causing some forms to keep losing moves.
- Fixed the ordering of Aprijuice's riding stat boosts so they always have the same order.
- Fixed selected leading Pokémon in battle not being used when levels are raised.
- Fixed the bottom half of pasture blocks having an incorrect hit-box when facing north or south. HUGE issue!!
- Fixed gimmicks not being usable in double / triple battles.
- Fixed gimmick buttons showing when already used in the same turn in double / triple battles.
- Fixed Fresh Start Mochi EV reset not syncing health changes properly.
- Fixed Pokémon spawn influences not being specific to regional forms.
- Fixed orientation not getting reset after crashing while flying and getting back on the Pokémon.
- Fixed shiny Pokémon not respecting the `silent` flag on spawn.
- Fixed memory leak from battles not being cleaned up after ending.
- Fixed Pokémon interactions not playing sound effects when the volume was not specified.
- Fixed Pokémon interaction cooldowns taking longer than intended when the Pokémon was in the player’s party.
- Fixed Pokémon interactions occasionally crashing the game when spamming an interaction with a 0 cooldown.
- Fixed Pokémon fullness decreasing more than intended when the Pokémon was pastured.
- Fixed crashing when sending a Pokémon out after editing its form.
- Fixed invulnerability-bypassed damage being resisted by invulnerable NPC entities.
- Fixed killer not being set early enough on a wild Pokémon entity from WinInstruction.
- Fixed requirements such as `biomeCondition` crashing the game when used to define Pokémon interactions.
- Fixed an issue that prevented registering custom dispenser behaviours (no longer overrides vanilla behaviour).
- Fixed an issue navigation that prevented Combee from pathing into and out of Saccharine Leaves.
- Added Saccharine Boats and Saccharine Boat with Chests to relevant boat tags.
- Fixed `/calculateseatpositions` expecting a locator format that even we don't use. It needs an underscore after "seat".
- Fixed the `run_script` Molang function to not fail if the environment's context is null.
- Fixed the Molang functions `date_local_time`, `date_of`, `date_is_after` to use the correct date format.
- Fixed Fabric network packets not being handled immediately, fixing incompatibilities with Supplementaries, Prometheus, Sleep Tight, and others.
- Fixed 'Plain' prefix not being applied to all low quality Aprijuice.
- Fixed Scraggy line, Milcery line, and Morpeko spawning on unnatural blocks. 
- Changed the resource location of pokeball icons in the gui to use the balls namespace instead of cobblemon, allowing mods to properly use their own name space for these textures

### Developer
- Changed the `owner` parameter in the `OwnerQueryRequirement` interface from `ServerPlayer` to `Player`. This method is now also called on the client to verify whether a Pokémon interaction succeeded, so make sure to update your implementations to handle both server and client contexts.
- Added `pnx` to the `BattleFaintedEvent` and `FormeChangeEvent`.
- Added register methods for custom instructions to `ShowdownInterpreter`.
- Changed callback operations in `BattlePokemon` to allow multiple callbacks and is now mutable.
- Added `display_name`, `description`, and `max_pp` functions to the `Movetemplate` struct.
- Changed the implementations of `AIBattleActor` to be open classes to allow extension.

### Molang & Datapacks
- Ride sounds can now be set as exclusive to passengers.
- Ride sounds no longer play when submerged unless specified with a new setting.
- Exception handling has been added to `run_molang`, resolving some crashes caused by malformed MoLang expressions.
- Added the `create_itemstack`, `has_inventory_space`, `set_inventory_slot`, and `give_item` MoLang functions for item and inventory utility.
- Added the `get_move_from_id` MoLang function for general move info queries without requiring a Moveset object to access.

### Particles

### Localization
- Updated translations for:
  - French
  - Canadian French
  - Simplified Chinese
  - Ukrainian
  - Brazilian Portuguese
  - Spanish
  - Korean
  - Japanese
