/**
 * Created by moyong on 2017/10/11.
 */

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.LoadLibs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 如果抛Invalid memory access 无效的内存访问异常，导致这个异常的原因是tessdata语言包的位置没有找到
 */
public class TesseractExample {

    public static void main(String[] args) {
        //图片二值化，水印也可以被去掉。
//        BufferedImage grayImage = ImageHelper.convertImageToBinary(ImageIO.read(imageFile));
//        ImageIO.write(grayImage, "jpg", new File("data/", "test2.jpg"));


//        File imageFile = new File("/Users/moyong/project/env-myopensource/1-spring/12-spring/fast-api/d-api/app-consuming-rest/src/test/resources/test-data/eurotext.tif");
        //File imageFile = new File("/Users/moyong/project/env-myopensource/1-spring/12-spring/fast-api/d-api/app-consuming-rest/src/test/resources/test-data/eurotext.png");
//        File imageFile = new File("/Users/moyong/project/env-myopensource/1-spring/12-spring/fast-api/d-api/app-consuming-rest/src/test/resources/test-data/eurotext_deskew.png");
//        File imageFile = new File("/Users/moyong/project/env-myopensource/1-spring/12-spring/fast-api/d-api/app-consuming-rest/src/test/resources/test-data/eurotext_unlv.png");
//        File imageFile = new File("/Users/moyong/project/env-myopensource/1-spring/12-spring/fast-api/d-api/app-consuming-rest/src/test/resources/test-data/eurotext.bmp");
        File imageFile = new File("/Users/moyong/project/env-myopensource/1-spring/12-spring/fast-api/d-api/app-consuming-rest/src/test/resources/test-data/eurotext.pdf");
//        File imageFile = new File("/Users/moyong/project/env-myopensource/1-spring/12-spring/fast-api/d-api/app-consuming-rest/src/test/resources/test-data/multipage-pdf.pdf");
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        //这样就能使用classpath目录下的训练库了
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setLanguage("eng");//英文库识别数字比较准确
//        instance.setLanguage("chi_sim");//中文库识别中文
        //Set the tessdata path
        instance.setDatapath(tessDataFolder.getAbsolutePath());

        //提取金额
//        Rectangle rectangle = new Rectangle(366, y_Axis, 92, h);
//        String decrease = instance.doOCR(bufferedImage,rectangle)
//                .replace(" ",".").replace(",","");


        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
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
