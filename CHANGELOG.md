# Changelog
## [1.7.1 (MONTH xth, 2025)](#1-7-1)

### Additions
- Added `/npcdelete` uuid parameter support and usuable to console
- Added `/spectateBattle <player>` command to spectate battles without having to manually walk up to the target.
- Added an in-game configuration screen, allowing all settings from `main.json` to be edited directly in-game.
- Added `/cobblemonconfig reload` command to reload `main.json` configuration. **Note:** Some settings require a server restart to take effect; use this command cautiously.
- Added `blacklisted_items_to_hold` and `whitelisted_items_to_hold` tags to allow for controlling which items players can give to their Pokémon. If the whitelist is empty, it will consider all item as allowed (unless they are in the blacklist).
- Pokémon now follow the mouse cursor on the Summary screen, with an option to disable this in the settings.
- Pokémon's held items can now be rendered, with a visibility toggle in the Summary screen.
- Added cosmetic item functionality for Pokémon. Certain cosmetic items can be given to applicable Pokémon via the interact menu.
  - Added the various log blocks as cosmetic items for Timburr and Komala.
- Added `visibility/hidden`, `visibility/hat` and `visibility/face` tags to control where and how certain items are rendered.

### Changes
- Renamed `chargeGainedPerTick` config to `secondsToChargeHealingMachine`.
- Made Blocks of Gold count as Big Nuggets when held by a Pokémon (for Fling functionality)
- Substantially optimised spawning checks mainly by front-loading biome filtering.

### Fixes
- Added Datapackable Item interactions with Pokemon
- Pokémon's held items can now be rendered, with a visibility toggle in the Summary screen.
- Added cosmetic item functionality for Pokémon. Certain cosmetic items can be given to applicable Pokémon via the interact menu.

#### Riding
- Added the ability to ride Pokémon by shift-right clicking and selecting the ride option. We are now a riding mod :D
  - If you get motion sickness, go to the configuration and "Disable Roll"!
- Your preferred camera mode per riding style is saved and restored when you mount/dismount.
- While many Pokémon can be ridden right now, many more will be added in future updates.

#### Cooking
- Added the Campfire Pot as well as loads of new food items (Poké Puffs, Ponigiri, Sinister Tea, etc)
    - Pots are made from apricorns and placed onto campfires.
    - The pot can be used to make bait for fishing, Poké Cakes, Lure Cakes, local specialties, and potions (less efficiently than a brewing stand), Exp. Candies, and more!
    - There is a different pot for each apricorn colour. These devs may have an addiction to apricorn colour varieties.
- Added Aprijuice, made from the Campfire Pot using apricorns with seasoning to give flavours.
    - Aprijuice can be fed to ride-able Pokémon to improve their riding characteristics, depending on the flavour and the Pokémon's nature.
- Added Lure Cakes, made from the Campfire Pot using honey and berries.
    - Lure Cakes can be used to modify the spawn rate of Pokémon in the wild.
    - Hidden Abilities can be obtained by using specific berries.
- Added Hearty Grains, a crop that grows in plains and swamps. Used for cooking.
- Added Tasty Tail, which you can get from Slowpokes... if you have a pair of shears. They don't mind, we asked.
- Redstone can close and open the lid of a campfire pot.
- Hoppers can be used to insert items into a campfire pot.
    - Top = Seasoning row
    - Sides = Crafting grid
    - Bottom = Extracts the result
- Observers will recognize when a campfire pot is closed or opened.
- Comparator calculates a signal strength depending on the items inside the campfire pot.

#### Visible Items and Cosmetics
- Pokémon's held items can now be seen being held by the Pokémon, with a visibility toggle in the Summary screen. This might be the cutest thing we've ever done.
- Pokémon now follow the mouse cursor on the Summary screen. There is an option to disable this in the settings. Why would you disable this?
- Added cosmetic item functionality for Pokémon. Certain cosmetic items can be given to their respective Pokémon via the interact menu. This is in addition to the held item.
  - Added the various log blocks as cosmetic items for Timburr and Komala.
  - Added cosmetics for Spoink, Gurdurr, Conkeldurr, Squirtle Line, Sneasler, Sandile line, Treecko line, Braixen, Delphox, and Dragonite. I wonder what items each of these Pokémon can be given?
  - Added Gilded Chest cosmetics for Chest Gimmighoul and Gholdengo.

