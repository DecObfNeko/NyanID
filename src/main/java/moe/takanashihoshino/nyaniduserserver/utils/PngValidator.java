package moe.takanashihoshino.nyaniduserserver.utils;


import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.util.Iterator;

public class PngValidator {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    private static final int MAX_IMAGE_DIMENSION = 5000;
    private static final long MAX_MEMORY_USAGE = 10 * 1024 * 1024;

    public static boolean isValidPng(MultipartFile file) {
        // 1. 检查文件大小
        if (file.isEmpty() || file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        try {
            byte[] fileBytes = file.getBytes();

            // 3. 检查PNG签名（魔数）
            if (!hasValidPngSignature(fileBytes)) {
                return false;
            }

            // 4. 使用ImageReader安全获取尺寸
            Dimension dimension = getImageDimensionsSafely(fileBytes);
            if (dimension == null) {
                return false;
            }

            // 5. 检查尺寸限制
            if (dimension.width > MAX_IMAGE_DIMENSION ||
                    dimension.height > MAX_IMAGE_DIMENSION) {
                return false;
            }

            // 6. 检查内存使用量
            long estimatedMemory = (long) dimension.width * dimension.height * 4; // RGBA格式
            if (estimatedMemory > MAX_MEMORY_USAGE) {
                return false;
            }

            // 7. 检查XSS风险（扫描元数据）
            if (containsXssPayload(fileBytes)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 验证PNG文件头（魔数）
    private static boolean hasValidPngSignature(byte[] fileBytes) {
        if (fileBytes.length < 8) return false;

        return (fileBytes[0] & 0xFF) == 0x89 &&
                fileBytes[1] == 0x50 &&
                fileBytes[2] == 0x4E &&
                fileBytes[3] == 0x47 &&
                fileBytes[4] == 0x0D &&
                fileBytes[5] == 0x0A &&
                fileBytes[6] == 0x1A &&
                fileBytes[7] == 0x0A;
    }

    // 安全获取图像尺寸（使用ImageReader，不加载完整图像）
    private static Dimension getImageDimensionsSafely(byte[] fileBytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
             ImageInputStream iis = ImageIO.createImageInputStream(bis)) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                return null;
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(iis);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                return new Dimension(width, height);
            } finally {
                reader.dispose();
            }
        } catch (Exception e) {
            return null;
        }
    }

    // 检查XSS风险（扫描文本块）
    private static boolean containsXssPayload(byte[] fileBytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
             ImageInputStream iis = ImageIO.createImageInputStream(bis)) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                return false;
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(iis);
                IIOMetadata metadata = reader.getImageMetadata(0);

                if (metadata != null) {
                    return scanMetadataForXss(metadata);
                }
                return false;
            } finally {
                reader.dispose();
            }
        } catch (Exception e) {
            return false;
        }
    }

    // 扫描图像元数据中的XSS风险
    private static boolean scanMetadataForXss(IIOMetadata metadata) {
        // 获取所有支持的元数据格式
        String[] formats = metadata.getMetadataFormatNames();
        for (String format : formats) {
            Node root = metadata.getAsTree(format);
            if (scanNodeTreeForXss(root)) {
                return true;
            }
        }
        return false;
    }

    // 递归扫描节点树
    private static boolean scanNodeTreeForXss(Node node) {
        // 检查当前节点
        if (isXssSensitive(node)) {
            return true;
        }

        // 检查所有属性
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                if (isXssSensitive(attr)) {
                    return true;
                }
            }
        }

        // 递归检查子节点
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (scanNodeTreeForXss(children.item(i))) {
                return true;
            }
        }

        return false;
    }

    // 检查节点是否包含XSS风险
    private static boolean isXssSensitive(Node node) {
        if (node == null) return false;

        // 检查节点名称（文本块类型）
        String nodeName = node.getNodeName().toLowerCase();
        boolean isTextChunk = nodeName.contains("text") ||
                nodeName.equals("textelement") ||
                nodeName.equals("textentry");

        // 获取文本内容
        String nodeValue = node.getNodeValue();
        if (nodeValue == null && node.getAttributes() != null) {
            Node valueAttr = node.getAttributes().getNamedItem("value");
            if (valueAttr != null) {
                nodeValue = valueAttr.getNodeValue();
            }
        }

        // 检查文本内容
        if (nodeValue != null && isXssContent(nodeValue)) {
            return true;
        }

        // 专门检查文本块
        if (isTextChunk) {
            // 检查关键字和文本值
            String keyword = getAttributeValue(node, "keyword");
            String text = getAttributeValue(node, "text");

            if (isXssContent(keyword) || isXssContent(text)) {
                return true;
            }
        }

        return false;
    }

    // 从节点属性获取值
    private static String getAttributeValue(Node node, String attrName) {
        if (node == null || attrName == null) return null;
        Node attr = node.getAttributes().getNamedItem(attrName);
        return attr != null ? attr.getNodeValue() : null;
    }

    // 检查文本是否包含XSS风险
    private static boolean isXssContent(String text) {
        if (text == null || text.isEmpty()) return false;

        String lowerText = text.toLowerCase();
        return lowerText.contains("<script") ||
                lowerText.contains("javascript:") ||
                lowerText.contains("onerror") ||
                lowerText.contains("onload") ||
                lowerText.contains("eval(") ||
                lowerText.contains("alert(") ||
                lowerText.contains("document.cookie") ||
                lowerText.contains("fromcharcode");
    }

    // 内部维度类
    private static class Dimension {
        public final int width;
        public final int height;

        public Dimension(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
