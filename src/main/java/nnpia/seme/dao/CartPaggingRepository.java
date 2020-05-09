package nnpia.seme.dao;

import nnpia.seme.model.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartPaggingRepository extends PagingAndSortingRepository<Cart, Integer> {

    Page<Cart> findAllByUserIdAndDone(Object userid, Boolean done, Pageable var1);
}
