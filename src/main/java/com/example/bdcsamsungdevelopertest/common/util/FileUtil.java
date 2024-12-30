package com.example.bdcsamsungdevelopertest.common.util;

import com.example.bdcsamsungdevelopertest.domain.info.OrdersProductInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileUtil {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * JSON 파일을 리스트 객체에서 바이트 배열로
     **/
    public static <T> byte[] generateJsonFile(List<T>data) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(data);
    }

    /**
     * CSV 파일을 리스트 객체에서 바이트 배열로
     **/
    public static byte[] generateCsvFile(List<OrdersProductInfo.OrdersProduct> ordersProducts) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream),
                CSVFormat.DEFAULT.withHeader(
                        "ProductId", "Name", "Email", "OrderItemId", "OrderDate", "Quantity", "OrderStatus"
                ))) {
            for (OrdersProductInfo.OrdersProduct ordersProduct : ordersProducts) {
                for (OrdersProductInfo.OrderItem orderItem : ordersProduct.orders()) {
                    csvPrinter.printRecord(
                            ordersProduct.id(),
                            ordersProduct.name(),
                            ordersProduct.email(),
                            orderItem.id(),
                            orderItem.orderDate(),
                            orderItem.quantity(),
                            orderItem.orderStatus()
                    );
                }
            }
        }
        return outputStream.toByteArray();
    }
}
