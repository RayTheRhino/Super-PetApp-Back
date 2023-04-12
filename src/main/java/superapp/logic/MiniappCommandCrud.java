package superapp.logic;

import org.springframework.data.repository.CrudRepository;

import superapp.data.MiniappCommandEntity;

public interface MiniappCommandCrud extends CrudRepository<MiniappCommandEntity, String>{
}
