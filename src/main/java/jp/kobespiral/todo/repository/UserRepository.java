package jp.kobespiral.todo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jp.kobespiral.todo.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String>{
}