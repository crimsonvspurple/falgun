package red.eminence.commons.base;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.*;
import red.eminence.commons.errors.Detailed404Error;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


//@Log4j2
@RequiredArgsConstructor
public class BasePersistenceService<E, P extends BaseProjection>
{
    private final BaseRepository<E, P, String> baseRepository;
    
    public ExampleMatcher getDefaultMatcher ()
    {
        return ExampleMatcher.matching().withIncludeNullValues().withIgnorePaths("version");
    }
    
    public boolean existsById (String id)
    {
        return baseRepository.existsById(id);
    }
    
    public void throwIfNotFound (String id) throws Detailed404Error
    {
        if (!baseRepository.existsById(id)) {
            // this should not be called from BasePersistenceService!
            throw http404(id);
            //throw new UserFriendlyError(Message.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
    
    protected Detailed404Error http404 (String id)
    {
        val types = Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(getClass(), this.getClass().getSuperclass()));
        assert (types.length == 2);
        return new Detailed404Error(types[0], id);
    }
    
    public List<E> findAll ()
    {
        return baseRepository.findAll();
    }
    
    public List<E> findByIds (List<String> ids)
    {
        return baseRepository.findByIdIn(ids);
    }
    
    public List<P> findProjectionsByIds (List<String> ids)
    {
        return baseRepository.findByIdIsIn(ids);
    }
    
    public List<P> findAllProjections ()
    {
        return this.baseRepository.findBy();
    }
    
    public Page<E> findAll (Example<E> example, Pageable pageable)
    {
        return baseRepository.findAll(example, pageable);
    }
    
    public List<E> findAll (Sort sort)
    {
        return baseRepository.findAll(sort);
    }
    
    public Page<E> findAll (Pageable pageable)
    {
        return baseRepository.findAll(pageable);
    }
    
    protected <T> Detailed404Error http404 (Class<T> clazz, String id)
    {
        return new Detailed404Error(clazz, id);
    }
    
    public E insert (E model)
    {
        return baseRepository.insert(model);
    }
    
    public List<E> insert (Iterable<E> model)
    {
        return baseRepository.insert(model);
    }
    
    public E save (E model)
    {
        return baseRepository.save(model);
    }
    
    public List<E> saveAll (List<E> model)
    {
        return baseRepository.saveAll(model);
    }
    
    public long count ()
    {
        return baseRepository.count();
    }
    
    public void deleteById (String id)
    {
        baseRepository.deleteById(id);
    }
    
    public void deleteAll ()
    {
        baseRepository.deleteAll();
    }
    
    public E findByIdOrThrow (String id)
    {
        return this.findById(id).orElseThrow(() -> http404(id));
    }
    
    public Optional<E> findById (String id)
    {
        return baseRepository.findById(id);
    }
    
    public P findProjectionByIdOrThrow (String id, Class<P> type)
    {
        return this.findProjectionById(id, type).orElseThrow(() -> http404(id));
    }
    
    public Optional<P> findProjectionById (String id, Class<P> projection)
    {
        return baseRepository.findById(id, projection);
    }
    
    public P findProjectionByIdOrThrow (String id)
    {
        return this.findProjectionById(id).orElseThrow(() -> http404(id));
    }
    
    @SuppressWarnings ("unchecked")
    public Optional<P> findProjectionById (String id)
    {
        val types = Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(this.getClass(), this.getClass().getSuperclass()));
        assert (types.length == 2);
        val type = (Class<P>) types[1];
        return this.baseRepository.findById(id, type);
    }
}