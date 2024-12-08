/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *                           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ================================================================================================
 *
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package androidx.test.rule;

import static org.robolectric.Shadows.shadowOf;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.internal.util.Checks;
import androidx.test.platform.app.InstrumentationRegistry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/*
 * Orca-specific changes:
 *
 * -  Separated commented statements;
 * -  Corrected strings' and comments' grammar;
 * -  Hard-coded 5-second connection timeout;
 * -  Idled Robolectric's main looper shadow upon service binding;
 * -  Inlined ServiceTestRule#waitOnLatch(CountDownLatch, String)'s actionName;
 * -  Made ProxyServiceConnection#callerConnection final;
 * -  Made ServiceTestRule#shutdownService() public;
 * -  Merged ServiceTestRule#bindService(Intent) and ServiceTestRule#bindService(Intent,
 *    ServiceConnection, int)
 * -  Privatized ServiceTestRule#serviceBound;
 * -  Privatized ServiceTestRule#serviceStarted;
 * -  Removed class qualifications from documentations;
 * -  Removed non-class, -constructor and -method-declaration-separating line breaks;
 * -  Removed ServiceTestRule#startService(Intent);
 * -  Removed trailing "seconds" comment from ServiceTestRule#TIMEOUT declaration;
 * -  Removed androidx.annotation.VisibleForTesting annotation from
 *    ServiceTestRule#shutdownService() and ServiceTestRule#bindServiceAndWait(Intent);
 * -  Renamed ServiceTestRule#bindServiceAndWait(Intent)'s local serviceConn variable to
 *    "serviceConnection";
 * -  Renamed ServiceTestRule#serviceConn to "serviceConnection";
 * -  Renamed ServiceTestRule#serviceBound to "isServiceBound";
 * -  Renamed ServiceTestRule#serviceStarted to "isServiceStarted";
 * -  Renamed ServiceTestRule#TIMEOUT to "TIMEOUT_IN_SECONDS"
 * -  Suppressed inaccessibility of IntentService#onHandleIntent(Intent) in the class'
 *    documentation;
 * -  Suppressed redundancy of ServiceTestRule#shutdownService()'s TimeoutException specification.
 */

/**
 * A JUnit rule that provides a simplified mechanism to start and shutdown your service before and
 * after the duration of your test. It also guarantees that the service is successfully connected
 * when starting (or binding to) a service. The service can be started (or bound) using one of the
 * helper methods. It will automatically be stopped (or unbound) after the test completes and any
 * methods annotated with <a href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>
 * After</code></a> are finished.
 *
 * <p>Note: This rule doesn't support {@link IntentService} because it's automatically destroyed
 * when {@link IntentService#onHandleIntent(Intent)} finishes all outstanding commands. So there is
 * no guarantee to establish a successful connection in a timely manner.
 *
 * <p>Usage:
 *
 * <pre>
 * &#064;Rule
 * public final ServiceTestRule mServiceRule = new ServiceTestRule();
 *
 * &#064;Test
 * public void testWithStartedService() {
 *     mServiceRule.startService(
 *         new Intent(InstrumentationRegistry.getTargetContext(), MyService.class));
 *     // Do something…
 * }
 *
 * &#064;Test
 * public void testWithBoundService() {
 *     IBinder binder = mServiceRule.bindService(
 *         new Intent(InstrumentationRegistry.getTargetContext(), MyService.class));
 *     MyService service = ((MyService.LocalBinder) binder).getService();
 *     assertTrue("True wasn't returned.", service.doSomethingToReturnTrue());
 * }
 * </pre>
 *
 * @noinspection JavadocReference
 */
public class ServiceTestRule implements TestRule {
  private static final String TAG = "ServiceTestRule";
  private static final long TIMEOUT_IN_SECONDS = 5L;

  private IBinder binder;
  private Intent serviceIntent;
  private ServiceConnection serviceConnection;
  private final TimeUnit timeUnit = TimeUnit.SECONDS;

  private boolean isServiceStarted = false;
  private boolean isServiceBound = false;

  /**
   * This class is used to wait until a successful connection to the service was established. It
   * then serves as a proxy to original {@link ServiceConnection} passed by the caller.
   */
  private class ProxyServiceConnection implements ServiceConnection {
    private final ServiceConnection callerConnection;
    public CountDownLatch connectedLatch = new CountDownLatch(1);

    private ProxyServiceConnection(ServiceConnection connection) {
      callerConnection = connection;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      // Store the service binder to return to the caller.
      binder = service;

      if (callerConnection != null) {
        // Pass through everything to the caller's service connection.
        callerConnection.onServiceConnected(name, service);
      }
      connectedLatch.countDown();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      // The process hosting the service has crashed or been killed.
      Log.e(TAG, "Connection to the service has been lost!");

      binder = null;
      if (callerConnection != null) {
        // Pass through everything to the caller's service connection.
        callerConnection.onServiceDisconnected(name);
      }
    }
  }

