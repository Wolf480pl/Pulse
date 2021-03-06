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
package org.inspirenxe.server;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.inspirenxe.server.input.Input;
import org.inspirenxe.server.network.Network;

public class Game {
    private static final String version;
    // A semaphore with no permits, so that the first acquire() call blocks
    private final Semaphore semaphore = new Semaphore(0);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Configuration configuration;
    private final Logger logger;
    private final Input input;
    private final Network network;

    static {
        version = Game.class.getPackage().getImplementationVersion();
    }

    public Game(Configuration configuration) {
        this.configuration = configuration;
        logger = LogManager.getLogger(configuration.getName());
        input = new Input(this);
        network = new Network(this);
    }

    private void start() {
        logger.info("Starting game, running version " + version + ", please wait a moment");
        input.start();
        network.start();
    }

    private void stop() {
        logger.info("Stopping game, please wait a moment");
        input.stop();
        network.stop();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Logger getLogger() {
        return logger;
    }

    public Input getInput() {
        return input;
    }

    public Network getNetwork() {
        return network;
    }

    public String getVersion() {
        return version;
    }

    /**
     * Starts the game and causes the current thread to wait until the {@link #close()} method is called. When this happens, the thread resumes and the game is stopped. Interrupting the thread will
     * not cause it to close, only calling {@link #close()} will. Calls to {@link #close()} before open() are not counted.
     */
    public void open() {
        // Only start the game if running has a value of false, in which case it's set to true and the if statement passes
        if (running.compareAndSet(false, true)) {
            // Start the threads, which might release permits by calling close() before all are started
            start();
            // Attempts to acquire a permit, but since none are available (except for the situation stated above), the thread blocks
            semaphore.acquireUninterruptibly();
            // A permit was acquired, which means close() was called; so we stop game. The available permit count returns to zero
            stop();
        }
    }

    /**
     * Wakes up the thread that has opened the game (by having called {@link #open()}) and allows it to resume it's activity to trigger the end of the game.
     */
    public void close() {
        // Only stop the game if running has a value of true, in which case it's set to false and the if statement passes
        if (running.compareAndSet(true, false)) {
            // Release a permit (which doesn't need to be held by the thread in the first place),
            // allowing the main thread to acquire one and resume to close the game
            semaphore.release();
            // The available permit count is now non-zero
        }
    }

    /**
     * Returns true if the game is running, false if otherwise.
     *
     * @return Whether or not the game is running
     */
    public boolean isRunning() {
        return running.get();
    }
}

