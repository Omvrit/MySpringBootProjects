package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends MongoRepository<UserEntity,String> {
    public UserEntity findByUsername(String username);
    public UserEntity deleteByUsername(String username);
}
