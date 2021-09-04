package rsockrt.model;

import java.util.UUID;

public class TicketRequest {
    String requestId;
    TicketStatus status;

    public TicketRequest() {
    }

    public TicketRequest(String requestId) {
        this.requestId = requestId;
    }

    public static TicketRequest issuedTicket() {
        TicketRequest ticketRequest = new TicketRequest(UUID.randomUUID().toString());
        ticketRequest.issue();
        System.out.println("******");
        System.out.println(ticketRequest.requestId);
        System.out.println("******");
        return ticketRequest;
    }

    public TicketStatus status() {
        return status;
    }

    public void cancel() {
        this.status = TicketStatus.TICKET_CANCELLED;
    }

    public String requestId() {
        return requestId;
    }

    public void issue() {
        this.status = TicketStatus.TICKET_ISSUED;
    }

    public boolean isIssued() {
        return this.status == TicketStatus.TICKET_ISSUED;
    }

    // TODO なくしたい
    public String getRequestId() {
        return requestId;
    }

    // TODO なくしたい
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

//    // TODO なくしたい
//    public TicketStatus getStatus() {
//        return status;
//    }
//
//    // TODO なくしたい
//    public void setStatus(TicketStatus status) {
//        this.status = status;
//    }
}
