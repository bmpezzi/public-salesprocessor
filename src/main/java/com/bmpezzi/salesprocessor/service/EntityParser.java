package com.bmpezzi.salesprocessor.service;

import com.bmpezzi.salesprocessor.pojo.CustomerPojo;
import com.bmpezzi.salesprocessor.pojo.OutputPojo;
import com.bmpezzi.salesprocessor.pojo.SalePojo;
import com.bmpezzi.salesprocessor.pojo.SalesmanPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class that proccess the entities
 *
 * @author bmpezzi
 */
@Service
public class EntityParser {
    private Logger logger = LoggerFactory.getLogger("EntityParser");

    private static final String DELIMITER = "\\u00E7";
    private static final String SALESPERSON_DATA = "001";
    private static final String CUSTOMER_DATA = "002";
    private static final String SALES_DATA = "003";

    private Set<CustomerPojo> customers;
    private Set<SalesmanPojo> salesmen;
    private Set<SalePojo> sales;

    /**
     * Instantiates the collections
     */
    public void start() {
        customers = new HashSet<>();
        salesmen = new HashSet<>();
        sales = new TreeSet<>();
    }

    /**
     * Parses parser raw data.
     * @param entityData Entity raw data
     */
    public void parse(String entityData) {
        String[] parsedEntityData = entityData.split(DELIMITER);
        if (parsedEntityData.length == 4) {
            parseEntityByType(parsedEntityData);
            logger.info("Line parsed: {}", entityData);
        } else {
            logger.error("Invalid parser data: {}", entityData);
        }
    }

    /**
     * Builds output data for a given set of parse operations.
     * @return Output raw data.
     */
    public OutputPojo buildOutput() {
        OutputPojo output = null;
        if(!customers.isEmpty())
            output = OutputPojo.builder()
                    .totalOfCustomers(customers.size())
                    .totalOfSalesmen(salesmen.size())
                    .mostExpensiveSaleId(getMostExpensiveSaleId())
                    .worstSalesmanName(getWorstSalesmanName())
                    .build();
        clearData();
        return output;
    }

    /**
     * Detects parser type data and proceds with proper parser.
     * @param parsedEntityData Parsed parser data.
     */
    private void parseEntityByType(String[] parsedEntityData) {
        if (SALESPERSON_DATA.equals(parsedEntityData[0])) {
            SalesmanPojo salesman = SalesmanPojo.createFrom(parsedEntityData);
            salesmen.add(salesman);
        } else if (CUSTOMER_DATA.equals(parsedEntityData[0])) {
            CustomerPojo customer = CustomerPojo.createFrom(parsedEntityData);
            customers.add(customer);
        } else if (SALES_DATA.equals(parsedEntityData[0])) {
            SalePojo sale = SalePojo.createFrom(parsedEntityData, getSalesmanByName(parsedEntityData[3]));
            sales.add(sale);
        }
    }

    /**
     * Gets a salesman by his name.
     * @param name Salesman's name.
     * @return The proper salesmen or null if none was found.
     */
    private SalesmanPojo getSalesmanByName(String name) {
        return salesmen.stream().filter(salesman -> salesman.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * @return The most expensive sale ID.
     */
    private Long getMostExpensiveSaleId() {
        Long mostExpensiveSaleId = null;
        if (!sales.isEmpty()) {
            SalePojo mostExpensiveSale = ((TreeSet<SalePojo>)sales).first();
            mostExpensiveSaleId = mostExpensiveSale.getId();
        }
        return mostExpensiveSaleId;
    }

    /**
     * @return The worst salesman name.
     */
    private String getWorstSalesmanName() {
        String worstSalesmanName = null;
        if (!salesmen.isEmpty()) {
            TreeSet<SalesmanPojo> sortedSalesmen = new TreeSet<>(salesmen);
            SalesmanPojo worstSalesman = sortedSalesmen.first();
            worstSalesmanName = worstSalesman.getName();
            sortedSalesmen.clear();
        }
        return worstSalesmanName;
    }

    /**
     * Clear all parser set.
     */
    private void clearData() {
        customers.clear();
        salesmen.clear();
        sales.clear();
    }
}