#### AI and Behaviours
- Pokémon can now spawn and move in herds.
- Rebuilt Pokémon AI to use Minecraft's Brain system, allowing for more complex behaviours and interactions.
    - Pastured Pokémon will now sleep when appropriate, and may come up to you when they see you.
    - Dog Pokémon (Growlithe, Arcanine, etc.) intimidate Skeletons.
    - Cat Pokémon (Meowth, Persian, etc.) intimidate Creepers and Phantoms.
    - Sleep-related Pokémon (Munsharna, Komala, etc.) intimidate Phantoms.
    - Some Pokémon naturally hunt each other.
    - Some Pokémon herd together.
    - Some of your Pokémon will defend you.
    - Combees are now capable of gathering nectar from flowers and delivering it to either Saccharine leaves or hives. (Hive interactions are restricted to wild Combees)
    - Probably also added a lot of bugs.
    - Pastured Pokémon can be set to attack hostile mobs they see using the Pasture Block GUI.
- Some Pokémon now pitch their bodies in the direction they're moving, so fish swimming looks really cool.
- Added a Behaviour Editor screen to the NPC editing GUI.
- Added `/behaviouredit` for opening the Behaviour Editor on Pokémon and NPCs.
    - Editing variables for Pokémon is not yet supported.
- Lightning is now affected by a Pokémon's ability/typing.
    - Pokémon with the ability Lightning Rod draw in lightning similar to a lightning rod block.
      - This has a lower priority and range than lightning rod blocks
      - Struck Pokémon gain an immunity to lightning damage, and receive a temporary damage buff.
    - Pokémon with the ability Motor Drive are immune to lightning damage and receive a temporary speed buff when struck by lightning.
    - Pokémon with the ability Volt Absorb are immune to lightning damage and receive Instant Health for a short duration
    - Ground type Pokémon are immune to lightning damage.

#### PC Improvements
- Added the ability to rename PC Boxes by clicking on the name of a box.
- Added box options buttons for PC boxes, toggleable by clicking the right icon button in the bottom bar.
    - The option buttons on the left side allow for sorting the box by name, level, Pokédex number, gender, and type. Shift clicking allows for sorting in reverse order.
    - The button on the right allows for changing the box wallpaper.
    - 10 additional color-based wallpapers have been added.
        - 5 unlockable biome-based wallpapers have been added, which can be unlocked by visiting:
            - Forest biomes
            - Ocean biomes
            - Cave Biomes
            - The End
            - The Nether
    - You can very easily add your own wallpapers, including with custom unlock conditions, using resource and datapacks!
- Added filter functionality in PC UI, which supports `PokemonProperties` (e.g. `shiny=yes` shows all shiny Pokémon).
    - Names can be filtered by exact or partial matches, e.g. entering "cha" will show Charmander, Charmeleon, etc.
- Added `/changewallpaper <player> <boxNumber> <wallpaper>` command to change a box wallpaper through commands.
- Added `/renamebox <player> <boxNumber> <name>` command to rename a PC box through commands.
- Added `/pcsearch <player> <pokemonProperties>` command that searches for a specific Pokémon within a player's PC.
- Added `/pctake <player> <box> <slot>` command that takes a specific Pokémon from a player's PC. The Pokémon is deleted if the target is self or is run from the server.
- Added IVs and EVs stat displays in PC.
    - The displays can be cycled through by scrolling when the display is hovered over.
- Added ability to cycle through boxes by scrolling the mouse wheel when the box is hovered over.
- The PC will now open to last box viewed within a session.

