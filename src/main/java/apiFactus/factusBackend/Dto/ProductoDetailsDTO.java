package apiFactus.factusBackend.Dto;

import apiFactus.factusBackend.Domain.enums.TipoEntrega;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ProductoDetailsDTO {
    private Integer id;
    @JsonProperty("code_reference")
    private String codeReference;
    private String name;
    private int quantity;
    @JsonProperty("discount_rate")
    private double discountRate;
    private double price;
    @JsonProperty("tax_rate")
    private double taxRate;
    @JsonProperty("unit_measure_id")
    private int unitMeasureId;
    private String coverPath;
    private String filePath;
    private int standardCodeId;
    private int isExcluded;
    private int tributeId;
    private List<WithholdingTaxDTO> withholdingTaxes;
    private LocalDateTime updatedAt;
    private String descripcion;
    private String categoryName;
    private List<SucursalStockDTO> sucursalesStock;
    private List<TipoEntrega> tiposEntrega;

    private Double costoDespacho;
}
