package superapp.logic;

import org.springframework.data.repository.CrudRepository;

import superapp.data.UserEntity;

public interface UserCrud extends CrudRepository<UserEntity, String> {

}
