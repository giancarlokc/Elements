package com.elements.elements.element;

/**
 * Created by Giancarlo on 20/12/2015.
 * Possible element colors on the periodic table
 */
public enum ElementColor {
    GREEN(1), BLUE(2), RED(3), YELLOW(4), GREY(5), PINK(6);

    private final int id;

    ElementColor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
