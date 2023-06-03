package superapp.dataAccess;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import superapp.data.MiniappCommandEntity;

import java.util.List;

public interface MiniappCommandCrud extends ListCrudRepository<MiniappCommandEntity, String>,
        PagingAndSortingRepository<MiniappCommandEntity, String> {
    public List<MiniappCommandEntity> findAllByCommandIdContains(@Param("miniapp") String miniapp , Pageable pageable);
}
