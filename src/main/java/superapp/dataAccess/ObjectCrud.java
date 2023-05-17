package superapp.dataAccess;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import superapp.data.SuperappObjectsEntity;

import java.util.List;
import java.util.Optional;


public interface ObjectCrud extends ListCrudRepository<SuperappObjectsEntity, String> {
    public Optional<List<SuperappObjectsEntity>> findAllByChildrenContains(SuperappObjectsEntity theChild);
    public Optional<List<SuperappObjectsEntity>> findAllByChildrenContainsAndActive(SuperappObjectsEntity theChild, @Param("childActive") boolean childActive);
    public Optional<List<SuperappObjectsEntity>> findAllByParentsContains(SuperappObjectsEntity parentObject);
    public Optional<List<SuperappObjectsEntity>> findAllByParentsContainsAndActive(SuperappObjectsEntity parentObject, @Param("parentActive") boolean active);
    public Optional<List<SuperappObjectsEntity>> findAllByType(@Param("type") String type);
    public Optional<List<SuperappObjectsEntity>> findAllByTypeAndActive(@Param("type") String type, @Param("active") boolean active);
    public Optional<List<SuperappObjectsEntity>> findAllByAlias(@Param("alias") String alias);
    public Optional<List<SuperappObjectsEntity>> findAllByAliasAndActive(@Param("alias") String alias, @Param("active") boolean active);
//    public Optional<List<SuperappObjectsEntity>> findAllByLocation(@Param("lat") double lat, @Param("lng") double lng);
//    public Optional<List<SuperappObjectsEntity>> findAllByLocationAndActive(@Param("lat") double lat, @Param("lng") double lng, @Param("active") boolean active);
    public Optional<List<SuperappObjectsEntity>> findAllByActive(@Param("active") boolean active);

    public Optional<SuperappObjectsEntity> findByObjectIdAndActive(@Param("objectId") String objectId , @Param("active") boolean active);


}
