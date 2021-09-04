package rsockrt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import rsockrt.RSocketMovieClient;
import rsockrt.model.TicketRequest;

@RestController
@RequestMapping("movie")
public class MovieClientController {

    RSocketMovieClient client;

    @GetMapping("cancel")
    public void cancel() {
        client.ticketCancel();
    }

    @GetMapping("purchase")
    public void purchase() {
        client.ticketPurchase();
    }

    @GetMapping("/mono/purchase")
    public Mono<TicketRequest> purchaseTicket() {
        return client.purchaseTicket();
    }

    @GetMapping("watch")
    public void watch() {
        client.playMovie();
    }

    public MovieClientController(RSocketMovieClient client) {
        this.client = client;
    }
}
