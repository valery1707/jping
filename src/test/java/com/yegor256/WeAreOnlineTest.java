/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yegor256;

import java.lang.reflect.AnnotatedElement;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.ReflectionUtils;

/**
 * Test case for {@link WeAreOnline}.
 *
 * @since 0.1.0
 */
@ExtendWith(WeAreOnline.class)
final class WeAreOnlineTest {

    @Test
    void checksOnlineStatus() {
        MatcherAssert.assertThat(
            new WeAreOnline().evaluateExecutionCondition(null).isDisabled(),
            Matchers.is(false)
        );
    }

    @Test
    void checkOfflineStatus() {
        final AnnotatedElement element = ReflectionUtils.getRequiredMethod(
            WeAreOnlineTest.class, "overrideTimeout"
        );
        final ExtensionContext context = WeAreOnlineDefaultContext.withElement(element);
        MatcherAssert.assertThat(
            new WeAreOnline().evaluateExecutionCondition(context).isDisabled(),
            Matchers.is(true)
        );
    }

    @Test
    void checkInvertOnlineStatus() {
        final AnnotatedElement element = ReflectionUtils.getRequiredMethod(
            WeAreOnlineTest.class, "overrideOfflineMode"
        );
        final ExtensionContext context = WeAreOnlineDefaultContext.withElement(element);
        MatcherAssert.assertThat(
            new WeAreOnline().evaluateExecutionCondition(context).isDisabled(),
            Matchers.is(true)
        );
    }

    @SuppressWarnings("unused")
    @OnlineMeans(connectTimeout = 1, readTimeout = 1)
    private void overrideTimeout() {
        // empty method for test override annotation
    }

    @SuppressWarnings("unused")
    @OnlineMeans(offline = true)
    private void overrideOfflineMode() {
        // empty method for test override annotation
    }
}
