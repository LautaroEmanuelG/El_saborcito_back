package utn.saborcito.El_saborcito_back.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utn.saborcito.El_saborcito_back.dto.CloudinaryResponseDto;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Sube una imagen a Cloudinary
     */
    public CloudinaryResponseDto uploadImage(MultipartFile file) throws IOException {
        return uploadFile(file, "image", "images");
    }

    /**
     * Sube un video a Cloudinary
     */
    public CloudinaryResponseDto uploadVideo(MultipartFile file) throws IOException {
        return uploadFile(file, "video", "videos");
    }

    /**
     * Sube un archivo genérico a Cloudinary
     */
    public CloudinaryResponseDto uploadFile(MultipartFile file, String resourceType, String folder) throws IOException {
        validateFile(file);

        try {
            String publicId = generatePublicId(file.getOriginalFilename());

            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "resource_type", resourceType,
                    "folder", folder,
                    "public_id", publicId,
                    "overwrite", true,
                    "use_filename", false,
                    "unique_filename", true);

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            log.info("Archivo subido exitosamente a Cloudinary: {}", uploadResult.get("public_id"));

            return mapToCloudinaryResponse(uploadResult);

        } catch (Exception e) {
            log.error("Error al subir archivo a Cloudinary: {}", e.getMessage(), e);
            throw new IOException("Error al subir archivo a Cloudinary: " + e.getMessage());
        }
    }

    /**
     * Elimina una imagen de Cloudinary
     */
    public Boolean deleteImage(String publicId) {
        return deleteFile(publicId, "image");
    }

    /**
     * Elimina un archivo de Cloudinary
     */
    public Boolean deleteFile(String publicId, String resourceType) {
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId,
                    ObjectUtils.asMap("resource_type", resourceType));

            String resultStatus = (String) result.get("result");
            boolean success = "ok".equals(resultStatus);

            if (success) {
                log.info("Archivo eliminado exitosamente de Cloudinary: {}", publicId);
            } else {
                log.warn("No se pudo eliminar el archivo de Cloudinary: {}, resultado: {}", publicId, resultStatus);
            }

            return success;

        } catch (Exception e) {
            log.error("Error al eliminar archivo de Cloudinary: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Genera una URL de transformación para una imagen
     */
    public String generateTransformationUrl(String publicId, Integer width, Integer height, String crop) {
        try {
            Integer w = width != null ? width : 300;
            Integer h = height != null ? height : 300;
            String c = crop != null ? crop : "fill";

            // Usar la clase Transformation de Cloudinary con parámetros específicos
            Transformation<?> transformation = new Transformation<>()
                    .width(w)
                    .height(h)
                    .crop(c)
                    .quality("auto")
                    .fetchFormat("auto");

            return cloudinary.url()
                    .transformation(transformation)
                    .generate(publicId);
        } catch (Exception e) {
            log.error("Error al generar URL de transformación: {}", e.getMessage(), e);
            return null;
        }
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("El archivo no puede estar vacío");
        }

        if (file.getSize() > 10 * 1024 * 1024) { // 10MB límite
            throw new IOException("El archivo es demasiado grande. Máximo 10MB permitido");
        }
    }

    private String generatePublicId(String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        if (originalFilename != null && !originalFilename.isEmpty()) {
            String name = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
            return String.format("%s_%s_%s", name.replaceAll("[^a-zA-Z0-9]", "_"), timestamp, uuid);
        }

        return String.format("file_%s_%s", timestamp, uuid);
    }

    private CloudinaryResponseDto mapToCloudinaryResponse(Map<?, ?> uploadResult) {
        return CloudinaryResponseDto.builder()
                .publicId((String) uploadResult.get("public_id"))
                .url((String) uploadResult.get("url"))
                .secureUrl((String) uploadResult.get("secure_url"))
                .format((String) uploadResult.get("format"))
                .width((Integer) uploadResult.get("width"))
                .height((Integer) uploadResult.get("height"))
                .bytes(Long.valueOf(uploadResult.get("bytes").toString()))
                .resourceType((String) uploadResult.get("resource_type"))
                .createdAt((String) uploadResult.get("created_at"))
                .signature((String) uploadResult.get("signature"))
                .etag((String) uploadResult.get("etag"))
                .build();
    }
}