  /** Creates a {@link ServiceTestRule} with a default timeout of 5 seconds */
  public ServiceTestRule() {}

  /**
   * Binds the service under test, in the same way as if it were started by {@link
   * Context#bindService(Intent, ServiceConnection, int)} with an {@link Intent} that identifies a
   * service and an internal {@link ServiceConnection} to guarantee successful bound. However, it
   * waits for {@link ServiceConnection#onServiceConnected(ComponentName, IBinder)} to be called
   * before returning. The operation option flag defaults to {@link Context#BIND_AUTO_CREATE}.
   *
   * @param intent Identifies the service to connect to. The Intent may specify either an explicit
   *     component name, or a logical description (action, category, etc) to match an {@link
   *     IntentFilter} published by a service.
   * @return An object whose type is a subclass of IBinder, for making further calls into the
   *     service.
   * @throws SecurityException if the called doesn't have permission to bind to the given service.
   * @throws TimeoutException if timed out waiting for a successful connection with the service.
   */
  @Nullable
  public IBinder bindService(@NonNull Intent intent) throws TimeoutException {
    // No extras are expected by unbind.
    serviceIntent = Checks.checkNotNull(intent, "intent can't be null").cloneFilter();

    isServiceBound = bindServiceAndWait(intent);
    return binder;
  }

  /**
   * Unbinds the service under test that was previously bound by a call to {@link
   * #bindService(Intent)}. You normally do not need to call this method since your service will
   * automatically be stopped and unbound at the end of each test method.
   */
  public void unbindService() {
    if (isServiceBound) {
      InstrumentationRegistry.getInstrumentation()
          .getTargetContext()
          .unbindService(serviceConnection);
      binder = null;
      isServiceBound = false;
    }
  }

  /**
   * Makes the necessary calls to stop (or unbind) the service under test. This method is called
   * automatically called after test execution. This is not a blocking call since there is no
   * reliable way to guarantee successful disconnect without access to service lifecycle.
   *
   * @noinspection RedundantThrows
   */
  public void shutdownService() throws TimeoutException {
    if (isServiceStarted) {
      InstrumentationRegistry.getInstrumentation().getTargetContext().stopService(serviceIntent);
      isServiceStarted = false;
    }
    unbindService();
  }

  private boolean bindServiceAndWait(Intent intent) throws TimeoutException {
    ProxyServiceConnection serviceConnection = new ProxyServiceConnection(null);
    boolean isBound =
        InstrumentationRegistry.getInstrumentation()
            .getTargetContext()
            .bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    if (isBound) {
      shadowOf(Looper.getMainLooper()).idle();

      // Block until service connection is established.
      waitOnLatch(serviceConnection.connectedLatch);

      this.serviceConnection = serviceConnection;
    } else {
      Log.e(TAG, "Failed to bind to service! Is your service declared in the manifest?");
    }
    return isBound;
  }

  /** Helper method to block on a given latch for the duration of the set timeout. */
  private void waitOnLatch(CountDownLatch latch) throws TimeoutException {
    try {
      if (!latch.await(TIMEOUT_IN_SECONDS, timeUnit)) {
        throw new TimeoutException(
            "Waited for "
                + TIMEOUT_IN_SECONDS
                + " "
                + timeUnit.name()
                + ", but service was never connected.");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Interrupted while waiting for service to be connected.", e);
    }
  }

  /**
   * Override this method to do your own service specific initialization before starting or binding
   * to the service. The method is called before each test method is executed including any method
   * annotated with <a href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>
   * Before</code></a>. Do not start or bind to a service from here!
   */
  protected void beforeService() {}

  /**
   * Override this method to do your own service specific clean up after the service is shutdown.
   * The method is called after each test method is executed including any method annotated with <a
   * href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>After</code></a> and
   * after necessary calls to stop (or unbind) the service under test were called.
   */
  protected void afterService() {}

  @Override
  public Statement apply(final Statement base, Description description) {
    return new ServiceStatement(base);
  }

  /**
   * {@link Statement} that executes the service lifecycle methods before and after the execution of
   * the test.
   */
  private class ServiceStatement extends Statement {
    private final Statement base;

    public ServiceStatement(Statement base) {
      this.base = base;
    }

    @Override
    public void evaluate() throws Throwable {
      try {
        beforeService();
        base.evaluate();
      } finally {
        shutdownService();
        afterService();
      }
    }
  }
}
