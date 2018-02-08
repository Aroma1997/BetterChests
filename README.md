# BetterChests Mod
This mod adds chests to minecraft, that are better than normal chests because they can be upgraded. It also adds better backpacks, that are better than normal backpacks, because they can be upgraded.

It also has a plugin API. To use the plugin API, you can just add BetterChests to the dependency block in your build.gradle file.
```
repositories {
	maven {
		name = "aroma"
		url = "http://files.aroma1997.org/maven/"
	}
}

dependencies {
	compile "aroma1997.betterchests:BetterChests-${MCVERSION}:${MODVERSION}:api"
	runtime "aroma1997.betterchests:BetterChests-${MCVERSION}:${MODVERSION}:deobf"
}
```