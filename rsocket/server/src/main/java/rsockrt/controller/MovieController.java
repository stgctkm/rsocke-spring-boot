package rsockrt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rsockrt.model.MovieScene;
import rsockrt.model.TicketRequest;
import rsockrt.service.MovieService;

import java.time.Duration;
import java.util.Collections;
import java.util.function.Function;

@Controller
public class MovieController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    MovieService movieService;

    // fire-and-forget
    @MessageMapping("ticket.cancel")
    public void cancelTicket(Mono<TicketRequest> request) {
        request.doOnNext(TicketRequest::cancel)
                .doOnNext(t -> logger.info("cancelTicket :: " + t.requestId() + " : " + t.status()))
                .subscribe();
    }

    // request-response
//    @MessageMapping("ticket.purchase")
//    public Mono<TicketRequest> purchaseTicket(Mono<TicketRequest> request) {
//        return request
//                .doOnNext(TicketRequest::issue)
//                .doOnNext(t -> logger.info("purchaseTicket :: " + t.requestId() + " : " + t.status()));
//    }

    @MessageMapping("ticket.purchase")
    public Mono<TicketRequest> purchaseTicket(TicketRequest request) {
        return Mono.just(request)
                .doOnNext(TicketRequest::issue)
                .doOnNext(t -> logger.info("purchaseTicket :: " + t.requestId() + " : " + t.status()));
    }

    // request-stream
    @MessageMapping("movie.stream")
    public Flux<MovieScene> playMovie(Mono<TicketRequest> request) {
        return request
//                .map(t -> t.isIssued() ? this.movieService.getScenes() : Collections.emptyList())
                .map(t -> this.movieService.getScenes())
                .flatMapIterable(Function.identity())
                .cast(MovieScene.class)
                .doOnNext(movieScene -> logger.info("streaming : " + movieScene.sceneDescription()))
                .delayElements(Duration.ofSeconds(1));

    }

    // channel
    @MessageMapping("tv.movie")
    public Flux<MovieScene> playMovie(Flux<Integer> sceneIndex) {
        return sceneIndex
                .map(index -> index - 1)
                .map(this.movieService::getScene)
                .delayElements(Duration.ofSeconds(1));
    }

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
}
