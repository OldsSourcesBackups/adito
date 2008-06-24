
				/*
 *  Adito
 *
 *  Copyright (C) 2003-2006 3SP LTD. All Rights Reserved
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
			
package com.maverick.crypto.engines;

import java.io.IOException;

/**
 *
 * <p>The base interface of all cipher engines.</p>
 * @author Lee David Painter
 */
public interface CipherEngine {
  public void init(boolean forEncryption, byte[] key);

  public int getBlockSize();

  public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws
      IOException;
}
