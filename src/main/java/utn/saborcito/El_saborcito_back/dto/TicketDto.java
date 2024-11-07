package utn.saborcito.El_saborcito_back.dto;

import java.util.List;

public class TicketDto {
    private List<TicketProductoDto> productos;
    private String pago; // Add this field

    // Getters and setters
    public List<TicketProductoDto> getProductos() {
        return productos;
    }

    public void setProductos(List<TicketProductoDto> productos) {
        this.productos = productos;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }
}