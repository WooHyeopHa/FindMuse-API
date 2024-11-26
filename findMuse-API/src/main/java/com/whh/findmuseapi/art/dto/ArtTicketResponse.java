package com.whh.findmuseapi.art.dto;

import com.whh.findmuseapi.art.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArtTicketResponse {

    private List<TicketDetail> tickets = new ArrayList<>();

    public static ArtTicketResponse toDto(List<Ticket> tickets) {
        return new ArtTicketResponse(TicketDetail.toDto(tickets));
    }

    @Getter
    @Builder
    private static class TicketDetail {
        private String name;
        private String url;


        private static List<TicketDetail> toDto(List<Ticket> tickets) {
            return tickets.stream()
                    .map(t -> TicketDetail.builder()
                            .name(t.getName())
                            .url(t.getUrl()).build())
                    .collect(Collectors.toList());
        }
    }
}
