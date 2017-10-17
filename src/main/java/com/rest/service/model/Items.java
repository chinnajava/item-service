
package com.rest.service.model;

public class Items implements Comparable {

    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public int compareTo(Object o) {
        Items items = (Items) o;
        if (this.getItem().getId().equals(items.getItem().getId())
                && this.getItem().getTimestamp().isEqual(items.getItem().getTimestamp())) {
            return 0;
        }
        if (this.getItem().getTimestamp().isAfter(items.getItem().getTimestamp()))
            return -1;
        else
            return 1;

    }
}
