package com.bmpezzi.salesprocessor.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Entity that holds output data.
 *
 * @author bmpezzi
 */
@ToString
@Builder
@Data
@AllArgsConstructor
public class OutputPojo {
    private static final String TOTAL_COSTUMER_MESSAGE = "Quantidade de clientes no arquivo de entrada: ";
    private static final String TOTAL_SALESMEN_MESSAGE = "Quantidade de vendedores no arquivo de entrada: ";
    private static final String MOST_EXPENSIVE_SALE_ID_MESSAGE = "ID da venda mais cara: ";
    private static final String WORST_SALESMEN_NAME_MESSAGE = "O pior vendedor já encontrado: ";

    private Integer totalOfCustomers;
    private Integer totalOfSalesmen;
    private Long mostExpensiveSaleId;
    private String worstSalesmanName;

    /**
     * @return Total of Customers Message.
     */
    public String getTotalOfCustomersMessage() {
        return TOTAL_COSTUMER_MESSAGE + this.totalOfCustomers;
    }

    /**
     * @return Total of Salesmen Message.
     */
    public String getTotalOfSalesmenMessage() {
        return TOTAL_SALESMEN_MESSAGE + this.totalOfSalesmen;
    }

    /**
     * @return Most Expensive Sale ID Message.
     */
    public String getMostExpensiveSaleIdMessage() {
        return MOST_EXPENSIVE_SALE_ID_MESSAGE + this.mostExpensiveSaleId;
    }

    /**
     * @return Worst Salesman Name Message.
     */
    public String getWorstSalesmanNameMessage() {
        return WORST_SALESMEN_NAME_MESSAGE + this.worstSalesmanName;
    }
}
