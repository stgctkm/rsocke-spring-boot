package rsockrt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rsockrt.model.MovieScene;
import rsockrt.model.TicketRequest;

import java.util.UUID;

@Component
public class RSocketMovieClient {

    Mono<RSocketRequester> requester;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RSocketMovieClient(Mono<RSocketRequester> requester) {
        this.requester = requester;
    }

    public void ticketCancel() {
        Mono<Void> mono = this.requester
                .map(r -> r.route("ticket.cancel").data(new TicketRequest(UUID.randomUUID().toString())))
                .flatMap(RSocketRequester.RetrieveSpec::send);

        mono.subscribe();
    }

    public void ticketPurchase() {
        Mono<TicketRequest> mono = this.requester
                .map(r -> r.route("ticket.purchase").data(TicketRequest.issuedTicket()))
                .flatMap(r -> r.retrieveMono(TicketRequest.class))
                .doOnNext(r ->  logger.info(r.requestId() + ":" + r.status()));

        mono.subscribe(x -> logger.info("*** " + x.requestId() + ", " + x.status()));
//        mono.subscribe();
    }

    public Mono<TicketRequest> purchaseTicket() {
        Mono<TicketRequest> mono = this.requester
                .map(r -> r.route("ticket.purchase").data(TicketRequest.issuedTicket()))
                .flatMap(r -> r.retrieveMono(TicketRequest.class))
                .doOnNext(r ->  logger.info(r.requestId() + ":" + r.status()));

        return mono;
//        mono.subscribe(x -> logger.info("*** " + x.requestId() + ", " + x.status()));
//        mono.subscribe();
    }

    public void playMovie() {
        Mono<TicketRequest> mono = this.requester
                .map(r -> r.route("ticket.purchase").data(new TicketRequest(UUID.randomUUID().toString())))
                .flatMap(retrieveSpec -> retrieveSpec.retrieveMono(TicketRequest.class));

        Flux<MovieScene> flux = this.requester
                .zipWith(mono)
                .map(tuple -> tuple.getT1().route("movie.stream").data(tuple.getT2()))
                .flatMapMany(retrieveSpec -> retrieveSpec.retrieveFlux(MovieScene.class))
                .doOnNext(movieScene -> logger.info("Playing : " + movieScene.sceneDescription()));

        flux.subscribe();

    }

}