#### Marks
- Added Pokémon markings, toggleable within the summary menu.
- Added data for all marks and ribbons from the mainline Pokémon games.
- Implemented fishing, personality, weather, and time related marks, which can (rarely) be granted to a wild-spawned Pokémon.
- Added `/givemark <player> <slot> <mark>` command to give a mark to a party Pokémon.
- Added `/takemark <player> <slot> <mark>` command to remove a mark from a party Pokémon.
- Added `/giveallmarks <player> <slot>` command to give all available marks to a Pokémon.

#### Other Additions
- Added an in-game configuration screen, allowing all settings from `main.json` to be edited directly in-game.
- Added level-up animations to the party overlay to replace the archaic and out of style chat messages.
- Added [LambDynamicLights](https://modrinth.com/mod/lambdynamiclights) support for items held by Pokémon, evolution stone blocks, evolution stone items, Pokédex, Luminous Moss, Flame Orb, and Magmarizer.
- Added the Clear Amulet, Grip Claw, Lagging Tail, Luminous Moss, Metal Alloy, Scroll of Darkness, Scroll of Waters.
- Added Hearty Grains, a new crop used in the new cooking mechanic.
- Added Tatami blocks and Tatami Mat blocks, made from Hearty Grain, for decorating builds.
- Added the Saccharine Tree, which can be found in the wild and is used to obtain Honey Bottles.
    - The leaves of the tree can be harvested to obtain honey, which can be used in the Campfire Pot to make Lure Cakes.
    - The log of the tree can be interacted with using a honey bottle to create a Honey-Slathered Saccharine Log, which increase the chances of spawning a Pokémon with a Hidden Ability.
    - Pokémon that have a Hidden Ability can be identified because they're dripping honey onto the ground. Messy eaters.
    - Dispensers now honey and un-honey the logs and the leaves.
- Added Hyper Training items (IV Modification) as well as some additional candy items to do so (Health Candy, Sickly Candy). You can cook 'em.
- Added Galarica Nut Bushes which generate on beaches.
- Added a new 69th berry, Eggant. We're moving on.
- Berries can now be smelted into dyes.
- Smeargle spawns with differing tail colour depending on its [Characteristic](https://bulbapedia.bulbagarden.net/wiki/Characteristic) stat.
- Added functionality to the Everstone when held by a Pokémon; suppresses evolution notification and hides evolve button in summary interface.
- Added crafting recipes for Masterpiece Cup, Eject Pack.
- Added modification to Minecraft Creative Inventory search to account for item names that contain `poké` when input contains `poke`. It's a small change, but man.
- Mooshtanks will switch between their red and brown variant when struck by lightning.
- Added Statistics for battles won, fled, and total, Pokémon captured (shiny and total), Pokémon released, dex entries added, trades completed, Pokémon levels gained, Pokémon evolved, fossils revived, PokéRod casts and reel-ins and Pokémon times ridden.
- Added new advancements: Didn't Stop To Think, We Need To Cook, Pokémon Jockey!, Souped-Up Stats, Culinary World Tour, Mochi Mochi!, A Luring Aroma, That's Bait, Star Pokéathlete, Home on the Range, and Just a Smackerel.
- Added bubble quirk to Kingler; like Krabby, Kingler will blows bubbles during dusk.
- Added a new gamerule, `healerHealsPC`. When set to true, a successful use of a healer will also heal all the Pokémon in that player's PC.
- Added `min_perfect_ivs` property to PokemonProperties to specify the minimum number of perfect IVs of the Pokémon.
- Added `scale_modifier` property to PokemonProperties to modify the scale of the Pokémon.
- Added `defaultKeyItems` config option to specify which key items players always have.
- Added `blacklisted_items_to_hold` and `whitelisted_items_to_hold` tags to allow for controlling which items players can give to their Pokémon. If the whitelist is empty, it will consider all item as allowed (unless they are in the blacklist).
- Added `/transformmodelpart (position|rotation|scale) <modelPart> <transform: x y z>` that can add transformations to a Pokémon's model part which can be used for good or for comedy.
  - The player executing the command must be facing the target Pokémon entity. Transformations are not persistent and will revert when resources are reloaded.
- Added `/runmolang <molang> [<npc>|<player>|<pokemon>]` that executes a Molang expression with the provided options as environment variables, as well as the entity (as `q.entity`) that executed the command.
- Added `/changejointscale` to change the scale of a joint in a model. Unbelievably funny to play around with but exists for testing purposes.
- Added `/calculateseatpositions` command for approximating hitbox locations for riding configurations. Good for when you're adding riding to your custom Pokémon!
- Added `/npcdelete` UUID parameter support and made it usable from console.
- Added `/spectatebattle <player>` to spectate battles without having to manually walk up to the target.
- Added `/cobblemonconfig reload` to reload the `main.json` configuration. **Note:** Some settings require a server restart to take effect; use this command cautiously.
- Added `/boxcount` to change the number of PC boxes a player has.

### Pokémon Added

#### Gen 2
- Marill
- Azumarill
- Hoppip
- Skiploom
- Jumpluff
- Dunsparce
- Togepi
- Togetic
- Unown
- Houndour
- Houndoom
- Ho-Oh
- Lugia
- Alola Bias Pichu (built-in resourcepack)

#### Gen 3
- Azurill
- Meditite
- Medicham
- Electrike
- Manectric
- Spoink
- Grumpig
- Swablu
- Altaria
- Snorunt
- Glalie
- Shuppet
- Banette
- Latias
- Latios

#### Gen 4
- Bronzor
- Bronzong
- Croagunk
- Toxicroak
- Froslass
- Glameow
- Purugly
- Skorupi
- Drapion
- Togekiss

#### Gen 5
- Pansage
- Simisage
- Pansear
- Simisear
- Panpour
- Simipour
- Munna
- Musharna
- Blitzle
- Zebstrika
- Drilbur
- Excadrill
- Trubbish
- Garbodor
- Gothita
- Gothorita
- Gothitelle
- Solosis
- Duosion
- Reuniclus
- Tynamo
- Eelektrik
- Eelektross
- Axew
- Fraxure
- Haxorus
- Mienfoo
- Mienshao
- Sewaddle
- Swadloon
- Leavanny
- Druddigon
- Minccino
- Cinccino
- Vanillite
- Vanillish
- Vanilluxe

#### Gen 6
- Skiddo
- Gogoat
- Espurr
- Meowstic
- Inkay
- Malamar
- Hawlucha
- Dedenne
- Noibat
- Noivern
- Furfrou
  - You can change Furfrou's form by using shears on it while it holds a certain dye in its cosmetic slot.

#### Gen 7
- Yungoos
- Gumshoos
- Drampa
- Alolan Grimer
- Alolan Muk
- Togedemaru

#### Gen 8
- Skwovet
- Greedent
- Chewtle
- Drednaw
- Toxel
- Toxtricity
- Morpeko
- Silicobra
- Sandaconda
- Sinistea
- Polteageist
- Galarian Corsola
- Cursola
- Mr. Rime
- Galarian Mr. Mime
- Clobbopus
- Grapploct
- Galarian Weezing

#### Gen 9
- Smoliv
- Dolliv
- Arboliva
- Tarountula
- Spidops
- Orthworm
- Dudunsparce
- Cyclizar
- Poltchageist
- Sinistcha
- Capsakid
- Scovillain
- Tadbulb
- Bellibolt
- Toedscool
- Toedscruel
- Rellor
- Rabsca
- Bramblin
- Brambleghast

### Animation updates for the following Pokémon
- Garchomp
- Tropius
- Nosepass
- Probopass
- Sneasel
- Weavile
- Sneasler
- Braixen
- Delphox
- Cinderace
- Kangaskhan
- Gossifleur
- Eldegoss
- Stonjourner
- Wailmer
- Lechonk
- Oinkologne
- Dratini
- Dragonair
- Dragonite
- G. Corsola
- Cursola
- Dunsparce
- Dudunsparce (both forms)
- Porygon
- Porygon2
- Porygon-Z
- Wattrel
- Kilowattrel
- Golurk
- Spinarak
- Ariados
- Wyrdeer
- Tyrantrum
- Pidove
- Tranquill
- Unfezant
- Beldum
- Metang
- Metagross
- Plusle
- Minun
- Murkrow
- Honchkrow
- Larvesta
- Volcarona
- Dwebble
- Crustle
- Mr. Mime
- Flygon
- Pichu
- Pikachu
- Raichu
- Alolan Raichu
- Espathra
- Abra
- Kadabra
- Alakazam
- Deino
- Zweilous
- Hydreigon
- Slaking
- Klink
- Klang
- Klinklang
- Baltoy
- Claydol
- Mamoswine
- Rufflet
- Braviary
- Girafarig
- Farigiraf
- Rookidee
- Corvisquire
- Corviknight
- Venipede
- Whirlipede
- Scolipede
- Venusaur
- Parasect
- Bastiodon
- Amaura
- Varoom
- Revavroom
- Bouffalant
- Lickilicky
- Blaziken
- Rhyhorn
- Rhydon
- Rhyperior
- Tauros
- Tauros - Paldea Aqua
- Tauros - Paldea Blaze
- Tauros - Paldea Combat
- Dewgong
- Corphish (updated placeholders for crustaceous tripod gait legs)
- Crawdaunt (updated placeholders for crustaceous bipedal legs)
- Dragapult
- Camerupt
- Relicanth
- Heracross
- Salamence
- Staraptor
- Dusknoir
- Arcanine
- Carvanha
- Sharpedo
- Mimikyu
- Dewgong
- Mime Jr.
- Gyarados
- Lapras

### Model updates for the following Pokémon
- Cleffa
- Clefairy
- Clefable
- Gyarados
- Dragonite
- Eevee
- Vaporeon
- Jolteon
- Flareon
- Espeon
- Umbreon
- Leafeon
- Glaceon
- Sylveon
- Treecko
- Grovyle
- Sceptile
- Honchkrow
- Gible
- Gabite
- Garchomp
- Pidgeot
- Nosepass
- Probopass
- Kangaskhan
- Scorbunny
- Raboot
- Cinderace
- Magnemite
- Magneton
- Magnezone
- Beldum
- Metang
- Metagross
- Hoothoot
- Noctowl
- Teddiursa
- Ursaring
- Ursaluna
- Heatmor
- Bouffalant
- Sigilyph
- Sharpedo
- Maractus
- Clodsire
- Scyther
- Scizor
- Cacturne
- Taillow
- Swellow
- Seel
- Dewgong
- Honedge
- Doublade
- Aegislash
- Drowzee
- Hypno
- Mudkip
- Marshtomp
- Swampert
- Shelmet
- Escavalier
- Klink
- Klank
- Klinklang
- Spinarak
- Ariados
- Pidove
- Tranquill
- Unfezant
- Sobble
- Drizzile
- Inteleon
- Plusle
- Minun
- Murkrow
- Zorua
- Zoroark
- Mime Jr.
- Mr. Mime
- Pichu
- Pikachu
- Raichu
- Alolan Raichu
- Dusknoir
- Deino
- Zweilous
- Hydreigon
- Dreepy
- Drakloak
- Dragapult
- Mamoswine
- Rookidee
- Corvisquire
- Corviknight
- Venipede
- Whirlipede
- Scolipede
- Farigiraf
- Staryu
- Starmie
- Gimmighoul
- Gholdengo
- Starly
- Staravia
- Staraptor
- Varoom
- Revavroom
- Nickit
- Thievul
- Litwick
- Lampent
- Chandelure
- Rayquaza
- Aerodactyl
- Basculin
- Basculegion
- Shroodle
- Grafaiai
- Steelix
- Dratini
- Dragonair
- Dragonite
- Cutiefly
- Ribombee
- Added trades for Saccharine Saplings, Hearty Grains, Chipped Pot, and Masterpiece Teacup to the Wandering Trader.
- Added brewing recipe for Throat Spray.

### Changes
- Convert the riding freelook button to a configurable keybinding.
- Reduced cost of the Vivichoke Seed trade with the Wandering Trader.

### Fixes
- Prevent displaying ride controls overlay to passengers.
- Fixed Poké Snack spawning sound positioning.
- Fix player suffocation on vanilla mounts experienced on some worlds.
- Stop passengers from hearing the shiny noise of a ridden shiny Pokémon.
- Ponigiri can no longer be eaten at full hunger.
- Medicinal Brew's Campfire Pot recipe now correctly displays that it can be made using an empty glass bottle or a filled bottle,
- Fix incorrect camera pivot on Bird, Jet, and Dolphin mounts.
- Fixed Combees not depositing honey upon leaving a hive if they entered with nectar.
- Safer reading of brain memories in hive mixins.
- Fixed crash when opening a PC box with certain wallpapers when using *VulkanMod*.
- Fix player suffocation on vanilla mounts experienced on some worlds.
- Stop passengers from hearing the shiny noise of a ridden shiny Pokémon.
- Fix a glitch where friendship would reset to the default value when evolving a Pokémon.

### Developer
- A finished battle now has winners and losers set inside of `PokemonBattle` instead of them always being empty.
- Dialogues are correctly removed from memory when they are stopped.
- Dialogues with variably-set initial pages now properly start timeout tracking.
- Deprecated `EVs#add(Stat, Int)` in favour of `EVs#add(Stat, Int, EvSource)`. You most likely want to use `SidemodEvSource` but please check other implementations or make your own.
- Removed the NbtItemPredicate class, all the mod usages now use the vanilla item predicate solution. This causes breaking changes on Fossil, HeldItemRequirement and ItemInteractionEvolution
- Renamed Cobblemon's creative tabs to start with "Cobblemon: " to distinguish Cobblemon's tabs from tabs for other mods.
- Various items now have a rarity value.

### Changes
- When using the `cobblemon` or `generation_9` capture calculators a critical capture with a single shake will always play for successful captures when you've already registered the Pokémon as caught in your Pokédex.
- Removed all VaryingModelRepository subclasses and collapsed them into the parent class.
- Refactored the following events to `.Pre` and `.Post` for consistency:
- `PokemonSentEvent`
- `ExperienceGainedEvent`
- `BattleStartedEvent`
- Updated `PokemonSentEvent` parameters to include the position and world of the Pokémon being sent out.
- Updated `EvolutionCompleteEvent` parameters to include the source Pokémon that evolved.
- Updated `HatchEggEvent.Post` to include the Pokémon that hatched.
- Reworked observable handling in `Pokemon.kt` to cut down on RAM usage and clarify the file.
  - Note: This will break mods that used our observable functionality there or in MoveSet, IVs, EVs, or BenchedMoves.
  - Using `Pokemon#onChange()` is now the way to mark a Pokémon as needing a save.
- Updated NPCEntity beam positioning to properly account for the baseScale property.
- Updated NPCEntity pokeball throw positioning to properly account for the baseScale property.
- Fixed `[Pokemon].copyFrom` error causing forms, IVs, and EVs to not be applied properly when using `[Pokemon].loadFromJSON` or `[Pokemon].loadFromNBT`
- Added new item class, `WearableItem`. Instances of this class should have a corresponding 3D model. These models render when the items display context is `HEAD`.
- Added new LearnsetQuery types:
  - `LEGAL` for moves that are innately compatible and learnable by the Pokémon.
  - `LEGACY` for moves that were once officially learnable by the Pokémon but aren't due to GameFreak's re-balancing.
  - `SPECIAL` for moves that are not learnable by the Pokémon but may have appeared in a special event or distribution.
- Pokémon species and forms can be configured to be immune to fire, lava, and magma blocks.
- The IVs class has now been extended to include Hyper Trained values.
- Added `Pokemon#hyperTrainIV()` and `IVs#setHyperTrainedIV(Stat, Int)`.
- Added `HyperTrainedIvEvent.Pre` and `HyperTrainedIvEvent.Post`.
- Added `Pokemon#validateMoveSet()` to validate an existing Pokémon's moveset, clearing illegal moves.
- Added a `hoverText` option to PartySelectCallback, to display a tooltip on hovering over a Pokémon in the selection screen.
- `PokemonEntity` instances spawned into the world now appropriately finalize the spawn for mod compatibility.
- Added `PokedexManager.obtain` as a replacement for .catch which is not a friendly function name in Java.
- Added `Pokemon#hyperTrainIV()` and `IVs#setHyperTrainedIV(Stat, Int)`
- `ElementalType` now implements `ShowdownIdentifiable` to ensure the communcation with showdown stays consistent (also in regards to TeraTypes)
- Pokémon no longer have a change observable.
- Pokémon can now have a behaviour changing the value of a species feature on lightning hit: 
  ```JSON
  { 
    "behaviour": {
      "lightningHit": {
        "rotateFeatures": [
          {
            "key": "mooshtank",
            "chain": ["red", "brown"]
          }
        ]
      }
    }
  }
  ```
- Removed Npc interface from NPCEntity. The interface is unused and in vanilla is only implemented by VillagerEntity as a means to disable villagers with the `spawn-npcs` server property.
- Added new `Observable#subscribe` methods that take Java Consumers to make usage in Java a little cleaner.
- Annotated a bunch of Kotlin methods and fields for cleaner Java names.
- Fixed `Species#create` using the species name instead of identifier, which had led to certain mismatches generating random pokémon.
- baseScale in NPCs has been removed from classes and presets, and in the entity it is replaced with hitboxScale and renderScale.
- Added `EntityCallbacks` to `NPCEntity` and `PokemonEntity`. You can trigger custom callback types by using `[Entity].callbacks.process(...)`
- Added `RenamePCBoxEvent.Pre` and `RenamePCBoxEvent.Post` events to prevent players from renaming a box or changing their input.
- Added `ChangePCBoxWallpaperEvent.Pre` and `ChangePCBoxWallpaperEvent.Post` to prevent players from changing wallpapers or changing their selection.
- Added `WallpaperCollectionEvent` which gets called when clients connect to a server, allowing the server to handle which of the client-found wallpapers it's allowed to move (collected wallpapers can be removed for example to make it "vanish" client-side).
- Added `WallpaperUnlockedEvent`.
- Renamed `SetPCBoxPokemonPacket` and the respective handler to `SetPCBoxPacket`.
- Spawning Influences now have the context of what the other buckets are when adjusting bucket weights. This will break existing influences that do bucket weight adjustment.
- Renamed heaps of things in the spawning system to make more sense.
    - SpawningContext is now SpawnablePosition
    - WorldSlice is SpawningZone
    - SpawningProspector is now SpawningZoneGenerator
-  Majorly refactored the hierarchy of Spawner
- The base Spawner interface provides more functions to allow single-point and area spawning given appropriate inputs.
- TickingSpawner is removed in favour of outside code handling ticking logic.
- AreaSpawner is removed.
- BasicSpawner is the first implementation of Spawner which can be used for any purpose.
- PlayerSpawners are now mixin'd into ServerPlayer and ticked from ServerPlayer#tick.
- A hierarchy diagram can be found at `./docs/spawner-hierarchy.png` in the mod repository.
- The SpawnerManager class has been removed as its functionality is all now handled elsewhere.
- Renamed things in Spawn Rules to go with the other renames:
    - contextSelector is now spawnablePositionSelector
    - context is now spawnable_position
- Added `.Pre` and `.Post` to the following events:
    - `PokemonRecallEvent`
    - `TradeEvent`
    - `EvGainedEvent`

### Molang & Datapacks

### Particles

### Localization
