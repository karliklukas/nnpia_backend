package nnpia.seme.dao;

import nnpia.seme.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Long countByUserIdAndDone(Object userid, Boolean done);

    Long countByUserId(Integer userid);

}
