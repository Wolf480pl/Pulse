/**
 * This file is part of Pulse, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 InspireNXE <http://inspirenxe.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.inspirenxe.server.network.protocol;

import org.inspirenxe.server.Game;
import org.inspirenxe.server.network.ServerProtocol;
import org.inspirenxe.server.network.codec.DisconnectCodec;
import org.inspirenxe.server.network.codec.play.JoinGameCodec;
import org.inspirenxe.server.network.codec.play.KeepAliveCodec;
import org.inspirenxe.server.network.message.DisconnectMessage;
import org.inspirenxe.server.network.message.play.JoinGameMessage;
import org.inspirenxe.server.network.message.play.KeepAliveMessage;

public class PlayProtocol extends ServerProtocol {
    public PlayProtocol(Game game) {
        super(game, "play", 64);
        registerMessage(INBOUND, KeepAliveMessage.class, KeepAliveCodec.class, null, 0);
        registerMessage(OUTBOUND, KeepAliveMessage.class, KeepAliveCodec.class, null, 0);
        registerMessage(OUTBOUND, JoinGameMessage.class, JoinGameCodec.class, null, 1);
        registerMessage(OUTBOUND, DisconnectMessage.class, DisconnectCodec.class, null, 40);
    }
}

