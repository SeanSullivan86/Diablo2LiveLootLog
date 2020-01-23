package d2.filters;

import d2.items.instance.D2ItemInstance;

interface D2ItemFilter {
	boolean isAcceptable(D2ItemInstance item);
}