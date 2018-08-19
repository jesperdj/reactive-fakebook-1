package com.jesperdj.example.reactivefakebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/posts")
public class FakePostController {

    private final FakePostRepository repository;

    @Autowired
    public FakePostController(FakePostRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Flux<FakePost> getAll() {
        return repository.findAllByOrderByTimestampDesc();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<FakePost>> getById(@PathVariable("id") String id) {
        return repository.findById(id)
                .map(post -> ResponseEntity.ok(post))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<FakePost>> create(@RequestBody FakePost post) {
        post.setTimestamp(LocalDateTime.now());
        return repository.save(post)
                .map(createdPost -> ResponseEntity.created(uriForPost(post.getId())).body(createdPost));
    }

    private URI uriForPost(String id) {
        return UriComponentsBuilder.fromPath("/posts/{id}").buildAndExpand(id).toUri();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<FakePost>> update(@PathVariable("id") String id, @RequestBody FakePost post) {
        return repository.findById(id)
                .flatMap(existingPost -> {
                    existingPost.setTimestamp(LocalDateTime.now());
                    existingPost.setContent(post.getContent());
                    return repository.save(existingPost);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return repository.findById(id)
                .flatMap(post -> repository.delete(post)
                        .then(Mono.<ResponseEntity<Void>>just(ResponseEntity.ok().build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
