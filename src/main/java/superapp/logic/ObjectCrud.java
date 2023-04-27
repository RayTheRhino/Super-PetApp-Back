package superapp.logic;

import org.springframework.data.repository.ListCrudRepository;
import superapp.data.SuperappObjectsEntity;

public interface ObjectCrud extends ListCrudRepository<SuperappObjectsEntity, String> {

}
