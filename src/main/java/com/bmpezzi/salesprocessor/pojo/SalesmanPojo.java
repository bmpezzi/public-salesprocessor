package com.bmpezzi.salesprocessor.pojo;

import lombok.*;

import java.math.BigDecimal;

/**
 * Entity that holds salesman data.
 *
 * @author bmpezzi
 */
@ToString
@Builder
@Data
@AllArgsConstructor
public class SalesmanPojo implements Comparable<SalesmanPojo> {
    private Long cpf;
    private String name;
    private BigDecimal salary;
    private SalePojo sale;

    /**
     * Creates Salesman parser.
     * @param parsedEntityData Raw data captured from input file.
     * @return An instance of this parser.
     */
    public static SalesmanPojo createFrom(String[] parsedEntityData){
        if(null != parsedEntityData) {
            return SalesmanPojo.builder()
                    .cpf(Long.parseLong(parsedEntityData[1]))
                    .name(parsedEntityData[2])
                    .salary(new BigDecimal(parsedEntityData[3]))
                    .build();
        }
        return null;
    }

    /**
     * Compare this parser to another based on sale's COST.
     * @param salesman Another instance of this parser.
     * @return -1, 0, or 1 as it is numerically less than, equal to, or greater than each other.
     */
    @Override
    public int compareTo(SalesmanPojo salesman) {
        return this.getSale().getCost().compareTo(salesman.getSale().getCost());
    }
}
