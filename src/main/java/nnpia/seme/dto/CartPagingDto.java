package nnpia.seme.dto;

import nnpia.seme.model.Cart;

import java.util.List;

public class CartPagingDto {
    private List<Cart> list;
    private Long totalElements;

    public List<Cart> getList() {
        return list;
    }

    public void setList(List<Cart> list) {
        this.list = list;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
