/**
 * Created by moyong on 2017/10/11.
 */

import net.sourceforge.tess4j.util.ImageHelper;
import ocr.Ocr;
import ocr.PictureManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 如果抛Invalid memory access 无效的内存访问异常，导致这个异常的原因是tessdata语言包的位置没有找到
 */
public class TesseractExample {

    private static final Logger logger = LoggerFactory.getLogger(TesseractExample.class);

    public static void main(String[] args) {
        Ocr.checkChodeOcr("/tmp/checkcode.png");

        String path = "app-consuming-rest/src/test/resources/test-data/cp2.jpg";
        PictureManage pictureManage = new PictureManage(path); //对图片进行处理
        pictureManage.imshow();
//        logger.debug("识别结果：{}", getCaptureText("app-consuming-rest/src/test/resources/test-data/cp1.jpg"));
        System.out.println("识别结果："+ Ocr.getCaptureText("/tmp/xintu.jpg"));
        System.out.println("识别结果："+ Ocr.getCaptureText(path));
    }

    public static BufferedImage change(File file) {

        // 读取图片字节数组
        BufferedImage textImage = null;
        try {
            InputStream in = new FileInputStream(file);
            BufferedImage image = ImageIO.read(in);
            textImage = ImageHelper.convertImageToGrayscale(ImageHelper.getSubImage(image, 0, 0, image.getWidth(), image.getHeight()));  //对图片进行处理
            textImage = ImageHelper.getScaledInstance(image, image.getWidth() * 5, image.getHeight() * 5);  //将图片扩大5倍
        } catch (IOException e) {
            e.printStackTrace();
        }

        return textImage;
    }
}
