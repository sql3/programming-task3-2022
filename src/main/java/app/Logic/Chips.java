package app.Logic;

import static app.Logic.ChipColor.NO_COLOR;

public class Chips {

    private ChipColor chipColor;

    private int quantity;

    public Chips() {
        this(0, NO_COLOR);
    }

    public Chips(int quantity, ChipColor chipColor) {
        this.chipColor = chipColor;
        this.quantity = quantity;
    }

    public Chips setChipColor(ChipColor chipColor) {
        this.chipColor = chipColor;
        return this;
    }

    public ChipColor getChipColor() {
        return chipColor;
    }

    public Chips setQuantity(int quantity) {
        if (quantity >= 0 && quantity < 16) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Chips can not be more than 15 and less than 0");
        }

        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity() {
        quantity--;
        if (quantity == 0) {
            chipColor = NO_COLOR;
        }
    }

    public void increaseQuantity(ChipColor chipColor) {
        if (this.chipColor == chipColor.changeColor()) {
            throw new IllegalArgumentException("You can't put your chips on the opponent's chips");
        }

        if (quantity == 0) {
            this.chipColor = chipColor;
        }

        this.quantity++;
    }
}
