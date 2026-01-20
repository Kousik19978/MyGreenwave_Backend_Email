package in.co.gw.utility;

public class ImageData {
	private byte[] data;
    private String contentType;

    public ImageData(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }
}
