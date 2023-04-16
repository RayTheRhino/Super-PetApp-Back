package superapp.logic;

import org.springframework.data.repository.CrudRepository;

import superapp.data.SuperappObjectsEntity;

public interface ObjectCrud extends CrudRepository<SuperappObjectsEntity, String>{

}
