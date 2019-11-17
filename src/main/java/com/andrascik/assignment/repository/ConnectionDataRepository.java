package com.andrascik.assignment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionDataRepository extends CrudRepository<ConnectionDataDao, Long> {
}
