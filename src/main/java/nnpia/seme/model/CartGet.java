package nnpia.seme.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartGet {
    private Items[] list;
    private Senior senior;

    public Items[] getList() {
        return list;
    }

    public void setList(Items[] list) {
        this.list = list;
    }

    public List<String> getItemList(){
        List<String> list = new ArrayList<>();
        for (Items item: this.list) {
            list.add(item.getItem());
        }
        return list;
    }

    public Senior getSenior() {
        return senior;
    }

    public void setSenior(Senior senior) {
        this.senior = senior;
    }
}
