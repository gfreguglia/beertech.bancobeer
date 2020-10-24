package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Outbox {
    @Id
    @Column(name = "UUID", columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(name = "AGGREGATE_ID")
    private Long aggregateId;

    @Column(name = "EVENT_TYPE")
    private Class eventType;

    @Lob
    @Column(name = "PAYLOAD")
    private String payload;

    @Column(name = "CREATED_ON")
    private Date createdOn;
}
