This is an in-progress work with the aim to model the combat system of a collectible card game like Magic: the Gathering.

Currently, I am still experimenting with the UI, and few game mechanics have been implemented yet.

At this point, this is best viewed as a showcase of my experimenting with RxJava, Dagger 2, and Data Binding.

/*****************************************************************************************************
 * USAGE
*****************************************************************************************************/
The bottom row represents the hand, the row above land resource cards, and the row above creature cards. Touching the creature cards will result in them rotating 90 degrees (tapping), and returning to normal (untapping) on a second touch. Above those are views that have not been implemented yet, which will contain opposing creature cards, a log of game actions, the turn number, and each player's life total. On the top right is the SWITCH button, which changes perspective to the other player, so you can play against yourself in the future.

In the future, the game will follow a simplified version of the rules of Magic: the Gathering. On your turn, you draw a card from your library, then you can move land cards from your hand to the battlefield, and use them to cast spells. Creature spells will be put on the battlefield, while sorceries will have an effect but then go to the graveyard. You can have creatures attack during the combat step of your turn. Once you go through all steps and phases on your turn, it switches to your opponent's turn. At any time, you can switch perspective to the other player and cast spells on your opponent's turn, if that spell allows you to.

/*****************************************************************************************************
 * NEXT STEPS
*****************************************************************************************************/
* Add tests for recent changes.
* Implement turn steps and phases, and switching player when the turn ends.
* Implement attacking, life point loss, and game loss.
* Implement full combat, with blocking and creature destruction, although limited to default creatures.
* Implement creatures with different power and toughness.
* Specify creatures in XML instead of programmatically.
* Implement lands and casting creature spells.
* Implement card drawing and the library.
* Have fully functioning card game, albeit with only creatures that have no abilities.
* Add static creature abilities, which can be specified in XML.
* Add sorcery spells, programmatically and then in XML.
* Implement "enter the battlefield" effects on creatures, which can be specified in XML.
* Add instant spells that can be cast on the opponent's turn.
* Add more complex abilities on creatures.
* Add other card types.

/*****************************************************************************************************
 * ISSUES
*****************************************************************************************************/
While working on this project, I had discovered problems with Data Binding. One feature is that switching players (the SWITCH button in the top right) will change the background color of every view in the main Fragment, which I planned to handle by using Data Binding to set the background of each view in the Fragment's XML with a function in the Fragment's View Model. However, when Data Binding is used to set the background of views in a PercentRelativeLayout, the background isn't properly sized. If Data Binding is not used and a static background is set in the layout, it loads properly, but even if Data Binding will always specify the same background resource, it doesn't load properly. My guess is that it always uses the dimensions of whatever view is loaded first, which in my case, was the bottom RecyclerView that takes up the width of the screen, but I have not experimented with this. I switched to setting the background programmatically, and it works properly.

Another feature is that clicking a creature (third row from the bottom) will cause the image to rotate 90 degrees (tap) or return to normal (untap), but after switching players a few times, some views would not properly do this. The data models were updating properly, but I found that the function to load the image, called from a Data Binding BindingAdapter function, was not being called. Instead of using Data Binding, I switched to just loading the image in onBindViewHolder() for the containing RecyclerView, and it works properly now.

At this point, I have mostly abandoned using Data Binding, with the exception of in fragment_board.xml, following the recommendations for how to use RecyclerViews with Model - View - View Model design at this link (https://medium.com/@hiBrianLee/writing-testable-android-mvvm-app-part-2-fd291290af83).