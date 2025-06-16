package utn.saborcito.El_saborcito_back.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.DetalleFacturaRenderizadoDTO;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.Factura;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FacturaPdfGeneratorService {

    // === PALETA DE COLORES ===
    private static final Color COLOR_ROSA_CLARO = new Color(0xFDEAEA);
    private static final Color COLOR_ROSA_INTENSO = new Color(0xEC2C4E);
    private static final Color COLOR_GRIS_OSCURO = new Color(0x151313);
    private static final Color COLOR_GRIS_CLARO = new Color(0xD2D2D2);
    private static final Color COLOR_FILA_ALTERNADA = new Color(0xFFFAFA);

    // === REGISTRO DE FUENTES ===
    static {
        try {
            FontFactory.register("src/main/resources/fonts/Montserrat-ExtraBold.ttf", "Montserrat-ExtraBold");
            FontFactory.register("src/main/resources/fonts/Montserrat-Bold.ttf", "Montserrat-Bold");
            FontFactory.register("src/main/resources/fonts/Montserrat-SemiBold.ttf", "Montserrat-SemiBold");
            FontFactory.register("src/main/resources/fonts/Montserrat-Medium.ttf", "Montserrat-Medium");
            FontFactory.register("src/main/resources/fonts/Montserrat-Regular.ttf", "Montserrat-Regular");
            FontFactory.register("src/main/resources/fonts/Pacifico-Regular.ttf", "Pacifico");
        } catch (Exception e) {
            System.err.println("Error al registrar fuentes. Se usarán fuentes por defecto.");
        }
    }

    // === DEFINICIÓN DE FUENTES ===
    private static final Font FONT_TITULO_NOTA = FontFactory.getFont("Montserrat-ExtraBold", 18, COLOR_GRIS_OSCURO);
    private static final Font FONT_ETIQUETA_FECHAS = FontFactory.getFont("Montserrat-ExtraBold", 10, COLOR_GRIS_OSCURO); // Subido
                                                                                                                         // a
                                                                                                                         // ExtraBold
                                                                                                                         // y
                                                                                                                         // tamaño
                                                                                                                         // 10
    private static final Font FONT_DIRECCION = FontFactory.getFont("Montserrat-SemiBold", 9, COLOR_GRIS_OSCURO);
    private static final Font FONT_RESTAURANT = FontFactory.getFont("Montserrat-SemiBold", 10, COLOR_ROSA_INTENSO);
    private static final Font FONT_TEXTO_CUERPO_REGULAR = FontFactory.getFont("Montserrat-Regular", 11,
            COLOR_GRIS_OSCURO);
    private static final Font FONT_TEXTO_CUERPO_BOLD = FontFactory.getFont("Montserrat-ExtraBold", 12,
            COLOR_GRIS_OSCURO); // Subido a ExtraBold y tamaño 12
    private static final Font FONT_TEXTO_TABLA_HEADER = FontFactory.getFont("Montserrat-ExtraBold", 12,
            COLOR_GRIS_OSCURO); // Tamaño 12
    private static final Font FONT_FIRMA = FontFactory.getFont("Pacifico", 16, COLOR_ROSA_INTENSO);
    private static final Font FONT_TOTAL_CAJA = FontFactory.getFont("Montserrat-ExtraBold", 14, Color.WHITE);
    private static final Font FONT_GRACIAS_BOTON = FontFactory.getFont("Montserrat-Bold", 12, Color.WHITE);

    public byte[] generarFacturaPdf(Factura factura, java.util.List<DetalleFacturaRenderizadoDTO> items) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 42.5f, 42.5f, 42.5f, 42.5f); // 15mm Margen
        PdfWriter.getInstance(doc, baos);
        doc.open();

        doc.add(createHeader(factura));
        doc.add(createLineaSeparadora(COLOR_ROSA_INTENSO, 1f, 10f));
        doc.add(createDatosCliente(factura));
        doc.add(new Paragraph(" "));
        doc.add(createTablaItems(items));
        doc.add(new Paragraph(" "));
        doc.add(createFirmaYTotales(items));
        doc.add(createLineaSeparadora(COLOR_ROSA_INTENSO, 1f, 20f));
        doc.add(createFooter());

        doc.close();
        return baos.toByteArray();
    }

    // --- SECCIÓN HEADER ---
    private PdfPTable createHeader(Factura factura) throws DocumentException, IOException {
        // Tabla principal del header con 2 columnas: [Logo+Info] y [Datos Nota]
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[] { 1.5f, 1f }); // Dar más espacio a la parte izquierda

        // --- Celda Izquierda: Contenedor del Logo y Texto del Restaurante ---
        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setPadding(0f); // Sin padding para control total

        // Tabla interna para alinear logo y texto horizontalmente
        PdfPTable contentTable = new PdfPTable(2);
        contentTable.setWidthPercentage(100);
        contentTable.setWidths(new float[] { 45f, 105f }); // Logo 45mm, Texto 105mm

        // Columna 1: El logo
        PdfPCell logoContainer = new PdfPCell();
        logoContainer.setBorder(Rectangle.NO_BORDER);
        logoContainer.setPadding(0f);
        logoContainer.setVerticalAlignment(Element.ALIGN_TOP);

        try {
            ClassPathResource logoResource = new ClassPathResource("static/El-Saborcito-Logo.png");
            Image logo = Image.getInstance(logoResource.getURL());
            logo.scaleAbsolute(127.5f, 127.5f); // 45mm = 127.5pt
            logoContainer.addElement(logo);
        } catch (Exception e) {
            logoContainer.addElement(new Paragraph("Logo"));
        }
        contentTable.addCell(logoContainer);

        // Columna 2: El bloque de texto a la derecha del logo
        PdfPCell textContainer = new PdfPCell();
        textContainer.setBorder(Rectangle.NO_BORDER);
        textContainer.setPadding(0f);
        textContainer.setPaddingLeft(14.2f); // 5mm de separación del logo
        textContainer.setVerticalAlignment(Element.ALIGN_TOP);

        // Nombre del restaurante - línea 1
        Phrase nombreLinea1 = new Phrase();
        nombreLinea1.add(new Chunk("El ", FontFactory.getFont("Montserrat-ExtraBold", 16, COLOR_GRIS_OSCURO)));
        Paragraph parrafoNombre1 = new Paragraph(nombreLinea1);
        parrafoNombre1.setSpacingAfter(0f);
        parrafoNombre1.setLeading(16f);
        textContainer.addElement(parrafoNombre1);

        // Nombre del restaurante - línea 2
        Phrase nombreLinea2 = new Phrase();
        nombreLinea2.add(new Chunk("Saborcito", FontFactory.getFont("Montserrat-Bold", 16, COLOR_GRIS_OSCURO)));
        Paragraph parrafoNombre2 = new Paragraph(nombreLinea2);
        parrafoNombre2.setSpacingAfter(2f);
        parrafoNombre2.setLeading(16f);
        textContainer.addElement(parrafoNombre2);

        // "RESTAURANT" en rojo
        Paragraph restaurant = new Paragraph("RESTAURANT", FONT_RESTAURANT);
        restaurant.setSpacingAfter(8f);
        textContainer.addElement(restaurant);

        // Dirección mejor ubicada, alineada a la izquierda y con espacio antes
        Paragraph direccion = new Paragraph("CALLE CUALQUIERA 123,\nCUALQUIER LUGAR, CP: 123450", FONT_DIRECCION);
        direccion.setAlignment(Element.ALIGN_LEFT);
        direccion.setSpacingBefore(8f);
        textContainer.addElement(direccion);

        contentTable.addCell(textContainer);
        leftCell.addElement(contentTable);
        headerTable.addCell(leftCell);

        // --- Celda Derecha: Datos de la Factura ---
        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightCell.setPaddingTop(10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        rightCell.addElement(
                new Paragraph("Factura Número Nº " + String.format("%03d", factura.getId()), FONT_TITULO_NOTA));
        rightCell.addElement(new Paragraph(" "));
        rightCell.addElement(new Paragraph("Fecha de expedición: " + factura.getFechaFacturacion().format(formatter),
                FONT_ETIQUETA_FECHAS));
        rightCell.addElement(new Paragraph("Número de pedido: " + factura.getPedido().getId(), FONT_ETIQUETA_FECHAS));
        rightCell.addElement(
                new Paragraph("Fecha de vencimiento: " + factura.getFechaFacturacion().plusDays(30).format(formatter),
                        FONT_ETIQUETA_FECHAS));
        headerTable.addCell(rightCell);

        return headerTable;
    }

    private PdfPTable createDatosCliente(Factura factura) {
        PdfPTable mainTable = new PdfPTable(3);
        mainTable.setWidthPercentage(100);
        try {
            mainTable.setWidths(new float[] { 70, 20, 70 });
        } catch (DocumentException e) {
        }

        mainTable.addCell(createCeldaDatos("Datos del cliente:",
                factura.getPedido().getCliente().getNombre() + " " + factura.getPedido().getCliente().getApellido(),
                factura.getPedido().getCliente().getEmail(),
                factura.getPedido().getCliente().getTelefono()));

        PdfPCell gapCell = new PdfPCell();
        gapCell.setBorder(Rectangle.NO_BORDER);
        mainTable.addCell(gapCell);

        mainTable.addCell(createCeldaDatos("Enviar a:",
                factura.getPedido().getCliente().getNombre() + " " + factura.getPedido().getCliente().getApellido(),
                "Dirección de envío...",
                "Mendoza, Argentina"));

        return mainTable;
    }

    private PdfPTable createTablaItems(java.util.List<DetalleFacturaRenderizadoDTO> items) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[] { 80, 30, 35, 35 }); // Anchos en mm
        table.setWidthPercentage(100);
        table.setHeaderRows(1);

        table.addCell(createHeaderCell("Descripción"));
        table.addCell(createHeaderCell("Cantidad"));
        table.addCell(createHeaderCell("Precio"));
        table.addCell(createHeaderCell("Total"));

        boolean alternar = false;
        for (DetalleFacturaRenderizadoDTO item : items) {
            Color bgColor = alternar ? COLOR_FILA_ALTERNADA : Color.WHITE;

            table.addCell(createItemCell(item.getDescripcion(), Element.ALIGN_LEFT, bgColor));
            table.addCell(createItemCell(String.valueOf(item.getCantidad()), Element.ALIGN_CENTER, bgColor));
            table.addCell(createItemCell(" ", Element.ALIGN_CENTER, bgColor));
            table.addCell(createItemCell(formatCurrency(item.getSubtotal()), Element.ALIGN_RIGHT, bgColor));

            // Si es una promoción, agregar los artículos incluidos
            if (item.isEsPromocion() && item.getArticulosIncluidos() != null) {
                for (String art : item.getArticulosIncluidos()) {
                    table.addCell(createItemCell("   " + art, Element.ALIGN_LEFT, Color.WHITE)); // Indentado
                    table.addCell(createItemCell(" ", Element.ALIGN_CENTER, Color.WHITE));
                    table.addCell(createItemCell(" ", Element.ALIGN_CENTER, Color.WHITE));
                    table.addCell(createItemCell(" ", Element.ALIGN_CENTER, Color.WHITE));
                }
            }

            alternar = !alternar;
        }
        return table;
    }

    private PdfPTable createFirmaYTotales(java.util.List<DetalleFacturaRenderizadoDTO> items) throws DocumentException {
        PdfPTable mainTable = new PdfPTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setWidths(new float[] { 1, 1 });

        PdfPCell firmaCell = new PdfPCell();
        firmaCell.setBorder(Rectangle.NO_BORDER);
        firmaCell.setPaddingTop(25);
        Chunk firma = new Chunk("ALBERTO CORTEZ", FONT_FIRMA);
        firmaCell.addElement(new Paragraph(firma));
        firmaCell.addElement(new Paragraph("Gerente Empresarial", FONT_ETIQUETA_FECHAS));
        mainTable.addCell(firmaCell);

        PdfPTable totalesTable = new PdfPTable(2);
        totalesTable.setWidthPercentage(100);
        totalesTable.setWidths(new float[] { 2, 1 });
        java.math.BigDecimal subtotal = items.stream()
                .map(i -> java.math.BigDecimal.valueOf(i.getSubtotal()))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal total = subtotal;

        totalesTable.addCell(createTotalCell("Importe Total:", Element.ALIGN_RIGHT, FONT_TEXTO_CUERPO_BOLD));
        totalesTable.addCell(createTotalCell(formatCurrency(subtotal), Element.ALIGN_RIGHT, FONT_TEXTO_CUERPO_BOLD));
        totalesTable.addCell(createTotalCell("Impuestos (0%):", Element.ALIGN_RIGHT, FONT_TEXTO_CUERPO_BOLD));
        totalesTable.addCell(createTotalCell(formatCurrency(0), Element.ALIGN_RIGHT, FONT_TEXTO_CUERPO_BOLD));
        totalesTable.addCell(createTotalCell("Importe a Pagar:", Element.ALIGN_RIGHT, FONT_TEXTO_CUERPO_BOLD));
        totalesTable.addCell(createTotalCell(formatCurrency(total), Element.ALIGN_RIGHT, FONT_TEXTO_CUERPO_BOLD));

        PdfPCell lineaDoble = new PdfPCell();
        lineaDoble.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
        lineaDoble.setBorderColor(COLOR_GRIS_OSCURO);
        lineaDoble.setBorderWidthTop(2f);
        lineaDoble.setBorderWidthBottom(1f);
        lineaDoble.setColspan(2);
        totalesTable.addCell(lineaDoble);

        PdfPCell totalesCell = new PdfPCell(totalesTable);
        totalesCell.setBorder(Rectangle.NO_BORDER);
        mainTable.addCell(totalesCell);

        return mainTable;
    }

    private PdfPTable createFooter() {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell graciasCell = new PdfPCell(new Phrase("¡GRACIAS!", FONT_GRACIAS_BOTON));
        graciasCell.setBackgroundColor(COLOR_ROSA_INTENSO);
        graciasCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        graciasCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        graciasCell.setBorder(Rectangle.NO_BORDER);
        graciasCell.setPadding(8);
        table.addCell(graciasCell);

        PdfPCell condicionesCell = new PdfPCell();
        condicionesCell.setBorder(Rectangle.NO_BORDER);
        condicionesCell.setPaddingLeft(10);
        condicionesCell.addElement(new Paragraph("Condiciones de pago y contacto:", FONT_ETIQUETA_FECHAS));
        condicionesCell.addElement(
                new Paragraph("Monto acreditado mediante anulación total y parcial.", FONT_TEXTO_CUERPO_REGULAR));
        condicionesCell
                .addElement(new Paragraph("elsaborcito2024@gmail.com | +54 9 2616903538", FONT_TEXTO_CUERPO_REGULAR));
        table.addCell(condicionesCell);

        return table;
    }

    // --- HELPERS ---
    private PdfPCell createCeldaDatos(String titulo, String l1, String l2, String l3) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(COLOR_ROSA_INTENSO);
        cell.setBorderWidth(1f);
        cell.setPaddingBottom(8);
        cell.addElement(new Paragraph(titulo, FONT_TEXTO_TABLA_HEADER)); // Header resaltado
        cell.addElement(new Paragraph(l1, FONT_TEXTO_CUERPO_REGULAR));
        cell.addElement(new Paragraph(l2, FONT_TEXTO_CUERPO_REGULAR));
        cell.addElement(new Paragraph(l3, FONT_TEXTO_CUERPO_REGULAR));
        return cell;
    }

    private PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_TEXTO_TABLA_HEADER));
        cell.setBackgroundColor(COLOR_ROSA_CLARO);
        cell.setBorderColor(COLOR_ROSA_INTENSO);
        cell.setBorderWidth(1f);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell createItemCell(String text, int align, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_TEXTO_CUERPO_REGULAR));
        cell.setBackgroundColor(bg);
        cell.setBorderColor(COLOR_ROSA_INTENSO);
        cell.setBorderWidth(1f);
        cell.setPadding(8);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell createTotalCell(String text, int align, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        cell.setPadding(4);
        return cell;
    }

    private PdfPTable createLinea(Color color, float width, float length) {
        PdfPTable line = new PdfPTable(1);
        line.setTotalWidth(length);
        line.setLockedWidth(true);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.TOP);
        cell.setBorderColor(color);
        cell.setBorderWidth(width);
        cell.setFixedHeight(width);
        line.addCell(cell);
        return line;
    }

    private PdfPTable createLineaSeparadora(Color color, float width, float paddingTop) {
        PdfPTable lineTable = new PdfPTable(1);
        lineTable.setWidthPercentage(100);
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorder(Rectangle.TOP);
        lineCell.setBorderColor(color);
        lineCell.setBorderWidth(width);
        lineCell.setFixedHeight(width);
        lineTable.setSpacingBefore(paddingTop);
        lineTable.addCell(lineCell);
        return lineTable;
    }

    private String formatCurrency(Object number) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        currencyFormatter.setMinimumFractionDigits(2);
        return currencyFormatter.format(number);
    }
}
