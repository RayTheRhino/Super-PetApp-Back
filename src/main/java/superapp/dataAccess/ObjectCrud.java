package superapp.dataAccess;

import org.springframework.data.repository.ListCrudRepository;
import superapp.data.SuperappObjectsEntity;

import java.util.List;


public interface ObjectCrud extends ListCrudRepository<SuperappObjectsEntity, String> {
    public List<SuperappObjectsEntity> findAllByChildrenContains(SuperappObjectsEntity theChild);

}
