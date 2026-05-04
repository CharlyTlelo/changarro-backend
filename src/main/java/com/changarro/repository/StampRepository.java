package com.changarro.repository;

import com.changarro.model.Stamp;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StampRepository extends MongoRepository<Stamp, String> {
}
