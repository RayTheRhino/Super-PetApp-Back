package superapp.logic;

import org.springframework.data.repository.ListCrudRepository;
import superapp.data.MiniappCommandEntity;

public interface MiniappCommandCrud extends ListCrudRepository<MiniappCommandEntity, String> {
}
