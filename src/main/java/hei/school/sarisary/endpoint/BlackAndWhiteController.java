package hei.school.sarisary.endpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class BlackAndWhiteController {

    @PutMapping("/black-and-white/{id}")
    public ResponseEntity<Void> convertToBlackAndWhite(@PathVariable String id, @RequestBody byte[] imageBytes) {
        if (!isPNG(imageBytes)) {
            return ResponseEntity.ok().build();
        }

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        BufferedImage blackAndWhiteImage = convertToBlackAndWhite(bufferedImage);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(blackAndWhiteImage, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        byte[] blackAndWhiteImageBytes = outputStream.toByteArray();

        return ResponseEntity.ok().build();
    }


    private boolean isPNG(byte[] imageBytes) {
        try {
            ImageIO.read(new ByteArrayInputStream(imageBytes));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private BufferedImage convertToBlackAndWhite(BufferedImage bufferedImage) {
        BufferedImage blackAndWhiteImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = blackAndWhiteImage.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, null);
        return blackAndWhiteImage;
    }

    @GetMapping("/black-and-white/{id}")
    public ResponseEntity<TransformationResult> getTransformationResult(@PathVariable String id) {
        String originalUrl = "https://original.url";
        String transformedUrl = "https://transformed.url";
        TransformationResult result = new TransformationResult(originalUrl, transformedUrl);

        return ResponseEntity.ok(result);
    }

    static class TransformationResult {
        private String originalUrl;
        private String transformedUrl;

        public TransformationResult(String originalUrl, String transformedUrl) {
            this.originalUrl = originalUrl;
            this.transformedUrl = transformedUrl;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public String getTransformedUrl() {
            return transformedUrl;
        }
    }
}
