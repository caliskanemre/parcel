package com.kn.data;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveCrudRepository<Message, Integer> {

    @Query("SELECT * FROM message WHERE id = :id")
    Flux<Message> findByDataId(int id);

}
