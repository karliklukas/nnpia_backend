package nnpia.seme.dao;

import nnpia.seme.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemDao extends JpaRepository<CartItem, Integer> {
}
