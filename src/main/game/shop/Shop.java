package main.game.shop;

import main.game.shop.storage.Item;

import java.util.List;

public class Shop {
    private List<Item> storage;

    public Shop(List<Item> storage) {
        this.storage = storage;
    }

    public List<Item> getStorage() {
        return storage;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "storage=" + storage +
                '}';
    }
}
