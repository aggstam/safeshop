package com.safeshop.services;

import com.safeshop.models.Product;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class FilesService {

    private static final Logger logger = Logger.getLogger(FilesService.class.getName());
    private static final String PRODUCTS_STATIC_FOLDER_PATH = "WEB-INF/static/images/products/";

    public static void setProductImageFile(ServletContext context, Product product, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty() && file.getOriginalFilename() != null && !StringUtils.isBlank(file.getOriginalFilename())) {
            // Product static image update.
            String filteredFileName = filterAttachmentName(file.getOriginalFilename());
            product.setImage(filteredFileName);
            String productFolderPath = context.getRealPath(PRODUCTS_STATIC_FOLDER_PATH) + product.getId();
            File productFolder = new File(productFolderPath);
            if (productFolder.exists()) {
                FileUtils.forceDelete(productFolder);
            }
            Files.createDirectories(Paths.get(productFolderPath));
            productFolderPath += File.separator;
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(productFolderPath + filteredFileName));
        }
    }

    public static void deleteProductImageFile(ServletContext context, Integer productId) throws IOException {
        File file = new File(context.getRealPath(PRODUCTS_STATIC_FOLDER_PATH) + productId);
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }
    }

    public static Boolean checkFileMimeType(MultipartFile multipartFile) {
        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String mimeType = new Tika().detect(multipartFile.getInputStream());
                return mimeType.equals("image/png") || mimeType.equals("image/jpg") || mimeType.equals("image/jpeg");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Could not extract File Mime Type. Exception: " + e.getMessage());
            return false;
        }
    }

    public static String filterAttachmentName(String originalName) {
        int lastDotIndex = originalName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return originalName.substring(0, lastDotIndex).replaceAll("[\\.|\\|\\\\|/|//]", "") + originalName.substring(lastDotIndex);
        }
        return originalName;
    }

}
