package com.jesperdj.example.reactivefakebook;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReactiveFakebookApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetAll() {
        webTestClient.get().uri("/posts")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_UTF8)
                .expectBodyList(FakePost.class);
    }

    @Test
    public void testCreate() {
        FakePost fakePost = new FakePost();
        fakePost.setName("Testing");
        fakePost.setContent("This is a test, please ignore me.");

        webTestClient.post().uri("/posts")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(fakePost), FakePost.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON_UTF8)
                .expectBody(FakePost.class)
                .consumeWith(body -> {
                    FakePost responseBody = body.getResponseBody();
                    assertNotNull(responseBody);
                    assertNotNull(responseBody.getId());
                });
    }

    @Test
    public void testUpdateNonExisting() {
        FakePost fakePost = new FakePost();
        fakePost.setName("Testing");
        fakePost.setContent("This is a test, please ignore me.");

        webTestClient.put().uri("/posts/TEST")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(fakePost), FakePost.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}
