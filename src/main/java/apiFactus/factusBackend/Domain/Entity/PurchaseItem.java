package apiFactus.factusBackend.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "purchase_items")
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float price;
    @Column(name = "quantity")
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "producto_id",referencedColumnName = "id"
    ,foreignKey = @ForeignKey(name = "FK_purchase_item_producto"))
    private productos_Tienda producto;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "id"
    , foreignKey = @ForeignKey(name = "FK_purhcase_item_purchase"))
    public Purchase purchase;

}
