package com.renegade.videoondemand.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostToken {
    @Id
    private String value;
    private Boolean isUsed;
    private LocalDateTime whenUsed;
    @JsonIgnore
    @Version
    private Integer version;
}
