package org.useitem;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor{
	private UseItem main = null;
	public Command() {
		main = UseItem.main;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if(!sender.isOp()) {
			sender.sendMessage("��c��û��Ȩ��ִ�и�����");
		}
		if(args.length == 0) {
			sender.sendMessage("��b��m��l=====��a UseItem ��b��m��l=====");
		    sender.sendMessage("��c/" + label + " reload ��7 - ����");
			sender.sendMessage("��b��m��l============");
		}else if(args.length == 1 && args[0].equalsIgnoreCase("reload")) { 
			main.reloadConfig();
			sender.sendMessage("��a[McendBase] ��a�����ļ������أ�");
		}else {
			sender.sendMessage("��a[McendBase] ��c�������");
		}
		return false;
	}

}