package red.eminence.commons.base;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface BaseRepository<E, P, String> extends MongoRepository<E, String>
{
    /**
     * Find {@code E} by {@code id} and project them to the given {@code P}.
     *
     * @return list of projections
     */
    Optional<P> findById (String id, Class<P> projection);
    /**
     * Find all entities of type {@code E} and project them to the given {@code P}.
     *
     * @return list of projections
     */
    List<P> findBy ();
    List<E> findByIdIn (List<String> ids);
    List<P> findByIdIsIn (List<String> ids);
}
