package superapp.dataAccess;

import org.springframework.data.repository.ListCrudRepository;
import superapp.data.MiniappCommandEntity;

public interface MiniappCommandCrud extends ListCrudRepository<MiniappCommandEntity, String> {
}
