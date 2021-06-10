package com.renegade.videoondemand.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Data
public class User {
    @Id
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;
    @JsonIgnore
    @Version
    private Integer version;

    @Transient
    public User cloneAll(User user) {
        this.password = user.password;
        this.email = user.email;
        return this;
    }

    @Transient
    public User cloneSome(User user) {
        this.password = user.password != null ? user.password : this.password;
        this.email = user.email != null ? user.email : this.email;
        return this;
    }
}
