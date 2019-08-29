package com.andrascik.assignment.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionDataRepository extends CrudRepository<ConnectionData, Long> {
}
