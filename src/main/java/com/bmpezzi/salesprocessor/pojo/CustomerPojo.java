package com.bmpezzi.salesprocessor.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Entity that holds customer data.
 *
 * @author bmpezzi
 */
@ToString
@Builder
@Data
@AllArgsConstructor
public class CustomerPojo {
    private Long cnpj;
    private String name;
    private String businessArea;

    /**
     * Creates Customer parser.
     * @param parsedEntityData Raw data captured from input file.
     * @return An instance of this parser.
     */
    public static CustomerPojo createFrom(String[] parsedEntityData) {
        if(null != parsedEntityData) {
            return CustomerPojo.builder()
                    .cnpj(Long.parseLong(parsedEntityData[1]))
                    .name(parsedEntityData[2])
                    .businessArea(parsedEntityData[3])
                    .build();
        }
        return null;
    }
}
