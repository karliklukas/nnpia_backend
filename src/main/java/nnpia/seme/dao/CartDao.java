package nnpia.seme.dao;

import javafx.util.Pair;
import nnpia.seme.model.Cart;
import nnpia.seme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartDao extends JpaRepository<Cart, Integer> {
    Long countByUserIdAndDone(Object userid, Boolean done);

    Long countByUserId(Integer userid);

}
