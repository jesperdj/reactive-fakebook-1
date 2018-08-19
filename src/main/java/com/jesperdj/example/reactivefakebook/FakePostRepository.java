package com.jesperdj.example.reactivefakebook;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FakePostRepository extends ReactiveMongoRepository<FakePost, String> {

    Flux<FakePost> findAllByOrderByTimestampDesc();
}
