package superapp.dataAccess;

import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import superapp.data.SuperappObjectsEntity;

import java.util.List;
import java.util.Optional;


public interface ObjectCrud extends ListCrudRepository<SuperappObjectsEntity, String>,
        PagingAndSortingRepository<SuperappObjectsEntity, String> {
    public List<SuperappObjectsEntity> findAllByChildrenContains(SuperappObjectsEntity theChild);
    public List<SuperappObjectsEntity> findAllByChildrenContainsAndActive(SuperappObjectsEntity theChild, @Param("childActive") boolean childActive);
    public List<SuperappObjectsEntity> findAllByParentsContains(SuperappObjectsEntity parentObject);
    public List<SuperappObjectsEntity> findAllByParentsContainsAndActive(SuperappObjectsEntity parentObject, @Param("parentActive") boolean active);
    public List<SuperappObjectsEntity> findAllByType(@Param("type") String type, Pageable pageable);
    public List<SuperappObjectsEntity> findAllByTypeAndActive(@Param("type") String type, Pageable pageable, @Param("active") boolean active);
    public List<SuperappObjectsEntity> findAllByAlias(@Param("alias") String alias, Pageable pageable);
    public List<SuperappObjectsEntity> findAllByAliasAndActive(@Param("alias") String alias, Pageable pageable, @Param("active") boolean active);
    public List<SuperappObjectsEntity> findAllByLocationNear(@Param("location") Point location , @Param("maxDistance") Distance distance, Pageable pageable);
    public List<SuperappObjectsEntity> findAllByLocationNearAndActive(@Param("location") Point location , @Param("maxDistance") Distance distance, Pageable pageable, @Param("active") boolean active);
    public List<SuperappObjectsEntity> findAllByActive(@Param("active") boolean active, Pageable pageable);
    public Optional<SuperappObjectsEntity> findByObjectIdAndActive(@Param("objectId") String objectId, @Param("active") boolean active);


}
