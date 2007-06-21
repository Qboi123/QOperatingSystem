/*
 * Copyright 2000-2001 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package javax.sql;

/** 
 * <P>
 * An object that registers to be notified of events generated by a 
 * <code>PooledConnection</code> object.
 * <P>
 * The <code>ConnectionEventListener</code> interface is implemented by a
 * connection pooling component.  A connection pooling component will
 * usually be provided by a JDBC driver vendor or another system software
 * vendor.  A JDBC driver notifies a <code>ConnectionEventListener</code>
 * object when an application is finished using a pooled connection with
 * which the listener has registered.  The notification 
 * occurs after the application calls the method <code>close</code> on
 * its representation of a <code>PooledConnection</code> object.  A 
 * <code>ConnectionEventListener</code> is also notified when a
 * connection error occurs due to the fact that the <code>PooledConnection</code>
 * is unfit for future use---the server has crashed, for example. 
 * The listener is notified by the JDBC driver just before the driver throws an
 * <code>SQLException</code> to the application using the 
 * <code>PooledConnection</code> object.
 *
 * @since 1.4
 */

public interface ConnectionEventListener extends java.util.EventListener {

  /**
   * Notifies this <code>ConnectionEventListener</code> that
   * the application has called the method <code>close</code> on its
   * representation of a pooled connection.
   *
   * @param event an event object describing the source of 
   * the event
   */
  void connectionClosed(ConnectionEvent event);
      
  /**
   * Notifies this <code>ConnectionEventListener</code> that
   * a fatal error has occurred and the pooled connection can
   * no longer be used.  The driver makes this notification just
   * before it throws the application the <code>SQLException</code>
   * contained in the given <code>ConnectionEvent</code> object.
   *
   * @param event an event object describing the source of 
   * the event and containing the <code>SQLException</code> that the
   * driver is about to throw
   */
  void connectionErrorOccurred(ConnectionEvent event);

 } 





