package guru.springframework.repositories;

import guru.springframework.domain.PageView;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jt on 2/25/17.
 */
public interface PageViewsRepository extends CrudRepository<PageView, Long> {
}
