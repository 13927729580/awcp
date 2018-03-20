package cn.org.awcp.venson.util;

import com.mortennobel.imagescaling.ResampleOp;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * 裁剪、缩放图片工具类
 *
 * @author CSDN 没有梦想-何必远方
 */
public class ImageUtils {
    /**
     * 日志对象
     */
    private static final Log logger = LogFactory.getLog(ImageUtils.class);

    public static final String JPG = "JPG";

    public static void main(String[] args) {
    }

    /**
     * 缩放图片方法
     *
     * @param input  要缩放的输入图片
     * @param height 目标高度像素
     * @param width  目标宽度像素
     * @return 缩放后的图片
     */
    public final static BufferedImage scale(InputStream input, int width, int height) {
        return scale(input, height, width, true);
    }

    /**
     * 缩放图片方法
     *
     * @param input   要缩放的输入图片
     * @param height  目标高度像素
     * @param width   目标宽度像素
     * @param isRatio 是否等比缩放
     * @return 缩放后的图片
     */
    public final static BufferedImage scale(InputStream input, int width, int height, boolean isRatio) {
        try {
            // 缩放比例
            double ratio;
            BufferedImage bi = ImageIO.read(input);
            if (height <= 0 && width <= 0) {
                return bi;
            }
            int toWidth, toHeight;
            if (isRatio) {
                //计算比例
                double ratioHeight = (double) height / bi.getHeight();
                double ratioWhidth = (double) width / bi.getWidth();
                if (ratioHeight >= ratioWhidth) {
                    ratio = ratioHeight;
                } else {
                    ratio = ratioWhidth;
                }
                toWidth = (int) (bi.getWidth() * ratio);
                toHeight = (int) (bi.getHeight() * ratio);
            } else {
                toWidth = width;
                toHeight = height;
            }
            // 转换
            ResampleOp resampleOp = new ResampleOp(toWidth, toHeight);
            return resampleOp.filter(bi, null);

        } catch (IOException e) {
            logger.debug("ERROR", e);
            return null;
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * 对图片裁剪，并把裁剪新图片保存
     *
     * @param input  读取源图片
     * @param x      剪切起始点x坐标
     * @param y      剪切起始点y坐标
     * @param width  剪切宽度
     * @param height 剪切高度
     * @param suffix 读取图片格式
     */
    public static BufferedImage crop(InputStream input, int x, int y, int width, int height, String suffix) {
        ImageInputStream iis = null;
        try {
            //读取图片文件
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(suffix);
            ImageReader reader = readers.next();
            //获取图片流
            iis = ImageIO.createImageInputStream(input);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            //定义一个矩形
            Rectangle rect = new Rectangle(x, y, width, height);
            //提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            return bi;
        } catch (IOException e) {
            logger.debug("ERROR", e);
            return null;
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(iis);
        }
    }

}