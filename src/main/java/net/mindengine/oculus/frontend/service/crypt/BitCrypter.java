/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.service.crypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Used to hide a set of boolean/bit variables (0-1 or true-false) in single
 * string Can be used to hide all permission codes for users in single field
 * 
 * @author ishubin
 */
public class BitCrypter {
	public final static int capacity = 4;

	public class Block {
		public Block() {
		}

		public int offset = 0;

		public Map<Integer, Integer> codes = new HashMap<Integer, Integer>();
	}

	public BitCrypter() {

	}

	public String encrypt(List<Integer> codes) {
		Map<Integer, Block> blocks = new HashMap<Integer, Block>();

		int maxOffset = 0;
		for (int i = 0; i < codes.size(); i++) {
			int offset = (codes.get(i) - 1) / capacity;
			if (maxOffset < offset)
				maxOffset = offset;
			Block block = blocks.get(offset);
			if (block == null) {
				block = new Block();
				blocks.put(offset, block);
				block.offset = offset;
			}
			block.codes.put((codes.get(i) - capacity * offset), 1);
		}

		String encrypted = "";
		for (int i = 0; i <= maxOffset; i++) {
			Block block = blocks.get(i);
			if (block == null) {
				encrypted += "0";
			}
			else {
				encrypted += convert(block);
			}
		}
		return encrypted;
	}

	private char convert(Block block) {
		Set<Integer> set = block.codes.keySet();

		int d = 0;
		for (Integer code : set) {
			if (code != null) {
				int c = code.intValue();
				d = d | ((2 << (c - 1)) / 2);
			}
		}
		if (d < 10) {
			return (char) (48 + d);
		}
		else
			return (char) (97 + d - 10);
	}

	public boolean hasCode(String encrypted, Integer code) {
		if (code != null) {
			int offset = (code - 1) / capacity;
			if (offset < encrypted.length()) {
				char ch = encrypted.charAt(offset);
				return hasCode(ch, code - offset * capacity);
			}
		}
		return false;
	}

	public List<Integer> decrypt(String encrypted) {
		List<Integer> codes = new ArrayList<Integer>();

		for (int offset = 0; offset < encrypted.length(); offset++) {
			char ch = encrypted.charAt(offset);
			int d = 0;
			if (ch >= 97) {
				d = ch - 97 + 10;
			}
			else
				d = ch - 48;

			for (int i = 1; i <= capacity; i++) {
				int c = (2 << (i - 1)) / 2;
				if ((d & c) > 0)
					codes.add(i + capacity * offset);
			}
		}
		return codes;
	}

	private boolean hasCode(char ch, int code) {
		int d = 0;
		if (ch >= 97) {
			d = ch - 97 + 10;
		}
		else
			d = ch - 48;

		int c = (2 << (code - 1)) / 2;

		return ((d & c) > 0);
	}

}
