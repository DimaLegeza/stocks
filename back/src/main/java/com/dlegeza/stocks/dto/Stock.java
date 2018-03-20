package com.dlegeza.stocks.dto;

import com.dlegeza.stocks.serializer.DateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
@ApiModel(
    description = "Representation of stock entity"
)
public class Stock {
    @Id
    @GeneratedValue
    @ApiModelProperty(hidden = true)
    private Long id;

    @NonNull
    @ApiModelProperty(required = true)
    private String name;

    @Version
    /**
     * Version for optimistic locking.
     * Exception will be thrown in case stock update is initiated with lockVersion of outdated value
     */
    private Long lockVersion;

    @NonNull
    @Column(precision = 19, scale = 10)
    @ApiModelProperty(required = true)
    private BigDecimal currentPrice;

    @UpdateTimestamp
    @JsonSerialize(using = DateTimeSerializer.class)
    private Timestamp timestamp;
}
