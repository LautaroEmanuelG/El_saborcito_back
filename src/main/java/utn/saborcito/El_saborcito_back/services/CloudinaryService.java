package utn.saborcito.El_saborcito_back.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // Subir imagen a Cloudinary y devolver la URL segura
    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("secure_url");
    }

    // Eliminar imagen de Cloudinary usando el public_id
    public String deleteImage(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return (String) result.get("result"); // "ok" si fue exitosa
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la imagen de Cloudinary: " + e.getMessage());
        }
    }

    // (Opcional) Extraer el public_id desde la URL de Cloudinary
    public String extractPublicIdFromUrl(String url) {
        // Ejemplo de URL: https://res.cloudinary.com/tu_cloud/image/upload/v1234567890/carpeta/archivo.jpg
        // Resultado esperado: carpeta/archivo (sin extensión)
        if (url == null) return null;
        try {
            String[] parts = url.split("/");
            String fileWithExt = parts[parts.length - 1];
            String fileName = fileWithExt.contains(".") ? fileWithExt.substring(0, fileWithExt.lastIndexOf('.')) : fileWithExt;
            // Si tienes carpetas, las puedes reconstruir así:
            StringBuilder publicId = new StringBuilder();
            // Busca desde "upload/" hasta el final menos la extensión
            boolean foundUpload = false;
            for (int i = 0; i < parts.length - 1; i++) {
                if (foundUpload) {
                    publicId.append(parts[i]).append("/");
                }
                if (parts[i].equals("upload")) {
                    foundUpload = true;
                }
            }
            publicId.append(fileName);
            return publicId.toString();
        } catch (Exception e) {
            return null;
        }
    }
}