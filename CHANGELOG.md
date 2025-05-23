# 📋 CHANGELOG

Este archivo documenta los cambios importantes en el proyecto El Saborcito.

## 🔄 Cambios

---

### 🛠️ Mayo 2025: Simplificación de la cancelación de pedidos

#### ✅ Cambios realizados

- **Eliminado** la entidad `NotaCredito` y todas sus clases relacionadas:
  - `NotaCreditoController.java`
  - `NotaCreditoService.java`
  - `NotaCreditoRepository.java`
  - `NotaCredito.java`
- **Añadido** la funcionalidad de cancelación directamente en `PedidoService`:
  - Nuevo método `cancelarPedido` que cambia el estado del pedido a `CANCELADO`
  - Implementado notificación al cliente cuando se cancela un pedido
- **Creado** un nuevo endpoint en `PedidoController`:
  - `PUT /api/pedidos/{id}/cancelar` que permite cancelar pedidos con una razón opcional

#### 🔍 Motivación

- Simplificar el proceso de cancelación eliminando la necesidad de crear una nota de crédito
- Mejorar la experiencia de usuario al reducir los pasos necesarios para cancelar un pedido
- Mantener la integridad referencial al eliminar entidades innecesarias

#### 🧪 Pruebas

- Verificado que la compilación del proyecto es exitosa
- Comprobado que se puede cancelar un pedido a través del nuevo endpoint

#### 📝 Notas importantes

- El cambio no incluye la restauración de stock cuando se cancela un pedido (funcionalidad que estaba en `NotaCreditoService`)
- La notificación al cliente es simulada y debería implementarse con un servicio real en el futuro
