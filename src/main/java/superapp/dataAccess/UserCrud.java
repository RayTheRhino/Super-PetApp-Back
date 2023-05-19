package superapp.dataAccess;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import superapp.data.UserEntity;

import java.util.List;

public interface UserCrud extends ListCrudRepository<UserEntity, String>,
        PagingAndSortingRepository<UserEntity, String> {

}
