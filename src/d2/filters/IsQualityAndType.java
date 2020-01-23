package d2.filters;
import d2.items.instance.D2ItemInstance;


public class IsQualityAndType implements D2ItemFilter {
	private String quality;
	private String type;
	
	public IsQualityAndType(String quality, String type) {
		this.quality = quality;
		this.type = type;
	}
	
	@Override
	public boolean isAcceptable(D2ItemInstance item) {
		return item.itemType.type.code.equals(this.type) &&
				item.quality.equals(this.quality);
	}
}