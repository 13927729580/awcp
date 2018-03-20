package cn.org.awcp.wechat.domain.menu;

import java.util.List;

/**
 * 微信菜单
 * @author yqtao
 *
 */
public class WeChatMenu {

	//最多3个
	private List<WeChatButton> button;

	/**
	 * 
	 * @return
	 */
	public List<WeChatButton> getButton() {
		return button;
	}

	/**
	 * 
	 * @param button
	 */
	public void setButton(List<WeChatButton> button) {
		this.button = button;
	}

	@Override
    public String toString() {
		String str = "menu:\n";
		for(int i=0;i<button.size();i++){
			str += button.get(i) + "\n";
		}
		return str;
	}

}
