package de.ngloader.scoreboard;

import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ComponentUtil {

	public static final WrappedChatComponent EMPTY_CHAT_COMPONENT = WrappedChatComponent.fromText("");
	public static final BaseComponent EMPTY_BASE_COMPONENT = new TextComponent();

	public static String createHashCode(Object object) {
		return Integer.toString(System.identityHashCode(object), 16).replaceAll("(.)", "§$1");
	}

	public static BaseComponent mergeBaseComponent(BaseComponent... components) {
		return switch (components.length) {
		case 0 -> EMPTY_BASE_COMPONENT;
		case 1 -> components[0];
		default -> {
			BaseComponent parent = components[0];
			for (int i = 1; i < components.length; i++) {
				parent.addExtra(components[i]);
			}
			yield parent;
		}
		};
	}

	public static WrappedChatComponent mergeBaseComponentWrapped(BaseComponent... components) {
		return WrappedChatComponent.fromJson(ComponentSerializer.toString(mergeBaseComponent(components)));
	}
}
