package aroma1997.betterchests.container;

public class ContainerHelper {

	private static final int[] ROWS = new int[] {
			9,
			18,
			27,
			36,
			45,
			54
	};

	public static int getHeightForSlots(int slots) {
		for (int i = 0; i < ROWS.length; i++) {
			if (slots <= ROWS[i]) {
				return i + 1;
			}
		}
		return ROWS.length;
	}

	public static int getWidthForSlots(int slots) {
		int height = getHeightForSlots(slots);
		int width = slots / height;
		if (slots % height != 0) {
			width++;
		}
		return width;
	}
}
